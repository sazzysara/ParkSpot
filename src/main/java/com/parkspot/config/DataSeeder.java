package com.parkspot.config;

import com.parkspot.entity.Flat;
import com.parkspot.entity.ParkingSlot;
import com.parkspot.repository.FlatRepository;
import com.parkspot.repository.ParkingSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the database on application startup with:
 * - Numbered parking slots (configurable via parkspot.total-slots)
 * - Sample flats for testing
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ParkingSlotRepository slotRepository;
    private final FlatRepository flatRepository;

    @Value("${parkspot.total-slots:20}")
    private int totalSlots;

    public DataSeeder(ParkingSlotRepository slotRepository, FlatRepository flatRepository) {
        this.slotRepository = slotRepository;
        this.flatRepository = flatRepository;
    }

    @Override
    public void run(String... args) {
        seedParkingSlots();
        seedSampleFlats();
    }

    private void seedParkingSlots() {
        if (slotRepository.count() == 0) {
            log.info("Seeding {} parking slots...", totalSlots);
            for (int i = 1; i <= totalSlots; i++) {
                slotRepository.save(new ParkingSlot(i));
            }
            log.info("✓ {} parking slots created.", totalSlots);
        } else {
            log.info("Parking slots already exist — skipping seed.");
        }
    }

    private void seedSampleFlats() {
        if (flatRepository.count() == 0) {
            log.info("Seeding sample flats...");
            flatRepository.save(new Flat("A-101", "Rajesh Kumar", "9876543210"));
            flatRepository.save(new Flat("A-102", "Priya Sharma", "9876543211"));
            flatRepository.save(new Flat("A-103", "Amit Patel", "9876543212"));
            flatRepository.save(new Flat("B-201", "Sneha Reddy", "9876543213"));
            flatRepository.save(new Flat("B-202", "Vikram Singh", "9876543214"));
            flatRepository.save(new Flat("B-203", "Ananya Gupta", "9876543215"));
            flatRepository.save(new Flat("C-301", "Rohan Mehta", "9876543216"));
            flatRepository.save(new Flat("C-302", "Kavita Joshi", "9876543217"));
            log.info("✓ 8 sample flats created.");
        } else {
            log.info("Flats already exist — skipping seed.");
        }
    }
}
