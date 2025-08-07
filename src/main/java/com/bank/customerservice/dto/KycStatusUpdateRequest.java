package com.bank.customerservice.dto;

import com.bank.customerservice.entity.KycStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KycStatusUpdateRequest {
    private KycStatus kycStatus;
    private String message;
}
