package net.olxApplication.Interfaces;

import net.olxApplication.Entity.Product;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.RequestBodies.ProductRequestBody;
import net.olxApplication.ResponseBodies.ProductResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Mono<List<ProductResponse>> getAll()throws RuntimeException;

    Mono<ProductResponse> createProduct(Long userId, String name, Double price)throws  RuntimeException;

    Mono<ProductResponse> getProductById(Long id)throws  RuntimeException;

    void deleteProduct(Long id, Long uId)throws  RuntimeException;

    Mono<ProductResponse> updateProduct(Long id, Long uId, ProductRequestBody prod)throws  RuntimeException;

    Mono<List<ProductResponse>> getUserProducts(Long uId)throws RuntimeException;
}
