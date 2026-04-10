package com.alejandro.hotelbackend.service.impl;

import com.alejandro.hotelbackend.model.RoomType;
import com.alejandro.hotelbackend.model.SeasonType;
import com.alejandro.hotelbackend.service.RateService;
import org.springframework.stereotype.Service;

@Service
public class RateServiceImpl implements RateService {

    @Override
    public double calculateRoomPrice(RoomType roomType, double basePrice, int nights, SeasonType seasonType) {
        double multiplier = seasonType == SeasonType.HIGH ? 1.5 : 1.0;
        return basePrice * nights * multiplier;
    }

    @Override
    public SeasonType determineSeasonType(int month) {
        if (month == 6 || month == 7 || month == 12) {
            return SeasonType.HIGH;
        }
        return SeasonType.LOW;
    }
}