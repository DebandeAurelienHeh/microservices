// service/ProductCompositeService.java
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
    public Mono<ProductComposite> getProduct(int productId) {
        // Lancer les 3 appels en parallèle
        Mono<Product> productMono = getProductInfo(productId);
        Mono<List<Recommendation>> recommendationsMono = getRecommendations(productId).collectList();
        Mono<List<Review>> reviewsMono = getReviews(productId).collectList();

        // Combiner les résultats quand ils sont tous disponibles
        return Mono.zip(productMono, recommendationsMono, reviewsMono)
                .map(tuple -> new ProductComposite(tuple.getT1(), tuple.getT2(), tuple.getT3()))
                .doOnError(ex -> log.error("Error creating composite product: {}", ex.getMessage()));
    }

    private Mono<Product> getProductInfo(int productId) {
        String url = "http://" + productServiceHost + ":" + productServicePort + "/product/" + productId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnError(ex -> log.error("Error retrieving product: {}", ex.getMessage()))
                .onErrorResume(ex -> Mono.empty());
    }

    private Flux<Recommendation> getRecommendations(int productId) {
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation/" + productId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Recommendation.class)
                .doOnError(ex -> log.error("Error retrieving recommendations: {}", ex.getMessage()))
                .onErrorResume(ex -> Flux.empty());
    }

    private Flux<Review> getReviews(int productId) {
        String url = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews/" + productId;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class)
                .doOnError(ex -> log.error("Error retrieving reviews: {}", ex.getMessage()))
                .onErrorResume(ex -> Flux.empty());
    }

    public Mono<Void> deleteProductComposite(int productId) {
        Mono<Void> deleteRecommendationsMono = deleteAllRecommendations(productId);
        Mono<Void> deleteReviewsMono = deleteAllReviewsForProduct(productId);
        Mono<Void> deleteProductMono = deleteProduct(productId);

        return Mono.when(deleteRecommendationsMono, deleteReviewsMono)
                .then(deleteProductMono)
                .doOnError(ex -> log.error("Error deleting composite product: {}", ex.getMessage()));
    }

    private Mono<Void> deleteProduct(int productId) {
        String url = "http://" + productServiceHost + ":" + productServicePort + "/product/" + productId;

        return webClientBuilder.build()
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(ex -> log.error("Error deleting product: {}", ex.getMessage()))
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteAllRecommendations(int productId) {
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation/" + productId;

        return webClientBuilder.build()
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(ex -> log.error("Error deleting recommendations: {}", ex.getMessage()))
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteAllReviewsForProduct(int productId) {
        return getReviews(productId)
                .flatMap(review -> deleteReview(review.getReviewId()))
                .then()
                .doOnError(ex -> log.error("Error deleting reviews for product: {}", ex.getMessage()))
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteRecommendation(int productId, String recommendationId) {
        String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/" + productId + "/recommendation/" + recommendationId;

        return webClientBuilder.build()
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(ex -> log.error("Error deleting recommendation: {}", ex.getMessage()))
                .onErrorResume(ex -> Mono.empty());
    }

    public Mono<Void> deleteReview(int reviewId) {
        String url = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews/" + reviewId;

        return webClientBuilder.build()
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(ex -> log.error("Error deleting review: {}", ex.getMessage()))
                .onErrorResume(ex -> Mono.empty());
    }
}