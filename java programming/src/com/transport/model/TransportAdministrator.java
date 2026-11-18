package com.transport.model;

/**
 * Represents a Transport Administrator managing fleet, routes, and schedules.
 */
public class TransportAdministrator {
    private int adminId;
    private String name;
    private String email;
    private String role;

    public TransportAdministrator(int adminId, String name, String email, String role) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void manageBus(Bus bus, int newCapacity, String newType, String newStatus) {
        bus.updateDetails(newCapacity, newType, newStatus);
    }

    public void manageRoute(Route route, String newSource, String newDest, double newDist) {
        route.updateRoute(newSource, newDest, newDist);
    }

    public void manageSchedule(Schedule schedule, java.time.LocalDate newDate, 
                               java.time.LocalTime newTime, double newFare) {
        schedule.updateSchedule(newDate, newTime, newFare);
    }

    public String viewReports() {
        return "Administrator System Audit: Fleet Status Active, Schedules Operating Normally.";
    }

    public int getAdminId() {
        return adminId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return String.format("[Admin #%d] %s (%s)", adminId, name, role);
    }
}
