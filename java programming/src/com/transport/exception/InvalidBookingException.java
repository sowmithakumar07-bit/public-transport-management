package com.transport.exception;

/**
 * Custom Exception thrown for invalid input data or invalid booking states.
 */
public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
