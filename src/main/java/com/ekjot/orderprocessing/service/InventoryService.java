package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreateInventoryRequest;
import com.ekjot.orderprocessing.dto.request.UpdateInventoryPriceRequest;
import com.ekjot.orderprocessing.dto.request.UpdateInventoryStockRequest;
import com.ekjot.orderprocessing.dto.response.InventoryResponse;
import com.ekjot.orderprocessing.entity.Inventory;
import com.ekjot.orderprocessing.entity.Product;
import com.ekjot.orderprocessing.exception.*;
import com.ekjot.orderprocessing.repository.InventoryRepository;
import com.ekjot.orderprocessing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    private InventoryResponse mapToInventoryResponse(Inventory inventory){
        return new InventoryResponse(inventory.getId(), inventory.getProduct().getId(), inventory.getProduct().getName(), inventory.getAvailableQuantity(), inventory.getReservedQuantity(), inventory.getPrice(), inventory.getCreatedAt(), inventory.getUpdatedAt());
    }

    public Inventory getInventoryByProductId(Long productId){
        return inventoryRepository.findByProductId(productId).orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository){
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public InventoryResponse createInventory(CreateInventoryRequest request){
        Inventory inventory = new Inventory();
        Long productId = request.getProductId();
        if(inventoryRepository.findByProductId(productId).isPresent()){
            throw new InventoryAlreadyExistsException(productId);
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        inventory.setProduct(product);
        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReservedQuantity(0);
        inventory.setPrice(request.getPrice());
        LocalDateTime now = LocalDateTime.now();
        inventory.setCreatedAt(now);
        inventory.setUpdatedAt(now);

        inventoryRepository.save(inventory);

        return mapToInventoryResponse(inventory);
    }

    public InventoryResponse getInventory(Long productId){
        Inventory inventory = getInventoryByProductId(productId);
        return mapToInventoryResponse(inventory);
    }

    @Transactional
    public InventoryResponse increaseStock(UpdateInventoryStockRequest request){
        Inventory inventory = getInventoryByProductId(request.getProductId());

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + request.getQuantity());
        inventory.setUpdatedAt(LocalDateTime.now());
        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToInventoryResponse(savedInventory);
    }

    @Transactional
    public InventoryResponse updatePrice(UpdateInventoryPriceRequest request){
        Inventory inventory = getInventoryByProductId(request.getProductId());

        inventory.setPrice(request.getPrice());
        inventory.setUpdatedAt(LocalDateTime.now());
        Inventory savedInventory = inventoryRepository.save(inventory);

        return mapToInventoryResponse(savedInventory);
    }

    @Transactional
    public Inventory reserveStock(Long productId, Integer quantity){
        Inventory inventory = getInventoryByProductId(productId);
        if(quantity>inventory.getAvailableQuantity()){
            throw new InsufficientInventoryException(productId);
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
        return inventory;
    }

    @Transactional
    public InventoryResponse reserveStock(UpdateInventoryStockRequest request){
        Long productId = request.getProductId();
        Inventory inventory = reserveStock(productId, request.getQuantity());

        return mapToInventoryResponse(inventory);
    }

    @Transactional
    public void releaseReservation(Long productId, Integer quantity){
        Inventory inventory = getInventoryByProductId(productId);
        if(quantity>inventory.getReservedQuantity()){
            throw new InsufficientReservedInventoryException(productId);
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    @Transactional
    public InventoryResponse releaseReservation(UpdateInventoryStockRequest request){
        Long productId = request.getProductId();
        releaseReservation(productId, request.getQuantity());

        return mapToInventoryResponse(getInventoryByProductId(productId));
    }

    @Transactional
    public void confirmReservation(Long productId, Integer quantity){
        Inventory inventory = getInventoryByProductId(productId);
        if(quantity>inventory.getReservedQuantity()){
            throw new InsufficientReservedInventoryException(productId);
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    @Transactional
    public InventoryResponse confirmReservation(UpdateInventoryStockRequest request){
        Long productId = request.getProductId();
        confirmReservation(productId, request.getQuantity());

        return mapToInventoryResponse(getInventoryByProductId(productId));
    }
}
