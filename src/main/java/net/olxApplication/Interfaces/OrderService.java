package net.olxApplication.Interfaces;

import net.olxApplication.Entity.Order;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.ResponseBodies.OrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getOrdersByUserId(Long id)throws NotExist, BadRequest;
    ResponseEntity<?> placeOrder(Long userId, Long productId)throws NotExist, BadRequest;
    ResponseEntity<?> cancelOrder(Long orderId)throws NotExist, BadRequest;
}
