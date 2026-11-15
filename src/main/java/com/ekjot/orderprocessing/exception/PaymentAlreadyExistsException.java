package com.ekjot.orderprocessing.exception;

public class PaymentAlreadyExistsException extends RuntimeException {
  public PaymentAlreadyExistsException(String message) {
    super(message);
  }
}
