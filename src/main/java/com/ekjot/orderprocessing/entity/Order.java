package com.ekjot.orderprocessing.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerId;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
