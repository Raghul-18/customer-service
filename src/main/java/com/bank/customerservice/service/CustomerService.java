package com.bank.customerservice.service;

import com.bank.customerservice.dto.*;

public interface CustomerService {

    CustomerResponse register(CustomerRegistrationRequest request);

    CustomerResponse getById(Long customerId);

    CustomerResponse update(Long customerId, CustomerUpdateRequest request);

    CustomerResponse getStatus(Long customerId);
}
