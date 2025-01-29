package net.olxApplication.controller;

import net.olxApplication.Entity.Order;
import net.olxApplication.Entity.Product;
import net.olxApplication.Entity.User;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.ResponseBodies.ProductResponse;
import net.olxApplication.ResponseBodies.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class ConvertResponses {

    public UserResponse covertUser(User user){
        return UserResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .walletBalance(user.getWallet().getBalance())
                .userStatus(user.getStatus())
                .orderNumber(user.getOrders().size())
                .productsAdded(user.getProduct().size())
                .build();


    }
    public ProductResponse covertProduct(Product prod){
        return ProductResponse.builder()
                .productName(prod.getName())
                .productStatus(prod.getStatus())
                .price(prod.getPrice())
                .build();


    }
    public OrderResponse covertOrder(Order order){
        return OrderResponse.builder()
                .username(order.getUser().getName())
                .orderStatus(order.getStatus())
                .price(order.getProduct().getPrice())
                .productName(order.getProduct().getName())
                .build();


    }
}
