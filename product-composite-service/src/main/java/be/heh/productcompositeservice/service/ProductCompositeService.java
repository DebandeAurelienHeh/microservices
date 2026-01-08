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
}