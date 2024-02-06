package com.example.diplom.controller;

import com.example.diplom.dto.CustomerLoginDto;
import com.example.diplom.entity.Customer;
import com.example.diplom.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static com.example.diplom.constant.WebConstants.*;

@Controller
@AllArgsConstructor
public class AuthController {
    private final CustomerService customerService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute(CUSTOMER_ATTRIBUTE, new CustomerLoginDto("", ""));
        return LOGIN_PAGE;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute(CUSTOMER_ATTRIBUTE, new Customer());
        return REGISTER_PAGE;
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
        if (!result.hasErrors() && customer.getUsername() != null && !customerService.isLoginUnique(customer.getUsername())) {
            model.addAttribute(ERROR_ATTRIBUTE, "Пользователь с таким логином уже существует");
            model.addAttribute(CUSTOMER_ATTRIBUTE, customer);
            return LOGIN_PAGE;
        }
        if (result.hasErrors()) {
            model.addAttribute(CUSTOMER_ATTRIBUTE, customer);
            return REGISTER_PAGE;
        }
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        customerService.save(customer);
        return "redirect:/products";
    }
}
