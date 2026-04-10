package com.alejandro.hotelbackend.dto;

public class InvoiceItemResponse {

    private String description;
    private double amount;

    public InvoiceItemResponse() {
    }

    public InvoiceItemResponse(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}