package com.bank.customerservice.service.impl;

import com.bank.customerservice.dto.*;
import com.bank.customerservice.entity.Customer;
import com.bank.customerservice.entity.KycStatus;
import com.bank.customerservice.exception.ResourceNotFoundException;
import com.bank.customerservice.mapper.CustomerMapper;
import com.bank.customerservice.repository.CustomerRepository;
import com.bank.customerservice.service.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse register(CustomerRegistrationRequest request) {
        // Add this logging BEFORE mapping
        System.out.println(">>> REQUEST DATA:");
        System.out.println(">>> Request FullName = " + request.getFullName());
        System.out.println(">>> Request Phone    = " + request.getPhone());
        System.out.println(">>> Request Aadhaar  = " + request.getAadhaar());

        Customer customer = customerMapper.toEntity(request);

        // Your existing logging
        System.out.println(">>> MAPPED CUSTOMER: " + customer);
        System.out.println(">>> Full Name = " + customer.getFullName());
        System.out.println(">>> Phone     = " + customer.getPhone());
        System.out.println(">>> Aadhaar   = " + customer.getAadhaar());

        customer.setKycStatus(KycStatus.PENDING);
        customer.setRegisteredAt(LocalDateTime.now());
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDto(saved, "Customer registered successfully");
    }

    @Override
    public CustomerResponse getById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerResponse update(Long customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());

        Customer updated = customerRepository.save(customer);
        return customerMapper.toDto(updated, "Customer updated successfully");
    }

    @Override
    public CustomerResponse getStatus(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        return customerMapper.toDto(customer);
    }
}
