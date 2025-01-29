package net.olxApplication.controller;

import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.OrderService;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/olx")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get Order History of a User
    @GetMapping("getUserOrders")
    public List<OrderResponse> getOrders(@RequestParam("user_id") Long id) throws NotExist, BadRequest {
        return orderService.getOrdersByUserId(id);
    }

    // Place an order
    @PostMapping("placeOrder")
    public ResponseEntity<?> placeOrder(@RequestParam("user_id") Long Uid, @RequestParam("prod_id") Long Pid)throws NotExist, BadRequest {
        return orderService.placeOrder(Uid, Pid);
    }

    // Cancel an order
    @PutMapping("/cancelOrder")
    public ResponseEntity<?> cancelOrder(@RequestParam("order_id") Long Oid)throws NotExist, BadRequest {
        return orderService.cancelOrder(Oid);
    }



}
