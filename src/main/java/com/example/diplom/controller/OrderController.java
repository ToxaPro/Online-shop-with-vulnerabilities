package com.example.diplom.controller;

import com.example.diplom.CashedShoppingCarts;
import com.example.diplom.entity.Order;
import com.example.diplom.entity.Position;
import com.example.diplom.repository.CustomerRepository;
import com.example.diplom.repository.OrderRepository;
import com.example.diplom.repository.PositionRepository;
import com.example.diplom.util.RedirectionUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.diplom.constant.WebConstants.*;

@Controller
@AllArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final CashedShoppingCarts cashedShoppingCarts;

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final PositionRepository positionRepository;
    
    private static final String STATUS_CREATED = "Создан";

    private static final String ORDER_NOT_FOUND = "Заказ с идентификатором = %d не найден";

    @GetMapping("")
    public String getOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            model.addAttribute(ORDERS_ATTRIBUTE,
                    customerRepository.findFirstByUsername(userDetails.getUsername()).getOrders());
            return ORDERS_PAGE;
        }
        return LOGIN_PAGE;
    }

    @GetMapping("/{id}")
    public String getOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, Model model,
                           HttpServletResponse response) {
        if (userDetails != null) {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                if (!userDetails.getUsername().equals(order.get().getCustomer().getUsername())) {
                    return RedirectionUtils.redirectToErrors(model, NOT_ENOUGH_RIGHTS, response);
                }
                Set<Position> positions = order.get().getPositions();
                model.addAttribute(POSITIONS_ATTRIBUTE, positions);
                model.addAttribute(COST_ATTRIBUTE, positions.stream().mapToDouble(it -> it.getAmount() * it.getProduct().getPrice()).sum());
                return ORDER_PAGE;
            }
            return RedirectionUtils.redirectToErrors(model, String.format(ORDER_NOT_FOUND, id),  response);
        }
        return LOGIN_PAGE;
    }

    @PostMapping("")
    public String createOrder(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            List<Position> positions = cashedShoppingCarts.getPositionsByCustomer(userDetails.getUsername());
            Order order = orderRepository.save(
                Order
                .builder()
                .createDate(LocalDateTime.now())
                .status(STATUS_CREATED)
                .customer(customerRepository.findFirstByUsername(userDetails.getUsername()))
                .build());
            positions.forEach(it -> 
                positionRepository.save(
                    Position
                    .builder()
                    .order(order)
                    .product(it.getProduct())
                    .amount(it.getAmount())
                    .build())
            );
            cashedShoppingCarts.removeCustomersPosition(userDetails.getUsername());
        }
        return "redirect:/orders";
    }
}
