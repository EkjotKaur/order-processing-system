package com.ekjot.orderprocessing.exception;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(Long orderId) {
        super("Payment Failed for orderId: " + orderId);
    }
}
