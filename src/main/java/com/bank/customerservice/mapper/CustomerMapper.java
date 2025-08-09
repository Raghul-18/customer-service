package com.bank.customerservice.mapper;

import com.bank.customerservice.dto.CustomerRegistrationRequest;
import com.bank.customerservice.dto.CustomerResponse;
import com.bank.customerservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Map from registration request to entity
    @Mapping(target = "customerId", ignore = true)  // ID is auto-generated
    @Mapping(target = "kycStatus", ignore = true)   // Set in service
    @Mapping(target = "registeredAt", ignore = true) // Set in service
    Customer toEntity(CustomerRegistrationRequest request);

    // Map from entity to response DTO
    @Mapping(target = "message", ignore = true)     // Set manually in service
    CustomerResponse toDto(Customer customer);

    // Helper method with custom message
    default CustomerResponse toDto(Customer customer, String message) {
        CustomerResponse response = toDto(customer);
        response.setMessage(message);
        return response;
    }

    // Update existing entity (if needed)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "dob", ignore = true)
    @Mapping(target = "pan", ignore = true)
    @Mapping(target = "aadhaar", ignore = true)
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "registeredAt", ignore = true)
    void updateEntity(@MappingTarget Customer customer, CustomerRegistrationRequest request);
}