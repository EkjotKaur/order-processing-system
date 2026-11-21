package com.ekjot.orderprocessing.controller;

import com.ekjot.orderprocessing.dto.request.CreateShipmentRequest;
import com.ekjot.orderprocessing.dto.response.ShipmentResponse;
import com.ekjot.orderprocessing.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService){
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody CreateShipmentRequest request){
        ShipmentResponse response = shipmentService.createShipment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/{orderId}")
    public ShipmentResponse getShipment(@PathVariable Long orderId){
        return shipmentService.getShipment(orderId);
    }

    @PostMapping("/order/{orderId}/pack")
    public ShipmentResponse packShipment(@PathVariable Long orderId){
        return shipmentService.packShipment(orderId);
    }

    @PostMapping("/order/{orderId}/ship")
    public ShipmentResponse shipShipment(@PathVariable Long orderId){
        return shipmentService.shipShipment(orderId);
    }

    @PostMapping("/order/{orderId}/deliver")
    public ShipmentResponse deliverShipment(@PathVariable Long orderId){
        return shipmentService.deliverShipment(orderId);
    }

    @PostMapping("/order/{orderId}/return")
    public ShipmentResponse returnShipment(@PathVariable Long orderId){
        return shipmentService.returnShipment(orderId);
    }
}
