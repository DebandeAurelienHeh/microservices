package be.heh.productcompositeservice.controller;

import be.heh.productcompositeservice.DTOs.Product;
import be.heh.productcompositeservice.DTOs.Recommendation;
import be.heh.productcompositeservice.DTOs.Review;
import be.heh.productcompositeservice.DTOs.ProductComposite;
import be.heh.productcompositeservice.service.ProductCompositeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product-composite")
@Slf4j
public class ProductCompositeController {

    private final ProductCompositeService productCompositeService;

    @Autowired
    public ProductCompositeController(ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }

    // --- GET ---

    @GetMapping("/{productId}")
    public Mono<ResponseEntity<ProductComposite>> getProductComposite(@PathVariable int productId) {
        return productCompositeService.getProduct(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // --- POST ---

    @PostMapping("/product")
    public Mono<ResponseEntity<Product>> createProduct(@RequestBody Product product) {
        log.info("ProductCompositeController: Request to create product: {}", product.getName());
        return productCompositeService.createProduct(product)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/recommendation")
    public Mono<ResponseEntity<Recommendation>> createRecommendation(@RequestBody Recommendation recommendation) {
        log.info("ProductCompositeController: Request to create recommendation for product: {}", recommendation.productId());
        return productCompositeService.createRecommendation(recommendation)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/review")
    public Mono<ResponseEntity<Review>> createReview(@RequestBody Review review) {
        log.info("ProductCompositeController: Request to create review for product: {}", review.getProductId());
        return productCompositeService.createReview(review)
                .map(ResponseEntity::ok);
    }

    // --- DELETE ---

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<Void>> deleteProductComposite(@PathVariable int productId) {
        return productCompositeService.deleteProductComposite(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/{productId}/recommendation/{recommendationId}")
    public Mono<ResponseEntity<Void>> deleteRecommendation(@PathVariable int productId, @PathVariable String recommendationId) {
        return productCompositeService.deleteRecommendation(productId, recommendationId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/{productId}/recommendations")
    public Mono<ResponseEntity<Void>> deleteAllRecommendationsForProduct(@PathVariable int productId) {
        return productCompositeService.deleteAllRecommendations(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/{productId}/reviews")
    public Mono<ResponseEntity<Void>> deleteAllReviewsForProduct(@PathVariable int productId) {
        return productCompositeService.deleteAllReviewsForProduct(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @DeleteMapping("/review/{reviewId}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable int reviewId) {
        return productCompositeService.deleteReview(reviewId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}