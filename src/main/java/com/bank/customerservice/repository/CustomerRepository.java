package com.bank.customerservice.repository;

import com.bank.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Existing methods (if any)
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
    Optional<Customer> findByAadhaar(String aadhaar);
    Optional<Customer> findByPan(String pan);

    // NEW: Methods for user-customer resolution
    Optional<Customer> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}