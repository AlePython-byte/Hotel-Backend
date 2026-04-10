package com.alejandro.hotelbackend.service.impl;

import com.alejandro.hotelbackend.model.AdditionalService;
import com.alejandro.hotelbackend.model.Invoice;
import com.alejandro.hotelbackend.model.InvoiceItem;
import com.alejandro.hotelbackend.model.Reservation;
import com.alejandro.hotelbackend.service.BillingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    @Override
    public Invoice generateInvoice(Reservation reservation) {
        List<InvoiceItem> items = new ArrayList<>();

        double servicesTotal = 0.0;

        for (AdditionalService additionalService : reservation.getAdditionalServices()) {
            items.add(InvoiceItem.builder()
                    .description(additionalService.getName())
                    .amount(additionalService.getPrice())
                    .build());

            servicesTotal += additionalService.getPrice();
        }

        double roomCharge = reservation.getTotalPrice() - servicesTotal;

        items.add(0, InvoiceItem.builder()
                .description("Room charge")
                .amount(roomCharge)
                .build());

        return Invoice.builder()
                .reservationId(reservation.getId())
                .items(items)
                .total(reservation.getTotalPrice())
                .build();
    }
}