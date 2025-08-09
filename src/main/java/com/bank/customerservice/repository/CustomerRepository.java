package com.bank.customerservice.repository;

import com.bank.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // ✅ Add these methods for userId-based operations
    Optional<Customer> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    // Existing unique constraint checks
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPan(String pan);
    boolean existsByAadhaar(String aadhaar);
}