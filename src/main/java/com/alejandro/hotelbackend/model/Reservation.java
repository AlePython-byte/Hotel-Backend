package com.alejandro.hotelbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long id;
    private String guestName;
    private String guestEmail;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int nights;
    private SeasonType seasonType;
    private ReservationStatus status;

    @Builder.Default
    private List<AdditionalService> additionalServices = new ArrayList<>();

    private double totalPrice;
    private DigitalKey digitalKey;
    private Invoice invoice;
}