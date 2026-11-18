package com.transport.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a scheduled bus journey on a specific route, date, and time.
 */
public class Schedule {
    private int scheduleId;
    private Bus bus;
    private Route route;
    private LocalDate travelDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private double fare;
    private Set<String> bookedSeats;

    public Schedule(int scheduleId, Bus bus, Route route, LocalDate travelDate, 
                    LocalTime departureTime, LocalTime arrivalTime, double fare) {
        this.scheduleId = scheduleId;
        this.bus = bus;
        this.route = route;
        this.travelDate = travelDate;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.bookedSeats = new HashSet<>();
    }

    public void updateSchedule(LocalDate travelDate, LocalTime departureTime, double fare) {
        this.travelDate = travelDate;
        this.departureTime = departureTime;
        this.fare = fare;
    }

    public synchronized boolean checkSeats(String seatNo) {
        return !bookedSeats.contains(seatNo) && bookedSeats.size() < bus.getCapacity();
    }

    public synchronized boolean bookSeat(String seatNo) {
        if (checkSeats(seatNo)) {
            bookedSeats.add(seatNo);
            return true;
        }
        return false;
    }

    public synchronized boolean releaseSeat(String seatNo) {
        return bookedSeats.remove(seatNo);
    }

    public int getAvailableSeatsCount() {
        return bus.getCapacity() - bookedSeats.size();
    }

    public int getBookedSeatsCount() {
        return bookedSeats.size();
    }

    public double getOccupancyRate() {
        return (bookedSeats.size() * 100.0) / bus.getCapacity();
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public Bus getBus() {
        return bus;
    }

    public Route getRoute() {
        return route;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public double getFare() {
        return fare;
    }

    public Set<String> getBookedSeats() {
        return new HashSet<>(bookedSeats);
    }

    @Override
    public String toString() {
        return String.format("[%d] %-10s -> %-10s | Bus: %s (%s) | Date: %s | Dep: %s | Fare: ₹%.2f | Avail: %d/%d",
                scheduleId, route.getSource(), route.getDestination(), bus.getBusNumber(), bus.getType(),
                travelDate, departureTime, fare, getAvailableSeatsCount(), bus.getCapacity());
    }
}
