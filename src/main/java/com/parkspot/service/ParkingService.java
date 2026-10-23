package com.parkspot.service;

import com.parkspot.dto.DashboardResponse;
import com.parkspot.dto.SlotStatusResponse;
import com.parkspot.dto.VehicleEntryRequest;
import com.parkspot.dto.VehicleEntryResponse;
import com.parkspot.entity.Flat;
import com.parkspot.entity.ParkingSlot;
import com.parkspot.entity.VisitorVehicle;
import com.parkspot.exception.InvalidOperationException;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.exception.SlotAlreadyOccupiedException;
import com.parkspot.repository.FlatRepository;
import com.parkspot.repository.ParkingSlotRepository;
import com.parkspot.repository.VisitorVehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core service holding all parking business logic.
 *
 * Business Rules Enforced:
 * 1. A slot cannot be assigned to a new vehicle until the previous one has exited.
 * 2. Entry time must always be recorded before an exit time can be logged.
 */
@Service
public class ParkingService {

    private final VisitorVehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final FlatRepository flatRepository;

    public ParkingService(VisitorVehicleRepository vehicleRepository,
                          ParkingSlotRepository slotRepository,
                          FlatRepository flatRepository) {
        this.vehicleRepository = vehicleRepository;
        this.slotRepository = slotRepository;
        this.flatRepository = flatRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. LOG VISITOR VEHICLE ENTRY
    // ─────────────────────────────────────────────────────────────

    /**
     * Logs a new visitor vehicle entry.
     *
     * Enforced rules:
     * - The flat must exist.
     * - The slot must exist.
     * - The slot must NOT be currently occupied (BUSINESS RULE #1).
     */
    @Transactional
    public VehicleEntryResponse logEntry(VehicleEntryRequest request) {
        // 1. Validate flat exists
        Flat flat = flatRepository.findByFlatNumber(request.getFlatNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flat '" + request.getFlatNumber() + "' not found. Please register the flat first."));

        // 2. Validate slot exists
        ParkingSlot slot = slotRepository.findBySlotNumber(request.getSlotNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking slot #" + request.getSlotNumber() + " does not exist."));

        // 3. BUSINESS RULE: Slot must not be occupied
        if (slot.isOccupied()) {
            // Find who is currently in the slot for a helpful error message
            vehicleRepository.findByParkingSlotAndExitTimeIsNull(slot)
                    .ifPresent(current -> {
                        throw new SlotAlreadyOccupiedException(
                                "Slot #" + slot.getSlotNumber() + " is already occupied by vehicle '"
                                        + current.getVehicleNumber() + "' (entered at "
                                        + current.getEntryTime() + "). "
                                        + "The vehicle must exit before a new one can be assigned.");
                    });
            // Fallback if no record found (shouldn't happen with proper data)
            throw new SlotAlreadyOccupiedException(
                    "Slot #" + slot.getSlotNumber() + " is currently occupied. "
                            + "It must be freed before a new vehicle can park.");
        }

        // 4. Create the visitor vehicle record
        VisitorVehicle vehicle = new VisitorVehicle();
        vehicle.setVehicleNumber(request.getVehicleNumber().toUpperCase().trim());
        vehicle.setVisitorName(request.getVisitorName());
        vehicle.setEntryTime(LocalDateTime.now());
        vehicle.setFlat(flat);
        vehicle.setParkingSlot(slot);

        // 5. Mark slot as occupied
        slot.setOccupied(true);
        slotRepository.save(slot);

        // 6. Save and return
        VisitorVehicle saved = vehicleRepository.save(vehicle);
        return toResponse(saved);
    }

    // ─────────────────────────────────────────────────────────────
    // 2. MARK EXIT — FREE THE SLOT
    // ─────────────────────────────────────────────────────────────

    /**
     * Marks a visitor vehicle's exit time and frees the parking slot.
     *
     * Enforced rules:
     * - The vehicle record must exist.
     * - The vehicle must have an entry time (BUSINESS RULE #2).
     * - The vehicle must not have already exited.
     */
    @Transactional
    public VehicleEntryResponse markExit(Long vehicleId) {
        // 1. Find the vehicle record
        VisitorVehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visitor vehicle record with ID " + vehicleId + " not found."));

        // 2. BUSINESS RULE: Entry time must exist before exit can be logged
        if (vehicle.getEntryTime() == null) {
            throw new InvalidOperationException(
                    "Cannot log exit: Vehicle '" + vehicle.getVehicleNumber()
                            + "' has no entry time recorded. Entry must be logged before exit.");
        }

        // 3. Check if already exited
        if (vehicle.getExitTime() != null) {
            throw new InvalidOperationException(
                    "Vehicle '" + vehicle.getVehicleNumber() + "' has already exited at "
                            + vehicle.getExitTime() + ". Cannot log exit again.");
        }

        // 4. Record exit time
        vehicle.setExitTime(LocalDateTime.now());
        vehicleRepository.save(vehicle);

        // 5. Free the parking slot
        ParkingSlot slot = vehicle.getParkingSlot();
        slot.setOccupied(false);
        slotRepository.save(slot);

        return toResponse(vehicle);
    }

    // ─────────────────────────────────────────────────────────────
    // 3. VIEW CURRENTLY OCCUPIED SLOTS
    // ─────────────────────────────────────────────────────────────

    /**
     * Returns the status of all parking slots (occupied and available).
     */
    public List<SlotStatusResponse> getAllSlotStatuses() {
        List<ParkingSlot> slots = slotRepository.findAllByOrderBySlotNumberAsc();
        return slots.stream().map(this::toSlotStatus).collect(Collectors.toList());
    }

    /**
     * Returns only the currently occupied slots.
     */
    public List<SlotStatusResponse> getOccupiedSlots() {
        List<ParkingSlot> occupied = slotRepository.findByOccupiedTrue();
        return occupied.stream().map(this::toSlotStatus).collect(Collectors.toList());
    }

    /**
     * Returns only the currently available (free) slots.
     */
    public List<SlotStatusResponse> getAvailableSlots() {
        List<ParkingSlot> available = slotRepository.findByOccupiedFalse();
        return available.stream().map(this::toSlotStatus).collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────
    // 4. DAILY VISITOR LOG REPORT
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a daily visitor log for the given date.
     * If no date is provided, defaults to today.
     */
    public List<VehicleEntryResponse> getDailyReport(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<VisitorVehicle> vehicles = vehicleRepository.findByEntryTimeBetween(start, end);
        return vehicles.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────
    // 5. DASHBOARD
    // ─────────────────────────────────────────────────────────────

    /**
     * Returns aggregate dashboard statistics.
     */
    public DashboardResponse getDashboard() {
        long totalSlots = slotRepository.count();
        long occupiedSlots = slotRepository.countByOccupiedTrue();
        long availableSlots = slotRepository.countByOccupiedFalse();
        long totalAllTime = vehicleRepository.count();
        long totalFlats = flatRepository.count();

        // Today's visitors
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);
        long totalToday = vehicleRepository.countByEntryTimeBetween(todayStart, todayEnd);

        return new DashboardResponse(totalSlots, occupiedSlots, availableSlots,
                totalToday, totalAllTime, totalFlats);
    }

    // ─────────────────────────────────────────────────────────────
    // 6. SEARCH / FILTER
    // ─────────────────────────────────────────────────────────────

    /**
     * Search visitor records by vehicle number (partial match).
     */
    public List<VehicleEntryResponse> searchByVehicleNumber(String query) {
        return vehicleRepository.searchByVehicleNumber(query)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Find all visitor records for a specific flat.
     */
    public List<VehicleEntryResponse> findByFlat(String flatNumber) {
        return vehicleRepository.findByFlatNumber(flatNumber)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Get a single vehicle entry by ID.
     */
    public VehicleEntryResponse getEntryById(Long id) {
        VisitorVehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Visitor vehicle record with ID " + id + " not found."));
        return toResponse(vehicle);
    }

    // ─────────────────────────────────────────────────────────────
    // MAPPERS
    // ─────────────────────────────────────────────────────────────

    private VehicleEntryResponse toResponse(VisitorVehicle vehicle) {
        return new VehicleEntryResponse(
                vehicle.getId(),
                vehicle.getVehicleNumber(),
                vehicle.getVisitorName(),
                vehicle.getFlat().getFlatNumber(),
                vehicle.getParkingSlot().getSlotNumber(),
                vehicle.getEntryTime(),
                vehicle.getExitTime(),
                vehicle.isCurrentlyParked()
        );
    }

    private SlotStatusResponse toSlotStatus(ParkingSlot slot) {
        SlotStatusResponse response = new SlotStatusResponse();
        response.setId(slot.getId());
        response.setSlotNumber(slot.getSlotNumber());
        response.setOccupied(slot.isOccupied());

        if (slot.isOccupied()) {
            vehicleRepository.findByParkingSlotAndExitTimeIsNull(slot)
                    .ifPresent(vehicle -> {
                        response.setCurrentVehicleNumber(vehicle.getVehicleNumber());
                        response.setCurrentVisitorName(vehicle.getVisitorName());
                        response.setVisitingFlat(vehicle.getFlat().getFlatNumber());
                    });
        }

        return response;
    }
}
