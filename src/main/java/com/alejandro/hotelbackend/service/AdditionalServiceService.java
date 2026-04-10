package com.alejandro.hotelbackend.service;

import com.alejandro.hotelbackend.model.AdditionalService;
import com.alejandro.hotelbackend.model.AdditionalServiceType;

public interface AdditionalServiceService {

    AdditionalService createService(AdditionalServiceType serviceType, int nights);
}