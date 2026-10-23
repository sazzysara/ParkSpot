package com.parkspot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a flat/apartment in the residential society.
 * Each flat can have multiple visitor vehicles visiting it over time.
 */
@Entity
@Table(name = "flats")
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flat number is required")
    @Size(max = 10, message = "Flat number must be at most 10 characters")
    @Column(name = "flat_number", nullable = false, unique = true, length = 10)
    private String flatNumber;

    @Size(max = 100, message = "Owner name must be at most 100 characters")
    @Column(name = "owner_name", length = 100)
    private String ownerName;

    @Size(max = 15, message = "Contact number must be at most 15 characters")
    @Column(name = "contact_number", length = 15)
    private String contactNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VisitorVehicle> visitorVehicles = new ArrayList<>();

    // ─── Constructors ───

    public Flat() {
    }

    public Flat(String flatNumber, String ownerName, String contactNumber) {
        this.flatNumber = flatNumber;
        this.ownerName = ownerName;
        this.contactNumber = contactNumber;
    }

    // ─── Getters and Setters ───

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<VisitorVehicle> getVisitorVehicles() {
        return visitorVehicles;
    }

    public void setVisitorVehicles(List<VisitorVehicle> visitorVehicles) {
        this.visitorVehicles = visitorVehicles;
    }
}
