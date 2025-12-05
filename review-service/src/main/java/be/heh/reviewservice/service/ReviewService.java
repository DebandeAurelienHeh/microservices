package be.heh.reviewservice.service;

import be.heh.reviewservice.model.ReviewEntity;
import be.heh.reviewservice.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public Flux<ReviewEntity> getReviews(int productId) {
        return Flux.fromIterable(repository.findByProductId(productId));
    }

    public Mono<ReviewEntity> createReview(ReviewEntity review) {
        return Mono.fromCallable(() -> repository.save(review));
    }

    public Mono<Void> deleteReview(int reviewId) {
        return Mono.fromCallable(() -> {
            repository.deleteById(reviewId);
            return null;
        });
    }
}

