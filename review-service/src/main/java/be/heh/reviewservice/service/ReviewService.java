package be.heh.reviewservice.service;

import be.heh.reviewservice.model.Review;
import be.heh.reviewservice.model.ReviewEntity;
import be.heh.reviewservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public Flux<Review> getReviews(int productId) {
        return Flux.defer(() -> Flux.fromIterable(repository.findByProductId(productId)))
                .map(this::toModel)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Review toModel(ReviewEntity e) {
        return new Review(e.getProductId(), e.getReviewId(), e.getAuthor(), e.getSubject(), e.getContent());
    }
}
