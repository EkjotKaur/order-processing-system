package com.ekjot.orderprocessing.repository;

import com.ekjot.orderprocessing.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    public Optional<Inventory> findByProductId(Long productId);
}
