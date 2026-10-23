package com.parkspot.dto;

/**
 * Dashboard summary showing quick aggregate counts.
 */
public class DashboardResponse {

    private long totalSlots;
    private long occupiedSlots;
    private long availableSlots;
    private long totalVisitorsToday;
    private long totalVisitorsAllTime;
    private long totalFlats;

    // ─── Constructors ───

    public DashboardResponse() {
    }

    public DashboardResponse(long totalSlots, long occupiedSlots, long availableSlots,
                             long totalVisitorsToday, long totalVisitorsAllTime, long totalFlats) {
        this.totalSlots = totalSlots;
        this.occupiedSlots = occupiedSlots;
        this.availableSlots = availableSlots;
        this.totalVisitorsToday = totalVisitorsToday;
        this.totalVisitorsAllTime = totalVisitorsAllTime;
        this.totalFlats = totalFlats;
    }

    // ─── Getters and Setters ───

    public long getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(long totalSlots) {
        this.totalSlots = totalSlots;
    }

    public long getOccupiedSlots() {
        return occupiedSlots;
    }

    public void setOccupiedSlots(long occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
    }

    public long getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(long availableSlots) {
        this.availableSlots = availableSlots;
    }

    public long getTotalVisitorsToday() {
        return totalVisitorsToday;
    }

    public void setTotalVisitorsToday(long totalVisitorsToday) {
        this.totalVisitorsToday = totalVisitorsToday;
    }

    public long getTotalVisitorsAllTime() {
        return totalVisitorsAllTime;
    }

    public void setTotalVisitorsAllTime(long totalVisitorsAllTime) {
        this.totalVisitorsAllTime = totalVisitorsAllTime;
    }

    public long getTotalFlats() {
        return totalFlats;
    }

    public void setTotalFlats(long totalFlats) {
        this.totalFlats = totalFlats;
    }
}
