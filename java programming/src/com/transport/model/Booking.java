package com.transport.model;

import java.time.LocalDate;

/**
 * Represents an individual booking transaction.
 */
public class Booking {
    private int bookingId;
    private int passengerId;
    private Schedule schedule;
    private String seatNo;
    private double amount;
    private String status;
    private LocalDate bookingDate;

    public Booking(int bookingId, int passengerId, Schedule schedule, String seatNo, double amount) {
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.schedule = schedule;
        this.seatNo = seatNo;
        this.amount = amount;
        this.status = "Confirmed";
        this.bookingDate = LocalDate.now();
    }

    public void confirmBooking() {
        this.status = "Confirmed";
    }

    public void cancelBooking() {
        this.status = "Cancelled";
        this.schedule.releaseSeat(this.seatNo);
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    @Override
    public String toString() {
        return String.format("Booking #%d | Passenger ID: %d | Seat: %s | Amount: ₹%.2f | Status: %s | Route: %s -> %s",
                bookingId, passengerId, seatNo, amount, status, 
                schedule.getRoute().getSource(), schedule.getRoute().getDestination());
    }
}
