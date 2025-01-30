
package net.olxApplication.services;
import lombok.AllArgsConstructor;
import net.olxApplication.Entity.Order;
import net.olxApplication.Entity.Product;
import net.olxApplication.Entity.Transaction;
import net.olxApplication.Entity.User;
import net.olxApplication.Enums.OrderStatus;
import net.olxApplication.Enums.ProductStatus;
import net.olxApplication.Enums.UserStatus;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.OrderService;
import net.olxApplication.ResponseBodies.MessageResponse;
import net.olxApplication.ResponseBodies.OrderResponse;
import net.olxApplication.controller.ConvertResponses;
import net.olxApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ConvertResponses convertResponses;


    @Override
    public List<OrderResponse> getOrdersByUserId(Long id) throws NotExist, BadRequest{
        try{
            User user = userRepository.findById(id).orElseThrow(() -> new NotExist("User Not exist"));
            if (user.getStatus().equals(UserStatus.DeActive)) {
                throw new BadRequest("User Logout");

            }
            List<Order> orders = orderRepository.findByUserId(id);
            List<OrderResponse> orderResponses = new ArrayList<>();
            for(Order order : orders){
                orderResponses.add(convertResponses.covertOrder(order));
            }
            return orderResponses;
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
    public ResponseEntity<MessageResponse> placeOrder(Long userId, Long productId) throws NotExist, BadRequest{

        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User Not exist"));
            Product prod = productRepository.findById(productId).orElseThrow(() -> new NotExist("Product Not exist"));

            if (user.getStatus().equals(UserStatus.DeActive)) {
                throw new BadRequest("User Logout");

            }
            if (prod.getStatus().equals(ProductStatus.Sold)) {
                throw new BadRequest("Product Sold Out ");

            }
            if (userId.equals(prod.getUser().getId())) {
                throw new BadRequest("You cant buy your own product");
            }
            if (user.getWallet().getBalance() < prod.getPrice()) {
                throw new BadRequest("Insufficient Balance");

            }


            prod.setStatus(ProductStatus.Sold);
            productRepository.save(prod);
            user.getWallet().setBalance(user.getWallet().getBalance() - prod.getPrice());
            walletRepository.save(user.getWallet());

            Order order = Order.builder()
                    .user(user)
                    .product(prod)
                    .status(OrderStatus.placed)
                    .orderDate(LocalDateTime.now())
                    .build();

            Transaction transaction =  Transaction.builder()
                    .wallet(user.getWallet())
                    .amount(prod.getPrice())
                    .order(order)
                    .build();
            order.setTransaction(transaction);

            orderRepository.save(order);

            return new ResponseEntity<>(new MessageResponse("Order placed successfull for " + prod.getName() + " and price " + prod.getPrice() + " with orderId  " + order.getId()), HttpStatus.OK);
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
    public ResponseEntity<OrderResponse> cancelOrder(Long orderId) throws NotExist, RuntimeException {
        try{
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotExist("Order Not exist"));
            if(order.getStatus().equals(OrderStatus.canceled)){
                throw new BadRequest("Order already canceled");
            }
            Product prod = order.getProduct();
            prod.setStatus(ProductStatus.UnSold);
            User user = order.getUser();
            user.getWallet().setBalance(user.getWallet().getBalance() + order.getProduct().getPrice());
            walletRepository.save(user.getWallet());


            Transaction transaction = Transaction.builder()
                    .wallet(user.getWallet())
                    .amount(order.getProduct().getPrice())
                    .build();
            transactionRepository.save(transaction);
            order.setStatus(OrderStatus.canceled);
            orderRepository.save(order);

            return new ResponseEntity<>(convertResponses.covertOrder(order), HttpStatus.OK);
        } catch (NotExist e) {
            throw new NotExist(e.getMessage());
        }catch (BadRequest e) {
            throw new BadRequest(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }
}


