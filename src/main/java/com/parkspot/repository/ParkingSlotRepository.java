package com.parkspot.repository;

import com.parkspot.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    Optional<ParkingSlot> findBySlotNumber(Integer slotNumber);

    List<ParkingSlot> findByOccupiedTrue();

    List<ParkingSlot> findByOccupiedFalse();

    long countByOccupiedTrue();

    long countByOccupiedFalse();

    List<ParkingSlot> findAllByOrderBySlotNumberAsc();
}
