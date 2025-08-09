package com.bank.customerservice.controller;

import com.bank.customerservice.dto.*;
import com.bank.customerservice.exception.ResourceNotFoundException;
import com.bank.customerservice.security.JwtAuthInterceptor;
import com.bank.customerservice.util.AuthenticatedUser;
import com.bank.customerservice.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.customerservice.dto.CustomerIdResponse;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@Valid @RequestBody CustomerRegistrationRequest request) {
        // 🔐 Extract userId from JWT token
        AuthenticatedUser currentUser = JwtAuthInterceptor.getCurrentUser();
        Long userId = currentUser.getUserId();

        return ResponseEntity.ok(customerService.register(request, userId));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long customerId) {
        validateAccess(customerId);
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long customerId,
                                                   @Valid @RequestBody CustomerUpdateRequest request) {
        validateAccess(customerId);
        return ResponseEntity.ok(customerService.update(customerId, request));
    }

    @GetMapping("/{customerId}/status")
    public ResponseEntity<CustomerResponse> getStatus(@PathVariable Long customerId) {
        validateAccess(customerId);
        return ResponseEntity.ok(customerService.getStatus(customerId));
    }

    // INTERNAL: Update KYC status (used by KYC Service)
    @PutMapping("/{customerId}/kyc-status")
    public ResponseEntity<CustomerResponse> updateKycStatus(
            @PathVariable Long customerId,
            @Valid @RequestBody KycStatusUpdateRequest request) {
        return ResponseEntity.ok(customerService.updateKycStatus(customerId, request));
    }

    // 🔐 Access control method
    private void validateAccess(Long requestedCustomerId) {
        AuthenticatedUser currentUser = JwtAuthInterceptor.getCurrentUser();

        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            return; // Allow
        }

        // Role = CUSTOMER, check match
        String expectedUsername = "customer_" + requestedCustomerId;
        if (!expectedUsername.equals(currentUser.getUsername())) {
            throw new SecurityException("Access denied");
        }
    }
    @GetMapping("/user/{userId}/customer-id")
    public ResponseEntity<CustomerIdResponse> getCustomerIdByUserId(@PathVariable Long userId) {
        AuthenticatedUser currentUser = JwtAuthInterceptor.getCurrentUser();

        // Allow if admin OR if user is requesting their own customer ID
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole()) &&
                !currentUser.getUserId().equals(userId)) {
            throw new SecurityException("Access denied - you can only access your own customer ID");
        }

        Long customerId = customerService.getCustomerIdByUserId(userId);
        if (customerId == null) {
            throw new ResourceNotFoundException("No customer found for user: " + userId);
        }

        return ResponseEntity.ok(new CustomerIdResponse(customerId));
    }

    @GetMapping("/{customerId}/verify-ownership/{userId}")
    public ResponseEntity<Boolean> verifyCustomerOwnership(
            @PathVariable Long customerId,
            @PathVariable Long userId) {

        AuthenticatedUser currentUser = JwtAuthInterceptor.getCurrentUser();

        // Allow if admin OR if user is verifying their own ownership
        if (!"ADMIN".equalsIgnoreCase(currentUser.getRole()) &&
                !currentUser.getUserId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
            boolean isOwner = customer.getUserId().equals(userId);
            return ResponseEntity.ok(isOwner);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(false);
        }
    }
}