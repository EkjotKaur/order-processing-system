package com.ekjot.orderprocessing.controller;

import com.ekjot.orderprocessing.dto.request.CreateInventoryRequest;
import com.ekjot.orderprocessing.dto.request.UpdateInventoryPriceRequest;
import com.ekjot.orderprocessing.dto.request.UpdateInventoryStockRequest;
import com.ekjot.orderprocessing.dto.response.InventoryResponse;
import com.ekjot.orderprocessing.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody CreateInventoryRequest request){
        InventoryResponse response = inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}")
    public InventoryResponse getInventory(@PathVariable Long productId){
       return inventoryService.getInventory(productId);
    }

    @PutMapping("/stock")
    public ResponseEntity<InventoryResponse> updateInventory(@Valid @RequestBody UpdateInventoryStockRequest request){
        InventoryResponse response = inventoryService.increaseStock(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/price")
    public ResponseEntity<InventoryResponse> updateInventoryPrice(@Valid @RequestBody UpdateInventoryPriceRequest request){
        InventoryResponse response = inventoryService.updatePrice(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reserve")
    public ResponseEntity<InventoryResponse> reserveInventory(@Valid @RequestBody UpdateInventoryStockRequest request){
        InventoryResponse response = inventoryService.reserveStock(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/release")
    public ResponseEntity<InventoryResponse> releaseInventory(@Valid @RequestBody UpdateInventoryStockRequest request){
        InventoryResponse response = inventoryService.releaseReservation(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/confirm")
    public ResponseEntity<InventoryResponse> confirmReservation(@Valid @RequestBody UpdateInventoryStockRequest request){
        InventoryResponse response = inventoryService.confirmReservation(request);
        return ResponseEntity.ok(response);
    }
}
