package com.transport.model;

import java.time.LocalDate;

/**
 * Handles Ticket details and generation associated with a Booking.
 * Aligned with Class Diagram.
 */
public class TicketBooking {
    private int ticketId;
    private Booking booking;
    private String ticketNo;
    private LocalDate issueDate;
    private double totalFare;
    private String qrCode;

    public TicketBooking(int ticketId, Booking booking, String ticketNo, double totalFare, String qrCode) {
        this.ticketId = ticketId;
        this.booking = booking;
        this.ticketNo = ticketNo;
        this.issueDate = LocalDate.now();
        this.totalFare = totalFare;
        this.qrCode = qrCode;
    }

    public void bookTicket() {
        this.booking.confirmBooking();
    }

    public void cancelTicket() {
        this.booking.cancelBooking();
    }

    public String generateTicket() {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("             ELECTRONIC BUS PASSENGER TICKET        \n");
        sb.append("====================================================\n");
        sb.append(String.format("Ticket No   : %s\n", ticketNo));
        sb.append(String.format("Booking ID  : %d\n", booking.getBookingId()));
        sb.append(String.format("Issue Date  : %s\n", issueDate));
        sb.append(String.format("Route       : %s -> %s\n", 
                booking.getSchedule().getRoute().getSource(), booking.getSchedule().getRoute().getDestination()));
        sb.append(String.format("Bus Number  : %s (%s)\n", 
                booking.getSchedule().getBus().getBusNumber(), booking.getSchedule().getBus().getType()));
        sb.append(String.format("Travel Date : %s | Departure: %s\n", 
                booking.getSchedule().getTravelDate(), booking.getSchedule().getDepartureTime()));
        sb.append(String.format("Seat Number : %s\n", booking.getSeatNo()));
        sb.append(String.format("Total Fare  : ₹%.2f\n", totalFare));
        sb.append(String.format("Status      : %s\n", booking.getStatus()));
        sb.append(String.format("Digital QR  : [%s]\n", qrCode));
        sb.append("====================================================\n");
        return sb.toString();
    }

    public void viewTicket() {
        System.out.println(generateTicket());
    }

    public int getTicketId() {
        return ticketId;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public String getQrCode() {
        return qrCode;
    }
}
