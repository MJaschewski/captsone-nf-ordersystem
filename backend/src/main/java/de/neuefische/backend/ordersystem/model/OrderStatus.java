package de.neuefische.backend.ordersystem.model;

public enum OrderStatus {
    REQUESTED("Requested"),
    IN_TRANSIT("In_Transit"),
    ARRIVED("Arrived"),
    CANCELLED("Cancelled");

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }
}

