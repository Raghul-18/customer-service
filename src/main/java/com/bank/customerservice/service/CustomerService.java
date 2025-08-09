package com.bank.customerservice.service;

import com.bank.customerservice.dto.*;
import com.bank.customerservice.entity.Customer;
import java.util.List;

public interface CustomerService {

    // Existing methods
    CustomerResponse register(CustomerRegistrationRequest request);
    CustomerResponse getById(Long customerId);
    CustomerResponse update(Long customerId, CustomerUpdateRequest request);
    CustomerResponse getStatus(Long customerId);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(Long customerId);
    CustomerResponse updateKycStatus(Long customerId, KycStatusUpdateRequest request);

    // NEW: User-Customer resolution methods for KYC authorization
    Long getCustomerIdByUserId(Long userId);
    Customer getCustomerByUserId(Long userId);
    boolean existsByUserId(Long userId);
}