package be.heh.reviewservice.service;

import be.heh.reviewservice.model.Review;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.List;

@Service
public class ReviewService {
    private final List<Review> hardcodeRecommendations = List.of(
        new Review(1, 1, "Author 1", "Subject 1", "Content 1"),
        new Review(1, 2, "Author 2", "Subject 2", "Content 2"),
        new Review(1, 3, "Author 3", "Subject 3", "Content 3"),
        new Review(2, 4, "Author 4", "Subject 4", "Content 4"),
        new Review(2, 5, "Author 5", "Subject 5", "Content 5"),
        new Review(3, 6, "Author 6", "Subject 6", "Content 6")
    );

    public Flux<Review> getReviews(int productId) {
        return Flux.fromIterable(hardcodeRecommendations)
                   .filter(review -> review.productId() == productId);
    }
}
