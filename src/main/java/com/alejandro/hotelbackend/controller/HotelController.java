package com.alejandro.hotelbackend.controller;

import com.alejandro.hotelbackend.dto.AddServiceRequest;
import com.alejandro.hotelbackend.dto.AvailabilityResponse;
import com.alejandro.hotelbackend.dto.CreateReservationRequest;
import com.alejandro.hotelbackend.dto.ReservationResponse;
import com.alejandro.hotelbackend.facade.HotelFacade;
import com.alejandro.hotelbackend.model.RoomType;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin(origins = "*")
public class HotelController {

    private final HotelFacade hotelFacade;

    public HotelController(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    @PostMapping("/reservar")
    public ReservationResponse createReservation(@Valid @RequestBody CreateReservationRequest request) {
        return hotelFacade.createReservation(request);
    }

    @GetMapping("/disponibilidad")
    public AvailabilityResponse getAvailability(@RequestParam RoomType roomType,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        return hotelFacade.getAvailability(roomType, checkInDate, checkOutDate);
    }

    @PostMapping("/servicios/{reservaId}")
    public ReservationResponse addService(@PathVariable Long reservaId,
                                          @Valid @RequestBody AddServiceRequest request) {
        return hotelFacade.addService(reservaId, request.getServiceType());
    }

    @PutMapping("/checkin/{reservaId}")
    public ReservationResponse checkIn(@PathVariable Long reservaId) {
        return hotelFacade.checkIn(reservaId);
    }

    @PutMapping("/checkout/{reservaId}")
    public ReservationResponse checkOut(@PathVariable Long reservaId) {
        return hotelFacade.checkOut(reservaId);
    }

    @GetMapping("/reserva/{reservaId}")
    public ReservationResponse getReservationById(@PathVariable Long reservaId) {
        return hotelFacade.getReservationById(reservaId);
    }
}