-- =====================================================================
-- ParkSpot Database Queries & Reference SQL
-- Run these in IntelliJ Database Console, MySQL Workbench, or H2 Console
-- =====================================================================

-- 1. View all flats
SELECT * FROM flats ORDER BY flat_number ASC;

-- 2. View all parking slots with occupancy status
SELECT slot_number, is_occupied FROM parking_slots ORDER BY slot_number ASC;

-- 3. View currently occupied slots with visitor and flat details
SELECT 
    ps.slot_number,
    vv.vehicle_number,
    vv.visitor_name,
    f.flat_number,
    f.owner_name AS resident_name,
    vv.entry_time
FROM parking_slots ps
JOIN visitor_vehicles vv ON vv.slot_id = ps.id
JOIN flats f ON vv.flat_id = f.id
WHERE ps.is_occupied = TRUE 
  AND vv.exit_time IS NULL
ORDER BY ps.slot_number ASC;

-- 4. View all available (empty) slots
SELECT slot_number 
FROM parking_slots 
WHERE is_occupied = FALSE 
ORDER BY slot_number ASC;

-- 5. Daily visitor log report for today
SELECT 
    vv.id,
    vv.vehicle_number,
    vv.visitor_name,
    f.flat_number,
    ps.slot_number,
    vv.entry_time,
    vv.exit_time,
    CASE 
        WHEN vv.exit_time IS NULL THEN 'PARKED' 
        ELSE 'EXITED' 
    END AS status
FROM visitor_vehicles vv
JOIN flats f ON vv.flat_id = f.id
JOIN parking_slots ps ON vv.slot_id = ps.id
WHERE CAST(vv.entry_time AS DATE) = CURRENT_DATE
ORDER BY vv.entry_time DESC;

-- 6. Total occupancy summary
SELECT 
    (SELECT COUNT(*) FROM parking_slots) AS total_slots,
    (SELECT COUNT(*) FROM parking_slots WHERE is_occupied = TRUE) AS occupied_slots,
    (SELECT COUNT(*) FROM parking_slots WHERE is_occupied = FALSE) AS available_slots,
    (SELECT COUNT(*) FROM visitor_vehicles WHERE CAST(entry_time AS DATE) = CURRENT_DATE) AS visitors_today;
