package com.bank.customerservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRegistrationRequest {

    @NotBlank
    // Remove @JsonProperty if JSON field is already "fullName"
    private String fullName;

    @NotBlank
    @Pattern(regexp = "^\\+91\\d{10}$", message = "Phone must be a valid Indian number")
    private String phone;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private LocalDate dob;

    @NotBlank
    private String address;

    @NotBlank
    @Size(min = 10, max = 10, message = "PAN must be 10 characters")
    private String pan;

    @NotBlank
    @Size(min = 12, max = 12, message = "Aadhaar must be 12 digits")
    private String aadhaar;
}