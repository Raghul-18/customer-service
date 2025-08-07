package com.bank.customerservice.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticatedUser {
    private Long userId;
    private String role;
    private String username;
}
