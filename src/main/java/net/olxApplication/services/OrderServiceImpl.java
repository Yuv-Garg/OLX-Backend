
package net.olxApplication.services;
import net.olxApplication.Entity.Order;
import net.olxApplication.Entity.Product;
import net.olxApplication.Entity.Transaction;
import net.olxApplication.Entity.User;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.OrderService;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public WalletRepository walletRepository;

    @Autowired
    public TransactionRepository transactionRepository;


    @Override
    public ResponseEntity<?> getOrdersByUserId(Long id) throws NotExist, BadRequest{
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User Not exist"));
            if (user.getStatus().equals("DeActive")) {
                throw new BadRequest("User Logout");

            }
            List<Order> orders = orderRepository.findByUserId(id);
//            if(orders.isEmpty()) {
//                return new ResponseEntity<>(new MessageResponse("No orders Found"), HttpStatus.OK);
//            }
            List<OrderResponse> orderResponses = orders.stream().map(order -> {
                OrderResponse dto = new OrderResponse();
                dto.setStatus(order.getStatus());
                dto.setProductName(order.getProduct().getName());
                dto.setPrice(order.getProduct().getPrice());
                return dto;
            }).toList();
            return new ResponseEntity<>(orderResponses, HttpStatus.OK);
        } catch (BadRequest e) {
            throw new BadRequest(e.getMessage());
        }catch (NotExist e) {
            throw new NotExist(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> placeOrder(Long userId, Long productId) throws NotExist, BadRequest{

        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User Not exist"));
            Product prod = productRepository.findById(productId).orElseThrow(() -> new NotExist("Product Not exist"));

            if (prod.getStatus().equals("Sold")) {
                throw new BadRequest("Product Sold Out ");

            }
            if (user.getStatus().equals("DeActive")) {
                throw new BadRequest("User Logout");

            }
            if (userId.equals(prod.getUser().getId())) {
                throw new BadRequest("You cant buy your own product");
            }
            if (user.getWallet().getBalance() < prod.getPrice()) {
                throw new BadRequest("Insufficient Balance");

            }


            prod.setStatus("Sold");
            productRepository.save(prod);
            user.getWallet().setBalance(user.getWallet().getBalance() - prod.getPrice());
            walletRepository.save(user.getWallet());

            Order order = Order.builder()
                    .user(user)
                    .product(prod)
                    .status("placed")
                    .build();

            Transaction transaction =  Transaction.builder()
                    .wallet(user.getWallet())
                    .amount(prod.getPrice())
                    .order(order)
                    .build();
            order.setTransaction(transaction);

            orderRepository.save(order);

            return new ResponseEntity<>(new MessageResponse("Order placed successfull for " + prod.getName() + " and price " + prod.getPrice()), HttpStatus.OK);
        } catch (BadRequest e) {
            throw new BadRequest(e.getMessage());
        } catch (NotExist e) {
            throw new NotExist(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public ResponseEntity<?> cancelOrder(Long orderId) throws NotExist, BadRequest {
        try{
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotExist("Order Not exist"));

            Product prod = order.getProduct();
            prod.setStatus("UnSold");
            User user = order.getUser();
            user.getWallet().setBalance(user.getWallet().getBalance() + order.getProduct().getPrice());
            walletRepository.save(user.getWallet());


            Transaction transaction = Transaction.builder()
                    .wallet(user.getWallet())
                    .amount(order.getProduct().getPrice())
                    .build();
            transactionRepository.save(transaction);

            OrderResponse orderResponse = OrderResponse.builder()
                    .status("canceled")
                    .productName(order.getProduct().getName())
                    .price(order.getProduct().getPrice())
                    .build();
            order.setStatus("canceled");
            orderRepository.save(order);

            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (NotExist e) {
            throw new NotExist(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }
}


