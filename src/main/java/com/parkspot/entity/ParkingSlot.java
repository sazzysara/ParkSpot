package com.parkspot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical parking slot in the society.
 * A slot can be occupied by at most one visitor vehicle at a time.
 * Historical records are maintained through the VisitorVehicle relationship.
 */
@Entity
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Slot number is required")
    @Positive(message = "Slot number must be a positive integer")
    @Column(name = "slot_number", nullable = false, unique = true)
    private Integer slotNumber;

    @Column(name = "is_occupied", nullable = false)
    private boolean occupied = false;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingSlot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisitorVehicle> visitorVehicles = new ArrayList<>();

    // ─── Constructors ───

    public ParkingSlot() {
    }

    public ParkingSlot(Integer slotNumber) {
        this.slotNumber = slotNumber;
        this.occupied = false;
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

    public List<VisitorVehicle> getVisitorVehicles() {
        return visitorVehicles;
    }

    public void setVisitorVehicles(List<VisitorVehicle> visitorVehicles) {
        this.visitorVehicles = visitorVehicles;
    }
}
