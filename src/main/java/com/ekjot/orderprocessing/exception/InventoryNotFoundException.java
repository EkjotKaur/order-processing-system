package com.ekjot.orderprocessing.exception;

import com.ekjot.orderprocessing.entity.Inventory;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(Long productId){
        super("Inventory with productId: " + productId + " not found!");
    }
}
