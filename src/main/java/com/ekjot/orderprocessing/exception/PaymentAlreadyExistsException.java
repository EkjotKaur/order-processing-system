package com.ekjot.orderprocessing.exception;

public class PaymentAlreadyExistsException extends RuntimeException {
    public PaymentAlreadyExistsException(Long orderId) {
        super("Payment already exists for orderId: " + orderId);
    }
}
