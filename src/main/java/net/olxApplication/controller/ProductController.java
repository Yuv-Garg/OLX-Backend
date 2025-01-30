package net.olxApplication.controller;

import lombok.AllArgsConstructor;
import net.olxApplication.Interfaces.ProductService;
import net.olxApplication.RequestBodies.ProductRequestBody;
import net.olxApplication.ResponseBodies.ProductResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/getAllProducts")
    public Mono<List<ProductResponse>> getAll() throws RuntimeException {
        return productService.getAll();
    }

    @GetMapping("/getUserProducts")
    public Mono<List<ProductResponse>> getUserProducts(@RequestParam("user_id") Long uId)
            throws RuntimeException
    {
        return productService.getUserProducts(uId);
    }

    @PostMapping("/createProduct")
    public Mono<ProductResponse> createNew(@RequestParam("user_id") Long userId,
                                           @RequestBody ProductRequestBody requestBody)
            throws RuntimeException
    {
        return productService.createProduct(userId, requestBody.getName(), requestBody.getPrice());
    }

    @GetMapping("/getProduct")
    public Mono<ProductResponse> getProductById(@RequestParam("prod_id") Long id)
            throws RuntimeException
    {
        return productService.getProductById(id);
    }

    @DeleteMapping("/deleteProduct")
    public void deleteProduct(@RequestParam("prod_id") Long pId,
                              @RequestParam("user_id") Long uId)
            throws RuntimeException
    {
        productService.deleteProduct(pId, uId);
    }

    @GetMapping("/updateProduct")
    public Mono<ProductResponse> updateProduct(@RequestParam("prod_id") Long id,
                                         @RequestParam("user_id") Long uId,
                                         @RequestBody ProductRequestBody prod)
            throws RuntimeException
    {
        return productService.updateProduct(id, uId, prod);
    }

}
