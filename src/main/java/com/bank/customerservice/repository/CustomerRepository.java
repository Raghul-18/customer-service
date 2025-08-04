package com.bank.customerservice.repository;

import com.bank.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPan(String pan);

    Optional<Customer> findByAadhaar(String aadhaar);
}
