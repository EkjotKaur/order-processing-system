package com.ekjot.orderprocessing.repository;

import com.ekjot.orderprocessing.entity.Payment;
import com.ekjot.orderprocessing.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    public Optional<Payment> findByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
