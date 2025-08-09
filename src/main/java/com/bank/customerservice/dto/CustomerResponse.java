package com.bank.customerservice.dto;

import com.bank.customerservice.entity.KycStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long customerId;
    private String fullName;
    private String phone;
    private String email;
    private LocalDate dob;
    private String address;
    private String pan;
    private String aadhaar;
    private KycStatus kycStatus;
    private LocalDateTime registeredAt;
    private String message; // For success/error messages

    public void setMessage(String message) {
    }
}