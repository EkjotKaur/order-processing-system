package com.ekjot.orderprocessing.controller;

import com.ekjot.orderprocessing.dto.request.CreateOrderRequest;
import com.ekjot.orderprocessing.dto.response.OrderResponse;
import com.ekjot.orderprocessing.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request){
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrder(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<OrderResponse> getCustomerOrders(@PathVariable Long customerId){
        return orderService.getOrders(customerId);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancelOrder(@PathVariable Long id){
        return orderService.cancelOrder(id);
    }
}
