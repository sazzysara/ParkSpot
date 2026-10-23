package com.parkspot.service;

import com.parkspot.entity.ParkingSlot;
import com.parkspot.exception.InvalidOperationException;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing parking slots.
 */
@Service
public class ParkingSlotService {

    private final ParkingSlotRepository slotRepository;

    public ParkingSlotService(ParkingSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Transactional
    public ParkingSlot createSlot(ParkingSlot slot) {
        if (slotRepository.findBySlotNumber(slot.getSlotNumber()).isPresent()) {
            throw new InvalidOperationException(
                    "Parking slot #" + slot.getSlotNumber() + " already exists.");
        }
        slot.setOccupied(false);
        return slotRepository.save(slot);
    }

    public List<ParkingSlot> getAllSlots() {
        return slotRepository.findAllByOrderBySlotNumberAsc();
    }

    public ParkingSlot getSlotByNumber(Integer slotNumber) {
        return slotRepository.findBySlotNumber(slotNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking slot #" + slotNumber + " not found."));
    }

    public ParkingSlot getSlotById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parking slot with ID " + id + " not found."));
    }

    @Transactional
    public void deleteSlot(Long id) {
        ParkingSlot slot = getSlotById(id);
        if (slot.isOccupied()) {
            throw new InvalidOperationException(
                    "Cannot delete slot #" + slot.getSlotNumber()
                            + " — it is currently occupied. Free the slot first.");
        }
        slotRepository.delete(slot);
    }
}
