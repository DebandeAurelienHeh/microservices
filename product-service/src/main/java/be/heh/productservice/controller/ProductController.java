package be.heh.productservice.controller;

import be.heh.productservice.model.Product;
import be.heh.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public Mono<Product> getProduct(@PathVariable int productId) {
        log.info("GET /product/{}", productId);
        return productService.getProduct(productId);
    }

    @GetMapping("/products")
    public Flux<Product> getAllProducts() {
        log.info("GET /product");
        return productService.getAllProducts();
    }

    @PostMapping("/product")
    public Mono<Product> createProduct(@RequestBody Product product) {
        log.info("POST /product: {}", product);
        return productService.createProduct(product);
    }

    @DeleteMapping("/product/{productId}")
    public Mono<Void> deleteProduct(@PathVariable int productId) {
        log.info("DELETE /product/{}", productId);
        return productService.deleteProduct(productId);
    }
}

