package com.parkspot.controller;

import com.parkspot.dto.DashboardResponse;
import com.parkspot.dto.SlotStatusResponse;
import com.parkspot.dto.VehicleEntryRequest;
import com.parkspot.dto.VehicleEntryResponse;
import com.parkspot.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for all parking operations —
 * vehicle entry/exit, slot status, daily reports, dashboard.
 */
@RestController
@RequestMapping("/api/parking")
@Tag(name = "Parking Operations", description = "Core parking management — entry, exit, status, reports")
@CrossOrigin(origins = "*")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    // ─── 1. Log Vehicle Entry ───

    @PostMapping("/entry")
    @Operation(summary = "Log a visitor vehicle entry",
            description = "Assigns a visitor vehicle to a numbered parking slot. "
                    + "Fails if the slot is already occupied.")
    public ResponseEntity<VehicleEntryResponse> logEntry(@Valid @RequestBody VehicleEntryRequest request) {
        VehicleEntryResponse response = parkingService.logEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── 2. Mark Vehicle Exit ───

    @PutMapping("/exit/{vehicleId}")
    @Operation(summary = "Mark a visitor vehicle exit",
            description = "Records exit time and frees the parking slot for reuse. "
                    + "Fails if entry time was never recorded or vehicle already exited.")
    public ResponseEntity<VehicleEntryResponse> markExit(@PathVariable Long vehicleId) {
        VehicleEntryResponse response = parkingService.markExit(vehicleId);
        return ResponseEntity.ok(response);
    }

    // ─── 3. View Slot Status ───

    @GetMapping("/slots")
    @Operation(summary = "View all parking slots", description = "Returns all slots with their current occupancy status.")
    public ResponseEntity<List<SlotStatusResponse>> getAllSlots() {
        return ResponseEntity.ok(parkingService.getAllSlotStatuses());
    }

    @GetMapping("/slots/occupied")
    @Operation(summary = "View currently occupied slots")
    public ResponseEntity<List<SlotStatusResponse>> getOccupiedSlots() {
        return ResponseEntity.ok(parkingService.getOccupiedSlots());
    }

    @GetMapping("/slots/available")
    @Operation(summary = "View available (free) slots")
    public ResponseEntity<List<SlotStatusResponse>> getAvailableSlots() {
        return ResponseEntity.ok(parkingService.getAvailableSlots());
    }

    // ─── 4. Get a Single Entry ───

    @GetMapping("/entry/{id}")
    @Operation(summary = "Get a visitor entry by ID")
    public ResponseEntity<VehicleEntryResponse> getEntry(@PathVariable Long id) {
        return ResponseEntity.ok(parkingService.getEntryById(id));
    }

    // ─── 5. Daily Visitor Log Report ───

    @GetMapping("/report/daily")
    @Operation(summary = "Generate daily visitor log report",
            description = "Returns all visitor entries for the given date. Defaults to today if no date provided.")
    public ResponseEntity<List<VehicleEntryResponse>> getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(parkingService.getDailyReport(date));
    }

    // ─── 6. Dashboard ───

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard summary", description = "Aggregate counts: slots, occupancy, visitors.")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(parkingService.getDashboard());
    }

    // ─── 7. Search / Filter ───

    @GetMapping("/search")
    @Operation(summary = "Search visitor records by vehicle number",
            description = "Partial, case-insensitive match on vehicle number.")
    public ResponseEntity<List<VehicleEntryResponse>> searchByVehicle(@RequestParam String vehicleNumber) {
        return ResponseEntity.ok(parkingService.searchByVehicleNumber(vehicleNumber));
    }

    @GetMapping("/by-flat/{flatNumber}")
    @Operation(summary = "Find all visitor records for a flat")
    public ResponseEntity<List<VehicleEntryResponse>> findByFlat(@PathVariable String flatNumber) {
        return ResponseEntity.ok(parkingService.findByFlat(flatNumber));
    }
}
