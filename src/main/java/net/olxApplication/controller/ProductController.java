package net.olxApplication.controller;

import net.olxApplication.Entity.Product;
import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import net.olxApplication.RequestBodies.ProductRequestBody;
import net.olxApplication.Interfaces.ProductService;
import net.olxApplication.ResponseBodies.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/olx")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("getAllProducts")
    public List<ProductResponse> getAll() throws RuntimeException {
        return productService.getAll();
    }

    @GetMapping("getUserProducts")
    public ResponseEntity<?> getUserProducts(@RequestParam("userId") Long uId)
            throws RuntimeException
    {
        return productService.getUserProducts(uId);
    }

    @PostMapping("createProduct")
    public ProductResponse createNew(@RequestParam("userId") Long userId,
                                     @RequestBody ProductRequestBody requestBody)
            throws RuntimeException
    {
        return productService.createProduct(userId, requestBody.getName(), requestBody.getPrice());
    }

    @GetMapping("getProduct")
    public ProductResponse getProductById(@RequestParam("prodId") Long id)
            throws RuntimeException
    {
        return productService.getProductById(id);
    }

    @DeleteMapping("deleteProduct")
    public void deleteProduct(@RequestParam("prodId") Long pId,
                              @RequestParam("userId") Long uId)
            throws RuntimeException
    {
        productService.deleteProduct(pId, uId);
    }

    @GetMapping("updateProduct")
    public ProductResponse updateProduct(@RequestParam("prodId") Long id,
                                         @RequestParam("userId") Long uId,
                                         @RequestBody ProductRequestBody prod)
            throws RuntimeException
    {
        return productService.updateProduct(id, uId, prod);
    }

}
