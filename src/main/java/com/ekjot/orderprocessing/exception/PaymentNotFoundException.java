package com.ekjot.orderprocessing.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long paymentId) {
        super("Payment Not Found for paymentId: " + paymentId);
    }
}
