package com.transport.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Passenger using the transit system.
 */
public class Passenger {
    private int passengerId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private List<Booking> bookings;

    public Passenger(int passengerId, String name, String email, String phone) {
        this(passengerId, name, email, phone, "Not Specified", "Unknown");
    }

    public Passenger(int passengerId, String name, String email, String phone, String gender, String address) {
        this.passengerId = passengerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.bookings = new ArrayList<>();
    }

    public List<Schedule> searchSchedules(List<Schedule> allSchedules, String source, String destination) {
        List<Schedule> matching = new ArrayList<>();
        for (Schedule s : allSchedules) {
            if (s.getRoute().getSource().equalsIgnoreCase(source) &&
                s.getRoute().getDestination().equalsIgnoreCase(destination)) {
                matching.add(s);
            }
        }
        return matching;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public boolean cancelTicket(int bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId() == bookingId) {
                b.cancelBooking();
                return true;
            }
        }
        return false;
    }

    public List<Booking> viewBookings() {
        return new ArrayList<>(this.bookings);
    }

    public int getPassengerId() {
        return passengerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-15s | Phone: %-12s | Email: %s", passengerId, name, phone, email);
    }
}
