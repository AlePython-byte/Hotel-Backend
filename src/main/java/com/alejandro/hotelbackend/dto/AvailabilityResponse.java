package com.alejandro.hotelbackend.dto;

import com.alejandro.hotelbackend.model.RoomType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityResponse {

    private RoomType roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean available;
    private List<String> availableRooms = new ArrayList<>();

    public AvailabilityResponse() {
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(List<String> availableRooms) {
        this.availableRooms = availableRooms;
    }
}