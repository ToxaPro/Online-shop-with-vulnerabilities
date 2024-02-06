package com.example.diplom.mapper;

import com.example.diplom.dto.CustomerInfoDto;
import com.example.diplom.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public CustomerInfoDto customerToCustomerInfoDto(Customer customer) {
        return CustomerInfoDto.builder()
            .surname(customer.getSurname())
            .name(customer.getName())
            .secondName(customer.getSecondName())
            .phone(customer.getPhone())
            .email(customer.getEmail())
            .address(customer.getAddress())
            .build();
    }
}
