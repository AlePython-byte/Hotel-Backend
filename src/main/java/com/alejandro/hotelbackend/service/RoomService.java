package com.alejandro.hotelbackend.service;

import com.alejandro.hotelbackend.model.Reservation;
import com.alejandro.hotelbackend.model.Room;
import com.alejandro.hotelbackend.model.RoomType;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    List<Room> getAvailableRooms(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate);

    Room assignAvailableRoom(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate);

    void releaseRoom(Room room);

    boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate);

    List<Reservation> getActiveReservations();
}