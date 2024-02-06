package com.example.diplom.service;

import com.example.diplom.entity.Product;
import com.example.diplom.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Product findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
