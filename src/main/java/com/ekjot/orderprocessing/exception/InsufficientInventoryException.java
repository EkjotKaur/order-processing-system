package com.ekjot.orderprocessing.exception;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(Long productId) {
        super("Insufficient available quantity for productid: " + productId);
    }
}
