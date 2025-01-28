package net.olxApplication.controller;

import net.olxApplication.Entity.Order;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.OrderService;
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
    public ResponseEntity<?> getOrders(@RequestParam("userId") Long id) throws NotExist, BadRequest {
        return orderService.getOrdersByUserId(id);
    }

    // Place an order
    @PostMapping("placeOrder")
    public ResponseEntity<?> placeOrder(@RequestParam("userId") Long Uid, @RequestParam("prodId") Long Pid)throws NotExist, BadRequest {
        return orderService.placeOrder(Uid, Pid);
    }

    // Cancel an order
    @PutMapping("/cancelOrder")
    public ResponseEntity<?> cancelOrder(@RequestParam("orderId") Long Oid)throws NotExist, BadRequest {
        return orderService.cancelOrder(Oid);
    }



}
