package com.bank.customerservice.controller;

import com.bank.customerservice.dto.CustomerResponse;
import com.bank.customerservice.dto.KycStatusUpdateRequest;
import com.bank.customerservice.security.JwtAuthInterceptor;
import com.bank.customerservice.service.CustomerService;
import com.bank.customerservice.util.AuthenticatedUser;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/admin")
@RequiredArgsConstructor
public class CustomerAdminController {

    private final CustomerService customerService;

    // ✅ GET /api/customers/admin/all
    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        validateAdmin();
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // ✅ GET /api/customers/admin/{customerId}
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId) {
        validateAdmin();
        return ResponseEntity.ok(customerService.getById(customerId));
    }

    // ✅ PUT /api/customers/admin/{customerId}/kyc-status (Fixed path)
    @PutMapping("/{customerId}/kyc-status")
    public ResponseEntity<CustomerResponse> updateKycStatus(
            @PathVariable Long customerId,
            @RequestBody KycStatusUpdateRequest request) {
        validateAdmin();
        return ResponseEntity.ok(customerService.updateKycStatus(customerId, request));
    }

    private void validateAdmin() {
        AuthenticatedUser user = JwtAuthInterceptor.getCurrentUser();
        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new SecurityException("Only ADMIN can access this endpoint");
        }
    }
}