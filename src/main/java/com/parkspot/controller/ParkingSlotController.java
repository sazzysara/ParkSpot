package com.parkspot.controller;

import com.parkspot.entity.ParkingSlot;
import com.parkspot.service.ParkingSlotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing parking slots.
 */
@RestController
@RequestMapping("/api/slots")
@Tag(name = "Slot Management", description = "CRUD operations for parking slots")
@CrossOrigin(origins = "*")
public class ParkingSlotController {

    private final ParkingSlotService slotService;

    public ParkingSlotController(ParkingSlotService slotService) {
        this.slotService = slotService;
    }

    @PostMapping
    @Operation(summary = "Create a new parking slot")
    public ResponseEntity<ParkingSlot> createSlot(@Valid @RequestBody ParkingSlot slot) {
        ParkingSlot created = slotService.createSlot(slot);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "List all parking slots")
    public ResponseEntity<List<ParkingSlot>> getAllSlots() {
        return ResponseEntity.ok(slotService.getAllSlots());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a parking slot by ID")
    public ResponseEntity<ParkingSlot> getSlotById(@PathVariable Long id) {
        return ResponseEntity.ok(slotService.getSlotById(id));
    }

    @GetMapping("/number/{slotNumber}")
    @Operation(summary = "Get a parking slot by its slot number")
    public ResponseEntity<ParkingSlot> getSlotByNumber(@PathVariable Integer slotNumber) {
        return ResponseEntity.ok(slotService.getSlotByNumber(slotNumber));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a parking slot (must be unoccupied)")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        slotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
