package com.alejandro.hotelbackend.service.impl;

import com.alejandro.hotelbackend.exception.BusinessException;
import com.alejandro.hotelbackend.model.AdditionalService;
import com.alejandro.hotelbackend.model.AdditionalServiceType;
import com.alejandro.hotelbackend.service.AdditionalServiceService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {

    private final AtomicLong serviceIdGenerator = new AtomicLong(1);

    @Override
    public AdditionalService createService(AdditionalServiceType serviceType, int nights) {
        double price;
        String name;

        switch (serviceType) {
            case SPA -> {
                name = "Spa";
                price = 50.0;
            }
            case BREAKFAST -> {
                name = "Breakfast";
                price = 15.0 * nights;
            }
            case TRANSPORT -> {
                name = "Transport";
                price = 30.0;
            }
            default -> throw new BusinessException("Invalid additional service type");
        }

        return AdditionalService.builder()
                .id(serviceIdGenerator.getAndIncrement())
                .type(serviceType)
                .name(name)
                .price(price)
                .build();
    }
}