package com.alejandro.hotelbackend.dto;

import com.alejandro.hotelbackend.model.AdditionalServiceType;
import jakarta.validation.constraints.NotNull;

public class AddServiceRequest {

    @NotNull(message = "Service type is required")
    private AdditionalServiceType serviceType;

    public AddServiceRequest() {
    }

    public AdditionalServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(AdditionalServiceType serviceType) {
        this.serviceType = serviceType;
    }
}