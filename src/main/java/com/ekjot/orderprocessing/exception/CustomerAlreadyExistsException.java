package com.ekjot.orderprocessing.exception;

public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String email) {
        super("Customer already exists with email: " + email);
    }
}
