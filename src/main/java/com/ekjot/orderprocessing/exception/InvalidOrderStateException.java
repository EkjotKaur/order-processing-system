package com.ekjot.orderprocessing.exception;

public class InvalidOrderStateException extends RuntimeException {
  public InvalidOrderStateException(String message) {
    super(message);
  }
}
