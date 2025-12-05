package be.heh.recommendationservice.controller;

import be.heh.recommendationservice.model.Recommendation;
import be.heh.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommendation/{productId}")
    public Flux<Recommendation> getRecommendations(@PathVariable int productId) {
        log.info("GET /recommendation/{}", productId);
        return recommendationService.getRecommendations(productId);
    }

    @PostMapping("/recommendation/{productId}")
    public Mono<Recommendation> createRecommendation(@PathVariable int productId, @RequestBody Recommendation recommendation) {
        log.info("POST /recommendation/{}: {}", productId, recommendation);
        return recommendationService.createRecommendation(productId, recommendation);
    }

    @DeleteMapping("/recommendation/{productId}")
    public Mono<Void> deleteRecommendations(@PathVariable int productId) {
        log.info("DELETE /recommendation/{}", productId);
        return recommendationService.deleteRecommendations(productId);
    }
}