package com.ekjot.orderprocessing.exception;

public class ShipmentAlreadyExistsException extends RuntimeException {
    public ShipmentAlreadyExistsException(Long orderId) {
        super("Shipment already exists for orderId: " + orderId);
    }
}
