package com.transport.model;

/**
 * Represents a transit Route connecting source and destination.
 */
public class Route {
    private int routeId;
    private String source;
    private String destination;
    private double distance;

    public Route(int routeId, String source, String destination, double distance) {
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    public void updateRoute(String source, String destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    public int getRouteId() {
        return routeId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s -> %s (%.1f km)", routeId, source, destination, distance);
    }
}
