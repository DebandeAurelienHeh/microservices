package be.heh.productservice.controller;

import be.heh.productservice.model.Product;
import be.heh.productservice.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public Mono<Product> getProduct(@PathVariable int productId) {
        return Mono.just(productService.getProduct(productId));
    }
}
