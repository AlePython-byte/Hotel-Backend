package com.alejandro.hotelbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private Long reservationId;

    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    private double total;
}