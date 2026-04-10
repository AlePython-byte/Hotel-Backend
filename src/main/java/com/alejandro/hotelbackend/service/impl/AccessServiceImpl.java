package com.alejandro.hotelbackend.service.impl;

import com.alejandro.hotelbackend.model.DigitalKey;
import com.alejandro.hotelbackend.model.Reservation;
import com.alejandro.hotelbackend.service.AccessService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccessServiceImpl implements AccessService {

    @Override
    public DigitalKey generateDigitalKey(Reservation reservation) {
        return DigitalKey.builder()
                .code(UUID.randomUUID().toString())
                .active(true)
                .build();
    }

    @Override
    public void deactivateDigitalKey(DigitalKey digitalKey) {
        if (digitalKey != null) {
            digitalKey.setActive(false);
        }
    }
}