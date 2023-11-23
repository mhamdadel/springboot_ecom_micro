package ecommercemicroservices.product.service;

import ecommercemicroservices.product.dto.ProductRequest;
import ecommercemicroservices.product.dto.ProductResponse;
import ecommercemicroservices.product.model.Product;
import ecommercemicroservices.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .images(productRequest.getImages())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        return mapToProductResponse(product);
    }

    public void updateProduct(String productId, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " not found"));

        // Update existingProduct fields with productRequest fields
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setImages(productRequest.getImages());
        existingProduct.setPrice(productRequest.getPrice());

        productRepository.save(existingProduct);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    private ProductResponse mapToProductResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .images(p.getImages())
                .price(p.getPrice())
                .build();
    }
}
