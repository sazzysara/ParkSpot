package com.parkspot.dto;

/**
 * Response payload for the parking slot status.
 */
public class SlotStatusResponse {

    private Long id;
    private Integer slotNumber;
    private boolean occupied;
    private String currentVehicleNumber;
    private String currentVisitorName;
    private String visitingFlat;

    // ─── Constructors ───

    public SlotStatusResponse() {
    }

    public SlotStatusResponse(Long id, Integer slotNumber, boolean occupied,
                              String currentVehicleNumber, String currentVisitorName,
                              String visitingFlat) {
        this.id = id;
        this.slotNumber = slotNumber;
        this.occupied = occupied;
        this.currentVehicleNumber = currentVehicleNumber;
        this.currentVisitorName = currentVisitorName;
        this.visitingFlat = visitingFlat;
    }

    // ─── Getters and Setters ───

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(Integer slotNumber) {
        this.slotNumber = slotNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getCurrentVehicleNumber() {
        return currentVehicleNumber;
    }

    public void setCurrentVehicleNumber(String currentVehicleNumber) {
        this.currentVehicleNumber = currentVehicleNumber;
    }

    public String getCurrentVisitorName() {
        return currentVisitorName;
    }

    public void setCurrentVisitorName(String currentVisitorName) {
        this.currentVisitorName = currentVisitorName;
    }

    public String getVisitingFlat() {
        return visitingFlat;
    }

    public void setVisitingFlat(String visitingFlat) {
        this.visitingFlat = visitingFlat;
    }
}
