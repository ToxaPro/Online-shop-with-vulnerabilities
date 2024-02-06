package com.example.diplom;

import com.example.diplom.entity.Product;
import com.example.diplom.entity.Position;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CashedShoppingCarts {
    private static Map<String, List<Position>> shippingCarts = new HashMap();

    public void addToCart(String username, Product product) {
        if (shippingCarts.get(username) == null) {
            List<Position> positionsList = new ArrayList<>();
            positionsList.add(Position.builder().product(product).amount(1).build());
            shippingCarts.put(username, positionsList);
        } else {
            List<Position> productsInOrder = shippingCarts.get(username);
            Position position = productsInOrder.stream().filter(it -> product.getId().equals(it.getProduct().getId())).findFirst().orElse(null);
            if (position != null) {
                position.setAmount(position.getAmount() + 1);
            } else {
                productsInOrder.add(Position.builder().product(product).amount(1).build());
            }
        }
    }

    public void removeFromCart(String username, Product product) {
        if (shippingCarts.get(username) != null) {
            List<Position> productsInOrder = shippingCarts.get(username);
            Position position = productsInOrder.stream().filter(it -> product.getId().equals(it.getProduct().getId())).findFirst().orElse(null);
            if (position != null) {
                if (position.getAmount() == 1) {
                    productsInOrder.remove(position);
                } else {
                    position.setAmount(position.getAmount() - 1);

                }
            }
        }
    }

    public List<Position> getPositionsByCustomer(String username) {
        return shippingCarts.get(username);
    }

    public void removeCustomersPosition(String username) {
        shippingCarts.remove(username);
    }
}
