package com.transport.model;

/**
 * Represents a Bus entity in the public transport fleet.
 * Aligned with Class Diagram and Syllabus Unit 1 & 2.
 */
public class Bus {
    private String busId;
    private String busNumber;
    private int capacity;
    private String type;
    private String status;

    public Bus(String busId, String busNumber, int capacity, String type) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.type = type;
        this.status = "Active";
    }

    public Bus(String busId, String busNumber, int capacity, String type, String status) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.type = type;
        this.status = status;
    }

    public void updateDetails(int capacity, String type, String status) {
        this.capacity = capacity;
        this.type = type;
        this.status = status;
    }

    public boolean checkAvailability(int currentBookedSeats) {
        return currentBookedSeats < this.capacity;
    }

    public String getBusId() {
        return busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Type: %-16s | Cap: %2d | Status: %s", 
                busId, busNumber, type, capacity, status);
    }
}
