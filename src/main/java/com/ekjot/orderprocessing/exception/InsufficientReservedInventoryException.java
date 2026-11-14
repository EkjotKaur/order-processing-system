package com.ekjot.orderprocessing.exception;

public class InsufficientReservedInventoryException extends RuntimeException {
    public InsufficientReservedInventoryException(Long productId) {
        super("Insufficient Reserved Quantity for productId: " + productId);
    }
}
