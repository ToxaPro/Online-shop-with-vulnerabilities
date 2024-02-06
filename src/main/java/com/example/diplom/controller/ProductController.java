package com.example.diplom.controller;

import com.example.diplom.entity.Product;
import com.example.diplom.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static com.example.diplom.constant.WebConstants.*;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    @GetMapping("")
    public String getProducts(Model model) {
        model.addAttribute(PRODUCTS_ATTRIBUTE, productRepository.findAll());
        return PRODUCTS_PAGE;
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                model.addAttribute(PRODUCT_ATTRIBUTE, product.get());
                return PRODUCT_PAGE;
            }
        return PRODUCTS_PAGE;
    }
}
