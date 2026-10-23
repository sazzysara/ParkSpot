package com.parkspot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request payload for logging a new visitor vehicle entry.
 */
public class VehicleEntryRequest {

    @NotBlank(message = "Vehicle number is required")
    @Size(max = 20, message = "Vehicle number must be at most 20 characters")
    private String vehicleNumber;

    @Size(max = 100, message = "Visitor name must be at most 100 characters")
    private String visitorName;

    @NotBlank(message = "Flat number is required")
    private String flatNumber;

    @NotNull(message = "Slot number is required")
    @Positive(message = "Slot number must be a positive integer")
    private Integer slotNumber;

    // ─── Getters and Setters ───

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }
}
