package net.olxApplication.services;

import lombok.AllArgsConstructor;
import net.olxApplication.Entity.Product;
import net.olxApplication.Entity.User;
import net.olxApplication.Enums.ProductStatus;
import net.olxApplication.Enums.UserStatus;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.Interfaces.ProductService;
import net.olxApplication.RequestBodies.ProductRequestBody;
import net.olxApplication.ResponseBodies.ProductResponse;
import net.olxApplication.controller.ConvertResponses;
import net.olxApplication.repository.ProductRepository;
import net.olxApplication.repository.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ConvertResponses convertResponses;

    @Override
    public Mono<List<ProductResponse>> getAll() throws RuntimeException {
        try {
            List<Product> products = productRepository.findAll();
            List<ProductResponse> productResponses = new ArrayList<>();
            for (Product prod : products) {
                productResponses.add(convertResponses.covertProduct(prod));
            }

            return Mono.just(productResponses);
        } catch (RuntimeException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<List<ProductResponse>> getUserProducts(Long userId) throws RuntimeException {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User Not exist"));
            if (user.getStatus().equals(UserStatus.DeActive)) {
                return Mono.error(new BadRequest("User Logout"));

            }
            if (user.getProduct().isEmpty()) {
                return Mono.error(new NotExist("There are no products to show"));
            }

            List<Product> products = user.getProduct();
            List<ProductResponse> productResponses = new ArrayList<>();
            for (Product prod : products) {
                productResponses.add(convertResponses.covertProduct(prod));
            }

            return Mono.just(productResponses);
        } catch (RuntimeException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<ProductResponse> createProduct(Long userId, String name, Double price) throws RuntimeException {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User Not exist"));
            if (user.getStatus().equals(UserStatus.DeActive)) {
                throw new BadRequest("User Logout");

            }
            if (name == null || price == null) {
                throw new BadRequest("Name or Price can't be null");
            }
            Product prod = Product.builder().name(name).price(price).build();
            prod.setStatus(ProductStatus.UnSold);
            prod.setUser(user);
            productRepository.save(prod);
            return Mono.just(convertResponses.covertProduct(prod));
        } catch (RuntimeException e) {
            return Mono.error(e);
        }
    }

    @Override
    public Mono<ProductResponse> getProductById(Long id) throws RuntimeException {

        try {
            Product prod = productRepository.findById(id).orElseThrow(() -> new NotExist("Product Not Found"));
            return Mono.just(convertResponses.covertProduct(prod));

        } catch (NotExist e) {
            return Mono.error(e);
        }
    }

    @Override
    public void deleteProduct(Long id, Long userId) throws RuntimeException {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User Not exist"));
            if (user.getStatus().equals(UserStatus.DeActive)) {
                throw new BadRequest("User not Active");
            }
            Product curr = productRepository.findById(id).orElseThrow(() -> new NotExist("Product Not exist"));
            if (curr.getStatus().equals(ProductStatus.Sold)) {
                throw new BadRequest("Product Sold Out");
            }
            if (!curr.getUser().getId().equals(userId)) {
                throw new BadRequest("You can't update this product");
            }
            productRepository.deleteById(id);
        } catch (NotExist e) {
            throw new NotExist(e.getMessage());
        }catch (RuntimeException e) {
            throw new BadRequest(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override

    // extra field bhi postman main pass krdi to uska validation se chk hoga object pr
    // traverse krke ki jo field pass kri hai vo entity k str se match hoti ho
    // abhi k lie y waala exception handle nhi kia

    public Mono<ProductResponse> updateProduct(Long id, Long userId, ProductRequestBody prod) throws RuntimeException {
        try{
            User user = userRepository.findById(userId).orElseThrow(() -> new NotExist("User Not exist"));
            if (user.getStatus().equals(UserStatus.DeActive)) {
                return Mono.error(new BadRequest("User not Active"));
            }
            Product curr = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not exist"));
            if (curr.getStatus().equals(ProductStatus.Sold)) {
                return Mono.error(new BadRequest("Product Sold Out"));
            }
            if (!curr.getUser().getId().equals(userId)) {
                return Mono.error(new BadRequest("You can't update this product"));
            }
            if (prod.getPrice() == null && prod.getName() == null) {
                return Mono.error(new BadRequest("Product Not Updated"));
            }
            if (prod.getPrice() != null) {
                curr.setPrice(prod.getPrice());

            }
            if (prod.getName() != null) {
                curr.setName(prod.getName());

            }
            productRepository.save(curr);
            return Mono.just(convertResponses.covertProduct(curr));
        } catch (RuntimeException e) {
            return Mono.error(e);
        }
    }
}
