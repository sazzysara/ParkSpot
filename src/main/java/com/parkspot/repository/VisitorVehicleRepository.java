package com.parkspot.repository;

import com.parkspot.entity.ParkingSlot;
import com.parkspot.entity.VisitorVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitorVehicleRepository extends JpaRepository<VisitorVehicle, Long> {

    /**
     * Find the currently parked vehicle in a given slot (exit time is null).
     */
    Optional<VisitorVehicle> findByParkingSlotAndExitTimeIsNull(ParkingSlot parkingSlot);

    /**
     * Find all vehicles currently parked (no exit time).
     */
    List<VisitorVehicle> findByExitTimeIsNull();

    /**
     * Daily log: find all vehicles that entered on a specific date range.
     */
    @Query("SELECT v FROM VisitorVehicle v WHERE v.entryTime >= :start AND v.entryTime < :end ORDER BY v.entryTime ASC")
    List<VisitorVehicle> findByEntryTimeBetween(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

    /**
     * Count visitors that entered today.
     */
    @Query("SELECT COUNT(v) FROM VisitorVehicle v WHERE v.entryTime >= :start AND v.entryTime < :end")
    long countByEntryTimeBetween(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    /**
     * Search by vehicle number (partial match, case-insensitive).
     */
    @Query("SELECT v FROM VisitorVehicle v WHERE LOWER(v.vehicleNumber) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY v.entryTime DESC")
    List<VisitorVehicle> searchByVehicleNumber(@Param("query") String query);

    /**
     * Search by flat number.
     */
    @Query("SELECT v FROM VisitorVehicle v WHERE v.flat.flatNumber = :flatNumber ORDER BY v.entryTime DESC")
    List<VisitorVehicle> findByFlatNumber(@Param("flatNumber") String flatNumber);
}
