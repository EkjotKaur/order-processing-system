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

    @ExceptionHandler(ProductInactiveException.class)
    public ResponseEntity<String> handleProductInactive(ProductInactiveException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleInventoryNotFound(InventoryNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<String> handleInventoryAlreadyExists(InventoryAlreadyExistsException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<String> handleInsufficientInventoryException(InsufficientInventoryException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientReservedInventoryException.class)
    public ResponseEntity<String> handleInsufficientReservedInventoryException(InsufficientReservedInventoryException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
