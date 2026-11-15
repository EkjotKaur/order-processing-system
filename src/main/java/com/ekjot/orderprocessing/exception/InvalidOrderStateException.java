package com.ekjot.orderprocessing.exception;

import com.ekjot.orderprocessing.enums.OrderStatus;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(Long id, OrderStatus status) {
        super("Invalid Order status: " + status + " for orderId: " + id);
    }
}
