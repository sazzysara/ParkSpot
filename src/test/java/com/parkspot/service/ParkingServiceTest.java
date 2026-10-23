package com.parkspot.service;

import com.parkspot.dto.VehicleEntryRequest;
import com.parkspot.dto.VehicleEntryResponse;
import com.parkspot.entity.Flat;
import com.parkspot.entity.ParkingSlot;
import com.parkspot.exception.InvalidOperationException;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.exception.SlotAlreadyOccupiedException;
import com.parkspot.repository.FlatRepository;
import com.parkspot.repository.ParkingSlotRepository;
import com.parkspot.repository.VisitorVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingServiceTest {

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private ParkingSlotRepository slotRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private VisitorVehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        // Sample flats and slots are already seeded by DataSeeder
    }

    @Test
    @DisplayName("Should successfully log vehicle entry into an available slot")
    void testLogEntry_Success() {
        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setVehicleNumber("KA-01-AB-1234");
        request.setVisitorName("Rahul Verma");
        request.setFlatNumber("A-101");
        request.setSlotNumber(1);

        VehicleEntryResponse response = parkingService.logEntry(request);

        assertNotNull(response.getId());
        assertEquals("KA-01-AB-1234", response.getVehicleNumber());
        assertEquals("Rahul Verma", response.getVisitorName());
        assertEquals("A-101", response.getFlatNumber());
        assertEquals(1, response.getSlotNumber());
        assertNotNull(response.getEntryTime());
        assertNull(response.getExitTime());
        assertTrue(response.isCurrentlyParked());

        // Verify slot is now occupied
        ParkingSlot slot = slotRepository.findBySlotNumber(1).orElseThrow();
        assertTrue(slot.isOccupied());
    }

    @Test
    @DisplayName("BUSINESS RULE #1: Reject assigning an already-occupied slot")
    void testLogEntry_SlotAlreadyOccupied_ThrowsException() {
        // 1. First vehicle enters slot 2
        VehicleEntryRequest first = new VehicleEntryRequest();
        first.setVehicleNumber("DL-03-XY-9999");
        first.setVisitorName("First Visitor");
        first.setFlatNumber("A-101");
        first.setSlotNumber(2);
        parkingService.logEntry(first);

        // 2. Second vehicle tries to take the same slot 2
        VehicleEntryRequest second = new VehicleEntryRequest();
        second.setVehicleNumber("MH-12-CD-5678");
        second.setVisitorName("Second Visitor");
        second.setFlatNumber("B-201");
        second.setSlotNumber(2);

        // Expect SlotAlreadyOccupiedException
        SlotAlreadyOccupiedException ex = assertThrows(SlotAlreadyOccupiedException.class, () -> {
            parkingService.logEntry(second);
        });

        assertTrue(ex.getMessage().contains("already occupied"));
        assertTrue(ex.getMessage().contains("DL-03-XY-9999"));
    }

    @Test
    @DisplayName("BUSINESS RULE #2: Cannot log exit if vehicle has already exited")
    void testMarkExit_AlreadyExited_ThrowsException() {
        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setVehicleNumber("HR-26-QQ-1111");
        request.setVisitorName("Test Driver");
        request.setFlatNumber("A-102");
        request.setSlotNumber(3);

        VehicleEntryResponse entryResponse = parkingService.logEntry(request);

        // First exit: succeeds and frees slot
        VehicleEntryResponse exitResponse = parkingService.markExit(entryResponse.getId());
        assertNotNull(exitResponse.getExitTime());
        assertFalse(exitResponse.isCurrentlyParked());

        ParkingSlot slot = slotRepository.findBySlotNumber(3).orElseThrow();
        assertFalse(slot.isOccupied());

        // Second exit attempt on same record: must fail
        InvalidOperationException ex = assertThrows(InvalidOperationException.class, () -> {
            parkingService.markExit(entryResponse.getId());
        });

        assertTrue(ex.getMessage().contains("already exited"));
    }

    @Test
    @DisplayName("Slot should be reusable after vehicle exits")
    void testSlotReuseAfterExit() {
        // Vehicle 1 parks in slot 4
        VehicleEntryRequest req1 = new VehicleEntryRequest();
        req1.setVehicleNumber("UP-16-AA-0001");
        req1.setVisitorName("Visitor One");
        req1.setFlatNumber("B-202");
        req1.setSlotNumber(4);
        VehicleEntryResponse entry1 = parkingService.logEntry(req1);

        // Vehicle 1 exits
        parkingService.markExit(entry1.getId());

        // Vehicle 2 now takes slot 4 -> should succeed!
        VehicleEntryRequest req2 = new VehicleEntryRequest();
        req2.setVehicleNumber("UP-16-BB-0002");
        req2.setVisitorName("Visitor Two");
        req2.setFlatNumber("C-301");
        req2.setSlotNumber(4);

        VehicleEntryResponse entry2 = parkingService.logEntry(req2);
        assertNotNull(entry2.getId());
        assertEquals("UP-16-BB-0002", entry2.getVehicleNumber());
        assertEquals(4, entry2.getSlotNumber());
    }

    @Test
    @DisplayName("Reject entry for non-existent flat")
    void testLogEntry_NonExistentFlat() {
        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setVehicleNumber("DL-01-ZZ-9999");
        request.setFlatNumber("Z-999");
        request.setSlotNumber(5);

        assertThrows(ResourceNotFoundException.class, () -> {
            parkingService.logEntry(request);
        });
    }

    @Test
    @DisplayName("Reject entry for non-existent slot")
    void testLogEntry_NonExistentSlot() {
        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setVehicleNumber("DL-01-ZZ-9999");
        request.setFlatNumber("A-101");
        request.setSlotNumber(9999);

        assertThrows(ResourceNotFoundException.class, () -> {
            parkingService.logEntry(request);
        });
    }

    @Test
    @DisplayName("Daily report should include today's entries")
    void testDailyReport() {
        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setVehicleNumber("TS-09-ER-5555");
        request.setVisitorName("Daily Visitor");
        request.setFlatNumber("B-203");
        request.setSlotNumber(6);

        parkingService.logEntry(request);

        List<VehicleEntryResponse> report = parkingService.getDailyReport(LocalDate.now());
        assertFalse(report.isEmpty());
        assertTrue(report.stream().anyMatch(v -> v.getVehicleNumber().equals("TS-09-ER-5555")));
    }
}
