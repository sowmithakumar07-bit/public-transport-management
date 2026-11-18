package com.transport.service;

import com.transport.exception.InvalidBookingException;
import com.transport.exception.SeatUnavailableException;
import com.transport.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core Business Logic Service for Transport Management.
 * Implements Generics, Collections, Lambda Expressions, and Stream API (Unit 5).
 */
public class TransportService {
    private Map<String, Bus> buses;
    private Map<Integer, Route> routes;
    private Map<Integer, Schedule> schedules;
    private Map<Integer, Passenger> passengers;
    private Map<Integer, Booking> bookings;
    private Map<Integer, TicketBooking> tickets;
    private int nextBookingId = 501;
    private int nextTicketId = 601;

    public TransportService() {
        this.buses = new LinkedHashMap<>();
        this.routes = new LinkedHashMap<>();
        this.schedules = new LinkedHashMap<>();
        this.passengers = new LinkedHashMap<>();
        this.bookings = new LinkedHashMap<>();
        this.tickets = new LinkedHashMap<>();
        seedInitialData();
    }

    private void seedInitialData() {
        // 1. Buses
        buses.put("BUS-01", new Bus("BUS-01", "KA-01-F-1001", 30, "AC Sleeper", "Active"));
        buses.put("BUS-02", new Bus("BUS-02", "TN-09-E-2002", 45, "Non-AC Seater", "Active"));
        buses.put("BUS-03", new Bus("BUS-03", "TS-07-H-3003", 36, "Volvo Multi-Axle", "Active"));
        buses.put("BUS-04", new Bus("BUS-04", "KL-01-C-4004", 32, "Electric Express", "Active"));
        buses.put("BUS-05", new Bus("BUS-05", "KA-05-D-5005", 40, "Volvo Multi-Axle", "Active"));

        // 2. Routes
        routes.put(201, new Route(201, "Bangalore", "Chennai", 350.0));
        routes.put(202, new Route(202, "Chennai", "Bangalore", 350.0));
        routes.put(203, new Route(203, "Bangalore", "Hyderabad", 570.0));
        routes.put(204, new Route(204, "Hyderabad", "Bangalore", 570.0));
        routes.put(205, new Route(205, "Chennai", "Coimbatore", 510.0));

        // 3. Schedules
        schedules.put(301, new Schedule(301, buses.get("BUS-01"), routes.get(201), LocalDate.of(2026, 11, 20), LocalTime.of(21, 0), LocalTime.of(5, 30), 850.00));
        schedules.put(302, new Schedule(302, buses.get("BUS-02"), routes.get(202), LocalDate.of(2026, 11, 20), LocalTime.of(7, 30), LocalTime.of(14, 30), 450.00));
        schedules.put(303, new Schedule(303, buses.get("BUS-03"), routes.get(203), LocalDate.of(2026, 11, 21), LocalTime.of(20, 0), LocalTime.of(6, 0), 1100.00));
        schedules.put(304, new Schedule(304, buses.get("BUS-04"), routes.get(204), LocalDate.of(2026, 11, 21), LocalTime.of(21, 30), LocalTime.of(7, 0), 950.00));
        schedules.put(305, new Schedule(305, buses.get("BUS-05"), routes.get(205), LocalDate.of(2026, 11, 22), LocalTime.of(22, 0), LocalTime.of(6, 30), 1050.00));

        // 4. Passengers
        passengers.put(101, new Passenger(101, "Aarav Sharma", "aarav@example.com", "9876543210", "Male", "Bangalore"));
        passengers.put(102, new Passenger(102, "Diya Patel", "diya@example.com", "9876543211", "Female", "Chennai"));
        passengers.put(103, new Passenger(103, "Rohan Verma", "rohan@example.com", "9876543212", "Male", "Hyderabad"));
        passengers.put(104, new Passenger(104, "Sneha Reddy", "sneha@example.com", "9876543213", "Female", "Hyderabad"));

        // Pre-book a few seats
        try {
            bookTicketInternal(101, 301, "S1");
            bookTicketInternal(102, 301, "S2");
            bookTicketInternal(103, 303, "V01");
            bookTicketInternal(104, 303, "V02");
        } catch (Exception ignored) {}
    }

    private synchronized TicketBooking bookTicketInternal(int passengerId, int scheduleId, String seatNo) 
            throws InvalidBookingException, SeatUnavailableException {
        Passenger passenger = passengers.get(passengerId);
        if (passenger == null) {
            throw new InvalidBookingException("Passenger ID " + passengerId + " not found!");
        }
        Schedule schedule = schedules.get(scheduleId);
        if (schedule == null) {
            throw new InvalidBookingException("Schedule ID " + scheduleId + " does not exist!");
        }
        if (!schedule.bookSeat(seatNo)) {
            throw new SeatUnavailableException("Seat '" + seatNo + "' on Schedule " + scheduleId + " is unavailable or bus is full!");
        }

        int bookingId = nextBookingId++;
        Booking booking = new Booking(bookingId, passengerId, schedule, seatNo, schedule.getFare());
        passenger.addBooking(booking);
        bookings.put(bookingId, booking);

        int ticketId = nextTicketId++;
        String ticketNo = String.format("TCK-%04d-%03d", scheduleId, bookingId);
        String qr = String.format("QR-%s-%s-%s", ticketNo, schedule.getRoute().getSource(), seatNo);
        TicketBooking ticket = new TicketBooking(ticketId, booking, ticketNo, schedule.getFare(), qr);
        tickets.put(bookingId, ticket);

        return ticket;
    }

    public synchronized TicketBooking bookTicket(int passengerId, int scheduleId, String seatNo) 
            throws InvalidBookingException, SeatUnavailableException {
        return bookTicketInternal(passengerId, scheduleId, seatNo);
    }

    public synchronized boolean cancelTicket(int bookingId) throws InvalidBookingException {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new InvalidBookingException("Booking ID " + bookingId + " not found!");
        }
        if ("Cancelled".equalsIgnoreCase(booking.getStatus())) {
            throw new InvalidBookingException("Booking ID " + bookingId + " is already cancelled!");
        }
        booking.cancelBooking();
        return true;
    }

    public List<Schedule> searchSchedules(String source, String destination) {
        return schedules.values().stream()
                .filter(s -> s.getRoute().getSource().equalsIgnoreCase(source) &&
                             s.getRoute().getDestination().equalsIgnoreCase(destination))
                .sorted(Comparator.comparing(Schedule::getTravelDate).thenComparing(Schedule::getDepartureTime))
                .collect(Collectors.toList());
    }

    public List<Schedule> getAllSchedules() {
        return new ArrayList<>(schedules.values());
    }

    public List<Bus> getAllBuses() {
        return new ArrayList<>(buses.values());
    }

    public List<Route> getAllRoutes() {
        return new ArrayList<>(routes.values());
    }

    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers.values());
    }

    public Passenger getPassenger(int passengerId) {
        return passengers.get(passengerId);
    }

    public void registerPassenger(Passenger passenger) {
        passengers.put(passenger.getPassengerId(), passenger);
    }

    public void addBus(Bus bus) {
        buses.put(bus.getBusId(), bus);
    }

    public void addRoute(Route route) {
        routes.put(route.getRouteId(), route);
    }

    public void addSchedule(Schedule schedule) {
        schedules.put(schedule.getScheduleId(), schedule);
    }

    public TicketBooking getTicketByBookingId(int bookingId) {
        return tickets.get(bookingId);
    }

    // =========================================================================
    // STREAM API REPORTING (Unit 5)
    // =========================================================================

    public Map<String, Double> getRouteWiseRevenue() {
        return bookings.values().stream()
                .filter(b -> "Confirmed".equalsIgnoreCase(b.getStatus()))
                .collect(Collectors.groupingBy(
                        b -> b.getSchedule().getRoute().getSource() + " -> " + b.getSchedule().getRoute().getDestination(),
                        Collectors.summingDouble(Booking::getAmount)
                ));
    }

    public Map<String, Double> getBusOccupancyRates() {
        return schedules.values().stream()
                .collect(Collectors.toMap(
                        s -> s.getBus().getBusNumber() + " [" + s.getRoute().getSource() + "->" + s.getRoute().getDestination() + "]",
                        Schedule::getOccupancyRate
                ));
    }

    public double getTotalRevenue() {
        return bookings.values().stream()
                .filter(b -> "Confirmed".equalsIgnoreCase(b.getStatus()))
                .mapToDouble(Booking::getAmount)
                .sum();
    }
}
