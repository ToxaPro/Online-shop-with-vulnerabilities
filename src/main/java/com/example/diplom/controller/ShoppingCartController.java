package com.example.diplom.controller;

import com.example.diplom.CashedShoppingCarts;
import com.example.diplom.dto.PositionDto;
import com.example.diplom.entity.Customer;
import com.example.diplom.mapper.PositionMapper;
import com.example.diplom.service.CustomerService;
import com.example.diplom.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static com.example.diplom.constant.WebConstants.*;

@Controller
@AllArgsConstructor
@RequestMapping("shopping-cart")
public class ShoppingCartController {
    private final CashedShoppingCarts shippingCarts;

    private final CustomerService customerService;

    private final ProductService productService;

    @GetMapping("")
    public String getShoppingCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            Customer customer = customerService.findByUsername(userDetails.getUsername());
            if (customer != null) {
                List<PositionDto> positions = new ArrayList<>();
                if (shippingCarts.getPositionsByCustomer(customer.getUsername()) != null) {
                    shippingCarts.getPositionsByCustomer(customer.getUsername()).forEach(it -> positions.add(PositionMapper.mapPositionToPositionDto(it)));
                    model.addAttribute(POSITIONS_ATTRIBUTE, positions);
                    model.addAttribute(COST_ATTRIBUTE, positions.stream().mapToDouble(it -> it.getAmount() * it.getProduct().getPrice()).sum());
                }
                return SHOPPING_CART;
            }
        }
        return "redirect:/";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            shippingCarts.addToCart(userDetails.getUsername(), productService.findById(productId));
        }
        return "redirect:/shopping-cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            shippingCarts.removeFromCart(userDetails.getUsername(), productService.findById(productId));
        }
        return "redirect:/shopping-cart";
    }
}
