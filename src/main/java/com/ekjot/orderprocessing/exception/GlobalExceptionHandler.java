package com.ekjot.orderprocessing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e){
        return ResponseEntity.badRequest().body("Validation Failed!");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String>handleProductNotFound(ProductNotFoundException e){
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
