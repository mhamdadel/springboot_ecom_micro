package ecommercemicroservices.product.service;
import ecommercemicroservices.product.dto.ProductRequest;
import ecommercemicroservices.product.dto.ProductResponse;
import ecommercemicroservices.product.model.Product;
import ecommercemicroservices.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService{

    @Autowired
    private ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
//                .images(new String[])
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts () {
        List<Product> products = productRepository.findAll();

        return products
                .stream()
                .map(this::mapToProductResponse)
                .toList();
    }

    private ProductResponse mapToProductResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .build();
    }

}
