package com.alejandro.hotelbackend.service;

import com.alejandro.hotelbackend.model.DigitalKey;
import com.alejandro.hotelbackend.model.Reservation;

public interface AccessService {

    DigitalKey generateDigitalKey(Reservation reservation);

    void deactivateDigitalKey(DigitalKey digitalKey);
}