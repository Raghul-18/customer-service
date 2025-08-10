package com.bank.customerservice.service.impl;

import com.bank.customerservice.dto.*;
import com.bank.customerservice.entity.Customer;
import com.bank.customerservice.entity.KycStatus;
import com.bank.customerservice.exception.ResourceNotFoundException;
import com.bank.customerservice.mapper.CustomerMapper;
import com.bank.customerservice.repository.CustomerRepository;
import com.bank.customerservice.service.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    // ❌ REMOVE THIS MANUAL CONSTRUCTOR - @RequiredArgsConstructor handles this automatically
    // public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
    //     this.customerRepository = customerRepository;
    //     this.customerMapper = customerMapper;
    // }

    @Override
    public CustomerResponse register(CustomerRegistrationRequest request, Long userId) {
        // ✅ Check if user already has a customer record
        if (customerRepository.existsByUserId(userId)) {
            throw new IllegalStateException("Customer already exists for this user");
        }

        System.out.println(">>> REQUEST DATA:");
        System.out.println(">>> Request FullName = " + request.getFullName());
        System.out.println(">>> Request Phone    = " + request.getPhone());
        System.out.println(">>> Request Aadhaar  = " + request.getAadhaar());
        System.out.println(">>> JWT UserId       = " + userId); // ✅ Add this log

        Customer customer = customerMapper.toEntity(request);

        // 🔑 SET THE USER ID from JWT token
        customer.setUserId(userId);

        System.out.println(">>> MAPPED CUSTOMER: " + customer);
        System.out.println(">>> Full Name = " + customer.getFullName());
        System.out.println(">>> Phone     = " + customer.getPhone());
        System.out.println(">>> Aadhaar   = " + customer.getAadhaar());
        System.out.println(">>> User ID   = " + customer.getUserId()); // ✅ Add this log

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

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> customerMapper.toDto(customer))
                .toList();
    }

    @Override
    public CustomerResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerResponse updateKycStatus(Long customerId, KycStatusUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setKycStatus(request.getKycStatus());

        Customer updated = customerRepository.save(customer);
        return customerMapper.toDto(updated, request.getMessage() != null ? request.getMessage() : "KYC status updated");
    }

    // ======= USER-CUSTOMER RESOLUTION METHODS =======

    @Override
    public Long getCustomerIdByUserId(Long userId) {
        log.debug("🔍 Looking up customerId for userId: {}", userId);
        Optional<Customer> customer = customerRepository.findByUserId(userId);

        if (customer.isPresent()) {
            Long customerId = customer.get().getCustomerId();
            log.debug("✅ Found customerId: {} for userId: {}", customerId, userId);
            return customerId;
        } else {
            log.debug("❌ No customer found for userId: {}", userId);
            return null;
        }
    }

    @Override
    public Customer getCustomerByUserId(Long userId) {
        log.debug("🔍 Looking up customer entity for userId: {}", userId);
        return customerRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return customerRepository.existsByUserId(userId);
    }

    @Override
    public Customer getCustomerEntity(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
    }
}