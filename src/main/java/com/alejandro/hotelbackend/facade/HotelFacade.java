package com.alejandro.hotelbackend.facade;

import com.alejandro.hotelbackend.dto.AvailabilityResponse;
import com.alejandro.hotelbackend.dto.InvoiceItemResponse;
import com.alejandro.hotelbackend.dto.InvoiceResponse;
import com.alejandro.hotelbackend.dto.ReservationResponse;
import com.alejandro.hotelbackend.dto.CreateReservationRequest;
import com.alejandro.hotelbackend.exception.BusinessException;
import com.alejandro.hotelbackend.exception.ResourceNotFoundException;
import com.alejandro.hotelbackend.model.AdditionalService;
import com.alejandro.hotelbackend.model.AdditionalServiceType;
import com.alejandro.hotelbackend.model.DigitalKey;
import com.alejandro.hotelbackend.model.Invoice;
import com.alejandro.hotelbackend.model.InvoiceItem;
import com.alejandro.hotelbackend.model.Reservation;
import com.alejandro.hotelbackend.model.ReservationStatus;
import com.alejandro.hotelbackend.model.Room;
import com.alejandro.hotelbackend.model.RoomType;
import com.alejandro.hotelbackend.model.SeasonType;
import com.alejandro.hotelbackend.repository.ReservationRepository;
import com.alejandro.hotelbackend.service.AccessService;
import com.alejandro.hotelbackend.service.AdditionalServiceService;
import com.alejandro.hotelbackend.service.BillingService;
import com.alejandro.hotelbackend.service.RateService;
import com.alejandro.hotelbackend.service.RoomService;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class HotelFacade {

    private final RoomService roomService;
    private final RateService rateService;
    private final AdditionalServiceService additionalServiceService;
    private final BillingService billingService;
    private final AccessService accessService;
    private final ReservationRepository reservationRepository;

    public HotelFacade(RoomService roomService,
                       RateService rateService,
                       AdditionalServiceService additionalServiceService,
                       BillingService billingService,
                       AccessService accessService,
                       ReservationRepository reservationRepository) {
        this.roomService = roomService;
        this.rateService = rateService;
        this.additionalServiceService = additionalServiceService;
        this.billingService = billingService;
        this.accessService = accessService;
        this.reservationRepository = reservationRepository;
    }

    public AvailabilityResponse getAvailability(RoomType roomType,
                                                java.time.LocalDate checkInDate,
                                                java.time.LocalDate checkOutDate) {
        List<Room> availableRooms = roomService.getAvailableRooms(roomType, checkInDate, checkOutDate);

        AvailabilityResponse response = new AvailabilityResponse();
        response.setRoomType(roomType);
        response.setCheckInDate(checkInDate);
        response.setCheckOutDate(checkOutDate);
        response.setAvailable(!availableRooms.isEmpty());
        response.setAvailableRooms(
                availableRooms.stream()
                        .map(Room::getRoomNumber)
                        .toList()
        );

        return response;
    }

    public ReservationResponse createReservation(CreateReservationRequest request) {
        validateReservationDates(request.getCheckInDate(), request.getCheckOutDate());

        Room room = roomService.assignAvailableRoom(
                request.getRoomType(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        int nights = (int) ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        SeasonType seasonType = rateService.determineSeasonType(request.getCheckInDate().getMonthValue());

        double roomPrice = rateService.calculateRoomPrice(
                room.getType(),
                room.getBasePrice(),
                nights,
                seasonType
        );

        Reservation reservation = Reservation.builder()
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .nights(nights)
                .seasonType(seasonType)
                .status(ReservationStatus.CREATED)
                .totalPrice(roomPrice)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        savedReservation.setInvoice(billingService.generateInvoice(savedReservation));
        reservationRepository.save(savedReservation);

        return mapToReservationResponse(savedReservation);
    }

    public ReservationResponse addService(Long reservationId, AdditionalServiceType serviceType) {
        Reservation reservation = findReservationOrThrow(reservationId);

        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT
                || reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException("Cannot add services to a completed or cancelled reservation");
        }

        AdditionalService additionalService = additionalServiceService.createService(serviceType, reservation.getNights());
        reservation.getAdditionalServices().add(additionalService);
        reservation.setTotalPrice(reservation.getTotalPrice() + additionalService.getPrice());
        reservation.setInvoice(billingService.generateInvoice(reservation));

        reservationRepository.save(reservation);

        return mapToReservationResponse(reservation);
    }

    public ReservationResponse checkIn(Long reservationId) {
        Reservation reservation = findReservationOrThrow(reservationId);

        if (reservation.getStatus() == ReservationStatus.CHECKED_IN) {
            throw new BusinessException("Reservation is already checked in");
        }

        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new BusinessException("Reservation is already checked out");
        }

        DigitalKey digitalKey = accessService.generateDigitalKey(reservation);
        reservation.setDigitalKey(digitalKey);
        reservation.setStatus(ReservationStatus.CHECKED_IN);

        reservationRepository.save(reservation);

        return mapToReservationResponse(reservation);
    }

    public ReservationResponse checkOut(Long reservationId) {
        Reservation reservation = findReservationOrThrow(reservationId);

        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new BusinessException("Reservation must be checked in before check-out");
        }

        if (reservation.getDigitalKey() != null) {
            accessService.deactivateDigitalKey(reservation.getDigitalKey());
        }

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.setInvoice(billingService.generateInvoice(reservation));
        roomService.releaseRoom(reservation.getRoom());

        reservationRepository.save(reservation);

        return mapToReservationResponse(reservation);
    }

    public ReservationResponse getReservationById(Long reservationId) {
        Reservation reservation = findReservationOrThrow(reservationId);
        return mapToReservationResponse(reservation);
    }

    private Reservation findReservationOrThrow(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + reservationId));
    }

    private void validateReservationDates(java.time.LocalDate checkInDate, java.time.LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new BusinessException("Check-in and check-out dates are required");
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new BusinessException("Check-out date must be after check-in date");
        }
    }

    private ReservationResponse mapToReservationResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setReservationId(reservation.getId());
        response.setGuestName(reservation.getGuestName());
        response.setGuestEmail(reservation.getGuestEmail());

        if (reservation.getRoom() != null) {
            response.setRoomNumber(reservation.getRoom().getRoomNumber());
            response.setRoomType(reservation.getRoom().getType());
        }

        response.setCheckInDate(reservation.getCheckInDate());
        response.setCheckOutDate(reservation.getCheckOutDate());
        response.setNights(reservation.getNights());
        response.setSeasonType(reservation.getSeasonType());
        response.setStatus(reservation.getStatus());
        response.setAdditionalServices(
                reservation.getAdditionalServices().stream()
                        .map(AdditionalService::getName)
                        .toList()
        );
        response.setTotalPrice(reservation.getTotalPrice());

        if (reservation.getDigitalKey() != null) {
            response.setDigitalKeyCode(reservation.getDigitalKey().getCode());
        }

        if (reservation.getInvoice() != null) {
            response.setInvoice(mapToInvoiceResponse(reservation.getInvoice()));
        }

        return response;
    }

    private InvoiceResponse mapToInvoiceResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setReservationId(invoice.getReservationId());
        response.setTotal(invoice.getTotal());

        List<InvoiceItemResponse> items = invoice.getItems().stream()
                .map(this::mapToInvoiceItemResponse)
                .toList();

        response.setItems(items);
        return response;
    }

    private InvoiceItemResponse mapToInvoiceItemResponse(InvoiceItem invoiceItem) {
        return new InvoiceItemResponse(invoiceItem.getDescription(), invoiceItem.getAmount());
    }
}