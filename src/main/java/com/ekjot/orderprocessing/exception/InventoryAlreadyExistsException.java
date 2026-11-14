package com.ekjot.orderprocessing.exception;

public class InventoryAlreadyExistsException extends RuntimeException {
    public InventoryAlreadyExistsException(Long productId) {
        super("Inventory already present with productId: " + productId);
    }
}
