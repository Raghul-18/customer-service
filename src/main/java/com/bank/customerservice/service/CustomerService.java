package com.bank.customerservice.service;

import com.bank.customerservice.dto.*;
import java.util.List;

public interface CustomerService {

    CustomerResponse register(CustomerRegistrationRequest request);

    CustomerResponse getById(Long customerId);

    CustomerResponse update(Long customerId, CustomerUpdateRequest request);

    CustomerResponse getStatus(Long customerId);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse getCustomerById(Long customerId);

    CustomerResponse updateKycStatus(Long customerId, KycStatusUpdateRequest request);

}
