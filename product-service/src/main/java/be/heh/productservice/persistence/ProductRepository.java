package be.heh.productservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, String> {
    // Cette méthode est magique, Spring la comprend automatiquement
    Mono<ProductEntity> findByProductId(int productId);
}