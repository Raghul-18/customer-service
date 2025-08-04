package com.bank.customerservice.mapper;

import com.bank.customerservice.dto.CustomerRegistrationRequest;
import com.bank.customerservice.dto.CustomerResponse;
import com.bank.customerservice.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "aadhaar", source = "aadhaar")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "dob", source = "dob")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "pan", source = "pan")
    Customer toEntity(CustomerRegistrationRequest request);

    CustomerResponse toDto(Customer customer);

    // MapStruct does not support overloading, so define a default method
    default CustomerResponse toDto(Customer customer, String message) {
        CustomerResponse response = toDto(customer);
        response.setMessage(message);
        return response;
    }
}
