package com.example.diplom.service;

import com.example.diplom.entity.Customer;
import com.example.diplom.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    private static final String CUSTOMER_NOT_FOUND = "Пользователь с username = %s не найден";

    public Customer findByUsername (String username) {
        return customerRepository.findFirstByUsername(username);
    }

    public boolean isLoginUnique(String login) {
        return customerRepository.findFirstByUsername(login) == null;
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = customerRepository.findFirstByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(String.format(CUSTOMER_NOT_FOUND, username));
        }
        return userDetails;
    }
}
