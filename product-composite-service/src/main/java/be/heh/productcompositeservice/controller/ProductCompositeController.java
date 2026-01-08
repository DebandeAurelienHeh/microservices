// controller/ProductCompositeController.java
package be.heh.productcompositeservice.controller;

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

    @GetMapping("/{productId}")
    public Mono<ResponseEntity<ProductComposite>> getProductComposite(@PathVariable int productId) {
        log.info("ProductCompositeController: Retrieving composite product for productId: {}", productId);

        return productCompositeService.getProduct(productId)
                .map(composite -> {
                    log.info("ProductCompositeController: Successfully retrieved composite product for productId: {}", productId);
                    return ResponseEntity.ok(composite);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> {
                    if (response.getStatusCode().value() == 404) {
                        log.info("ProductCompositeController: Product with id {} not found", productId);
                    }
                });
    }

    @DeleteMapping("/{productId}")
    public Mono<ResponseEntity<Void>> deleteProductComposite(@PathVariable int productId) {
        log.info("ProductCompositeController: Deleting composite product for productId: {}", productId);

        return productCompositeService.deleteProductComposite(productId)
                .then(Mono.fromCallable(() -> {
                    log.info("ProductCompositeController: Successfully deleted composite product for productId: {}", productId);
                    return ResponseEntity.noContent().build();
                }));
    }

    @DeleteMapping("/{productId}/recommendation/{recommendationId}")
    public Mono<ResponseEntity<Void>> deleteRecommendation(@PathVariable int productId, @PathVariable String recommendationId) {
        log.info("ProductCompositeController: Deleting recommendation {} for productId: {}", recommendationId, productId);

        return productCompositeService.deleteRecommendation(productId, recommendationId)
                .then(Mono.fromCallable(() -> {
                    log.info("ProductCompositeController: Successfully deleted recommendation {} for productId: {}", recommendationId, productId);
                    return ResponseEntity.noContent().build();
                }));
    }

    @DeleteMapping("/{productId}/recommendations")
    public Mono<ResponseEntity<Void>> deleteAllRecommendationsForProduct(@PathVariable int productId) {
        log.info("ProductCompositeController: Deleting all recommendations for productId: {}", productId);

        return productCompositeService.deleteAllRecommendations(productId)
                .then(Mono.fromCallable(() -> {
                    log.info("ProductCompositeController: Successfully deleted all recommendations for productId: {}", productId);
                    return ResponseEntity.noContent().build();
                }));
    }

    @DeleteMapping("/{productId}/reviews")
    public Mono<ResponseEntity<Void>> deleteAllReviewsForProduct(@PathVariable int productId) {
        log.info("ProductCompositeController: Deleting all reviews for productId: {}", productId);

        return productCompositeService.deleteAllReviewsForProduct(productId)
                .then(Mono.fromCallable(() -> {
                    log.info("ProductCompositeController: Successfully deleted all reviews for productId: {}", productId);
                    return ResponseEntity.noContent().build();
                }));
    }

    @DeleteMapping("/review/{reviewId}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable int reviewId) {
        log.info("ProductCompositeController: Deleting review {}", reviewId);

        return productCompositeService.deleteReview(reviewId)
                .then(Mono.fromCallable(() -> {
                    log.info("ProductCompositeController: Successfully deleted review {}", reviewId);
                    return ResponseEntity.noContent().build();
                }));
    }
}