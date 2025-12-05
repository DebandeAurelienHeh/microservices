package be.heh.reviewservice.repository;

import be.heh.reviewservice.model.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    List<ReviewEntity> findByProductId(Integer productId);
}
