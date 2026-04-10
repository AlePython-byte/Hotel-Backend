package com.alejandro.hotelbackend.dto;

import java.util.ArrayList;
import java.util.List;

public class InvoiceResponse {

    private Long reservationId;
    private List<InvoiceItemResponse> items = new ArrayList<>();
    private double total;

    public InvoiceResponse() {
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public List<InvoiceItemResponse> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemResponse> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}