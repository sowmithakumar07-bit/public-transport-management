package com.transport.exception;

/**
 * Custom Exception thrown when attempting to book a seat that is already booked
 * or when bus capacity is exceeded.
 * Aligned with Unit 3: Exception Handling (User-defined Exceptions).
 */
public class SeatUnavailableException extends Exception {
    public SeatUnavailableException(String message) {
        super(message);
    }
}
