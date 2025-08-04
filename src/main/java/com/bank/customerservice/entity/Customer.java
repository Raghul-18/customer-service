package com.bank.customerservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dob;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String pan;

    @Column(nullable = false, unique = true)
    private String aadhaar;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus = KycStatus.PENDING;

    private LocalDateTime registeredAt;
}
