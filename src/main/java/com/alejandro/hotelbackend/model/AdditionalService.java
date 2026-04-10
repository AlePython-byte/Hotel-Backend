package com.alejandro.hotelbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalService {
    private Long id;
    private AdditionalServiceType type;
    private String name;
    private double price;
}