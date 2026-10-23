package com.parkspot.dto;

import java.time.LocalDateTime;

/**
 * Response payload returned when querying visitor vehicle records.
 */
public class VehicleEntryResponse {

    private Long id;
    private String vehicleNumber;
    private String visitorName;
    private String flatNumber;
    private Integer slotNumber;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private boolean currentlyParked;

    // ─── Constructors ───

    public VehicleEntryResponse() {
    }

    public VehicleEntryResponse(Long id, String vehicleNumber, String visitorName,
                                String flatNumber, Integer slotNumber,
                                LocalDateTime entryTime, LocalDateTime exitTime,
                                boolean currentlyParked) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.visitorName = visitorName;
        this.flatNumber = flatNumber;
        this.slotNumber = slotNumber;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.currentlyParked = currentlyParked;
    }

    // ─── Getters and Setters ───

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public boolean isCurrentlyParked() {
        return currentlyParked;
    }

    public void setCurrentlyParked(boolean currentlyParked) {
        this.currentlyParked = currentlyParked;
    }
}
