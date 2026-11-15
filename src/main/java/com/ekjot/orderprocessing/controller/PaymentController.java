package com.ekjot.orderprocessing.controller;

import com.ekjot.orderprocessing.dto.request.CreatePaymentRequest;
import com.ekjot.orderprocessing.dto.response.PaymentResponse;
import com.ekjot.orderprocessing.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request){
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable Long id){
        return paymentService.getPayment(id);
    }

    @PostMapping("/{id}/confirm")
    public PaymentResponse confirmPayment(@PathVariable Long id){
        return paymentService.confirmPayment(id);
    }

    @PostMapping("/{id}/fail")
    public PaymentResponse failPayment(@PathVariable Long id){
        return paymentService.failPayment(id);
    }
}
