package ecommercemicroservices.product.controller;

import ecommercemicroservices.product.dto.ProductRequest;
import ecommercemicroservices.product.dto.ProductResponse;
import ecommercemicroservices.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping({"", "/"})
    public List<ProductResponse> listAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProductById(@PathVariable Long productId) {
        // Add exception handling for product not found
        return productService.getProductById(String.valueOf(productId));
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@PathVariable Long productId, @RequestBody ProductRequest productRequest) {
        // Add exception handling for product not found
        productService.updateProduct(String.valueOf(productId), productRequest);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@PathVariable Long productId) {
        // Add exception handling for product not found
        productService.deleteProduct(String.valueOf(productId));
    }
}
