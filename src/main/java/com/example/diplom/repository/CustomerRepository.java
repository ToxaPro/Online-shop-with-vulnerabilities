package com.example.diplom.repository;

import com.example.diplom.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findFirstByUsername(String login);
}