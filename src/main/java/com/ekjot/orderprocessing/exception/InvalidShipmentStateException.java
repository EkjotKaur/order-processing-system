package com.ekjot.orderprocessing.exception;

import com.ekjot.orderprocessing.enums.ShipmentStatus;

public class InvalidShipmentStateException extends RuntimeException {
    public InvalidShipmentStateException(Long orderId, ShipmentStatus status) {
        super("Invalid Shipment state: " + status + " for orderId: " + orderId);
    }
}
