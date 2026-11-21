package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreateCustomerRequest;
import com.ekjot.orderprocessing.dto.response.CustomerResponse;
import com.ekjot.orderprocessing.entity.Customer;
import com.ekjot.orderprocessing.exception.CustomerAlreadyExistsException;
import com.ekjot.orderprocessing.exception.CustomerNotFoundException;
import com.ekjot.orderprocessing.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    private CustomerResponse mapToCustomerResponse(Customer customer){
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone(), customer.getDeviceToken(), customer.getCreatedAt());
    }

    // Reusable by other services (e.g. Notification) that need a customer's contact details
    public Customer getCustomerById(Long customerId){
        return customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request){
        if(customerRepository.existsByEmail(request.getEmail())){
            throw new CustomerAlreadyExistsException(request.getEmail());
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setDeviceToken(request.getDeviceToken());
        LocalDateTime now = LocalDateTime.now();
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        customerRepository.save(customer);

        return mapToCustomerResponse(customer);
    }

    public CustomerResponse getCustomer(Long customerId){
        Customer customer = getCustomerById(customerId);
        return mapToCustomerResponse(customer);
    }
}
