package com.bank.customerservice.dto;

import lombok.Data;

@Data
public class CustomerIdResponse {
    private Long customerId;

    public CustomerIdResponse() {
        // No-arg constructor
    }

    public CustomerIdResponse(Long customerId) {
        this.customerId = customerId;
    }
}