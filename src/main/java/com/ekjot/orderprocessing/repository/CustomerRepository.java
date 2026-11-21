package com.ekjot.orderprocessing.repository;

import com.ekjot.orderprocessing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);
}
