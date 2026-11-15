package com.ekjot.orderprocessing.exception;

import com.ekjot.orderprocessing.enums.PaymentStatus;

public class InvalidPaymentStateException extends RuntimeException {
    public InvalidPaymentStateException(Long paymentId, PaymentStatus status) {
        super("Payment " + paymentId + " is in " + status + " state and cannot be processed.");
    }
}

