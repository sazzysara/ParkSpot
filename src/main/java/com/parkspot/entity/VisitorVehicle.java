package com.parkspot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Represents a visitor vehicle entry record.
 * Each record captures a single parking session: entry → exit.
 *
 * Relationships:
 *   - ManyToOne → Flat       (which flat the visitor is visiting)
 *   - ManyToOne → ParkingSlot (which slot was assigned)
 */
@Entity
@Table(name = "visitor_vehicles")
public class VisitorVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vehicle number is required")
    @Size(max = 20, message = "Vehicle number must be at most 20 characters")
    @Column(name = "vehicle_number", nullable = false, length = 20)
    private String vehicleNumber;

    @Size(max = 100, message = "Owner name must be at most 100 characters")
    @Column(name = "visitor_name", length = 100)
    private String visitorName;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    // ─── Constructors ───

    public VisitorVehicle() {
    }

    public VisitorVehicle(String vehicleNumber, String visitorName,
                          LocalDateTime entryTime, Flat flat, ParkingSlot parkingSlot) {
        this.vehicleNumber = vehicleNumber;
        this.visitorName = visitorName;
        this.entryTime = entryTime;
        this.flat = flat;
        this.parkingSlot = parkingSlot;
    }

    // ─── Helper ───

    /**
     * Returns true if the vehicle is still parked (no exit time recorded).
     */
    public boolean isCurrentlyParked() {
        return this.exitTime == null;
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

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }
}
