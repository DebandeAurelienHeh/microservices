package be.heh.productcompositeservice.service;

import be.heh.productcompositeservice.DTOs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
@Slf4j
public class ProductCompositeService {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.product-service.host}")
    private String productServiceHost;

    @Value("${app.product-service.port}")
    private int productServicePort;

    @Value("${app.review-service.host}")
    private String reviewServiceHost;

    @Value("${app.review-service.port}")
    private int reviewServicePort;

    @Value("${app.recommendation-service.host}")
    private String recommendationServiceHost;

    @Value("${app.recommendation-service.port}")
    private int recommendationServicePort;

    @Autowired
    public ProductCompositeService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // --- GET METHODS ---

    public Mono<ProductComposite> getProduct(int productId) {
        Mono<Product> productMono = getProductInfo(productId);
        Mono<List<Recommendation>> recommendationsMono = getRecommendations(productId).collectList();
        Mono<List<Review>> reviewsMono = getReviews(productId).collectList();

        return Mono.zip(productMono, recommendationsMono, reviewsMono)
                .map(tuple -> new ProductComposite(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .doOnError(ex -> log.error("Error creating composite product: {}", ex.getMessage()));
    }

    private Mono<Product> getProductInfo(int productId) {
        String url = "http://" + productServiceHost + ":" + productServicePort + "/product/" + productId;
        return webClientBuilder.build().get().uri(url).retrieve().bodyToMono(Product.class)
                .onErrorResume(ex -> Mono.empty());
    }

    private Flux<Recommendation> getRecommendations(int productId) {
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation/" + productId;
        return webClientBuilder.build().get().uri(url).retrieve().bodyToFlux(Recommendation.class)
                .onErrorResume(ex -> Flux.empty());
    }

    private Flux<Review> getReviews(int productId) {
        String url = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews/" + productId;
        return webClientBuilder.build().get().uri(url).retrieve().bodyToFlux(Review.class)
                .onErrorResume(ex -> Flux.empty());
    }

    // --- POST METHODS ---

    public Mono<Product> createProduct(Product product) {
        String url = "http://" + productServiceHost + ":" + productServicePort + "/product";
        return webClientBuilder.build()
                .post()
                .uri(url)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnSuccess(p -> log.info("Product created successfully"))
                .doOnError(ex -> log.error("Error creating product: {}", ex.getMessage()));
    }

    public Mono<Recommendation> createRecommendation(Recommendation recommendation) {
        // Extract productId to build the URL expected by recommendation-service
        int productId = recommendation.productId();
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation/" + productId;

        return webClientBuilder.build()
                .post()
                .uri(url)
                .bodyValue(recommendation)
                .retrieve()
                .bodyToMono(Recommendation.class)
                .doOnSuccess(r -> log.info("Recommendation created for product {}", productId))
                .doOnError(ex -> log.error("Error creating recommendation: {}", ex.getMessage()));
    }

    public Mono<Review> createReview(Review review) {
        String url = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews";
        return webClientBuilder.build()
                .post()
                .uri(url)
                .bodyValue(review)
                .retrieve()
                .bodyToMono(Review.class)
                .doOnSuccess(r -> log.info("Review created for product {}", review.getProductId()))
                .doOnError(ex -> log.error("Error creating review: {}", ex.getMessage()));
    }

    // --- DELETE METHODS ---

    public Mono<Void> deleteProductComposite(int productId) {
        return Mono.when(deleteAllRecommendations(productId), deleteAllReviewsForProduct(productId))
                .then(deleteProduct(productId));
    }

    private Mono<Void> deleteProduct(int productId) {
        String url = "http://" + productServiceHost + ":" + productServicePort + "/product/" + productId;
        return webClientBuilder.build().delete().uri(url).retrieve().bodyToMono(Void.class).onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteAllRecommendations(int productId) {
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation/" + productId;
        return webClientBuilder.build().delete().uri(url).retrieve().bodyToMono(Void.class).onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteAllReviewsForProduct(int productId) {
        return getReviews(productId)
                .flatMap(review -> deleteReview(review.getReviewId()))
                .then();
    }

    public Mono<Void> deleteRecommendation(int productId, String recommendationId) {
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/" + productId + "/recommendation/" + recommendationId;
        return webClientBuilder.build().delete().uri(url).retrieve().bodyToMono(Void.class).onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteReview(int reviewId) {
        String url = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews/" + reviewId;
        return webClientBuilder.build().delete().uri(url).retrieve().bodyToMono(Void.class).onErrorResume(ex -> Mono.empty());
    }
}