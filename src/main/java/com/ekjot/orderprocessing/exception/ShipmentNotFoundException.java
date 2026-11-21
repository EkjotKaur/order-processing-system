package com.ekjot.orderprocessing.exception;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(Long orderId) {
        super("Shipment not found for orderId: " + orderId);
    }
}
