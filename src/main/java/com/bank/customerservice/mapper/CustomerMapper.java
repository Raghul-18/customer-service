package com.bank.customerservice.mapper;

import com.bank.customerservice.dto.CustomerRegistrationRequest;
import com.bank.customerservice.dto.CustomerResponse;
import com.bank.customerservice.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRegistrationRequest request);

    CustomerResponse toDto(Customer customer);

    default CustomerResponse toDto(Customer customer, String message) {
        CustomerResponse response = toDto(customer);
        response.setMessage(message);
        return response;
    }
}