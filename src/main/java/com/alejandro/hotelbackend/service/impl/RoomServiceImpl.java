package com.alejandro.hotelbackend.service.impl;

import com.alejandro.hotelbackend.exception.BusinessException;
import com.alejandro.hotelbackend.model.Reservation;
import com.alejandro.hotelbackend.model.ReservationStatus;
import com.alejandro.hotelbackend.model.Room;
import com.alejandro.hotelbackend.model.RoomType;
import com.alejandro.hotelbackend.repository.ReservationRepository;
import com.alejandro.hotelbackend.repository.RoomRepository;
import com.alejandro.hotelbackend.service.RoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomServiceImpl(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Room> getAvailableRooms(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        validateDates(checkInDate, checkOutDate);

        return roomRepository.findByType(roomType).stream()
                .filter(room -> isRoomAvailable(room, checkInDate, checkOutDate))
                .toList();
    }

    @Override
    public Room assignAvailableRoom(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> availableRooms = getAvailableRooms(roomType, checkInDate, checkOutDate);

        if (availableRooms.isEmpty()) {
            throw new BusinessException("No rooms available for the selected type and dates");
        }

        Room room = availableRooms.get(0);
        room.setAvailable(false);
        roomRepository.save(room);

        return room;
    }

    @Override
    public void releaseRoom(Room room) {
        if (room != null) {
            room.setAvailable(true);
            roomRepository.save(room);
        }
    }

    @Override
    public boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> activeReservations = getActiveReservations();

        for (Reservation reservation : activeReservations) {
            if (reservation.getRoom() != null
                    && reservation.getRoom().getId().equals(room.getId())
                    && datesOverlap(checkInDate, checkOutDate, reservation.getCheckInDate(), reservation.getCheckOutDate())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public List<Reservation> getActiveReservations() {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CREATED
                        || reservation.getStatus() == ReservationStatus.CHECKED_IN)
                .toList();
    }

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new BusinessException("Check-in and check-out dates are required");
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new BusinessException("Check-out date must be after check-in date");
        }
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}