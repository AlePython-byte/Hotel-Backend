package com.alejandro.hotelbackend.service;

import com.alejandro.hotelbackend.model.RoomType;
import com.alejandro.hotelbackend.model.SeasonType;

public interface RateService {

    double calculateRoomPrice(RoomType roomType, double basePrice, int nights, SeasonType seasonType);

    SeasonType determineSeasonType(int month);
}