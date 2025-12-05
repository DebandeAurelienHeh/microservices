package be.heh.reviewservice.controller;

import be.heh.reviewservice.model.ReviewEntity;
import be.heh.reviewservice.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // GET reviews by productId
    @GetMapping("/{productId}")
    public Flux<ReviewEntity> getReviews(@PathVariable int productId) {
        return reviewService.getReviews(productId);
    }

    // POST a new review
    @PostMapping
    public Mono<ReviewEntity> createReview(@RequestBody ReviewEntity review) {
        return reviewService.createReview(review);
    }

    // DELETE a review by reviewId
    @DeleteMapping("/{reviewId}")
    public Mono<Void> deleteReview(@PathVariable int reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
