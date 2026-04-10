package com.alejandro.hotelbackend.service;

import com.alejandro.hotelbackend.model.Invoice;
import com.alejandro.hotelbackend.model.Reservation;

public interface BillingService {

    Invoice generateInvoice(Reservation reservation);
}