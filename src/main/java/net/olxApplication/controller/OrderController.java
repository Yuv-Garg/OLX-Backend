package net.olxApplication.controller;

import lombok.AllArgsConstructor;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.OrderService;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final TransactionRepository transactionRepository;

    // Get Order History of a User
    @GetMapping("/getUserOrders")
    public List<OrderResponse> getOrders(@RequestParam("user_id") Long id) throws NotExist, BadRequest {
        return orderService.getOrdersByUserId(id);
    }

    // Place an order
    @PostMapping("/placeOrder")
    public ResponseEntity<MessageResponse> placeOrder(@RequestParam("user_id") Long Uid, @RequestParam("prod_id") Long Pid)throws NotExist, BadRequest {
        return orderService.placeOrder(Uid, Pid);
    }

    // Cancel an order
    @PutMapping("/cancelOrder")
    public ResponseEntity<OrderResponse> cancelOrder(@RequestParam("order_id") Long Oid)throws NotExist, BadRequest {
        return orderService.cancelOrder(Oid);
    }



}
