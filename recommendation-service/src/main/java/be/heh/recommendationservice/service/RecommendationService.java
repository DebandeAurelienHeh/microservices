package be.heh.recommendationservice.service;

import be.heh.recommendationservice.mapper.RecommendationMapper;
import be.heh.recommendationservice.model.Recommendation;
import be.heh.recommendationservice.persistence.RecommendationEntity;
import be.heh.recommendationservice.persistence.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final RecommendationRepository repository;
    private final RecommendationMapper mapper;

    public Flux<Recommendation> getRecommendations(int productId) {
        log.info("Retrieving recommendations for productId: {}", productId);

        return repository.findByProductId(productId)
                .map(mapper::entityToApi)
                .doOnComplete(() -> log.info("Recommendations retrieved for productId: {}", productId));
    }

    public Mono<Recommendation> createRecommendation(int productId, Recommendation recommendation) {
        RecommendationEntity entity = mapper.apiToEntity(recommendation);
        entity.setProductId(productId);
        return repository.save(entity)
                .map(mapper::entityToApi)
                .doOnSuccess(r -> log.info("Recommendation created for product {}: {}", productId, r));
    }

    public Mono<Void> deleteRecommendations(int productId) {
        log.info("Deleting recommendations for productId: {}", productId);

        return repository.findByProductId(productId)
                .flatMap(entity -> repository.deleteById(entity.getId()))
                .then()
                .doOnSuccess(v -> log.info("Recommendations deleted for productId: {}", productId));
    }
}