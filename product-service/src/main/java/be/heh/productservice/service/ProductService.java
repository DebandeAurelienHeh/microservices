package be.heh.productservice.service;

import be.heh.productservice.model.Product;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final Map<Integer, Product> products = new HashMap<>();

    public ProductService() {
        products.put(1, new Product(1, "Laptop Dell XPS", 2000));
        products.put(2, new Product(2, "Souris Logitech", 100));
        products.put(3, new Product(3, "Clavier mécanique", 500));
        products.put(4, new Product(4, "Écran 27 pouces", 5000));
        products.put(5, new Product(5, "Webcam HD", 300));
    }

    public Product getProduct(int productId) {
        Product product = products.get(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        return product;
    }
}




