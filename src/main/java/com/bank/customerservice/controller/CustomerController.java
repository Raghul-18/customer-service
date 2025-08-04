package com.bank.customerservice.controller;

import com.bank.customerservice.dto.*;
import com.bank.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.ok(customerService.register(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> update(@PathVariable Long customerId,
                                                   @Valid @RequestBody CustomerUpdateRequest request) {
        return ResponseEntity.ok(customerService.update(customerId, request));
    }

    @GetMapping("/{customerId}/status")
    public ResponseEntity<CustomerResponse> getStatus(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getStatus(customerId));
    }
}
