package be.heh.productservice.service;

import be.heh.productservice.mapper.ProductMapper;
import be.heh.productservice.model.Product;
import be.heh.productservice.persistence.ProductEntity;
import be.heh.productservice.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final SequenceGeneratorService sequenceGenerator; // On injecte le générateur

    public Mono<Product> createProduct(Product product) {
        return sequenceGenerator.generateSequence("products_sequence") // 1. On demande le prochain ID
                .flatMap(sequenceId -> {
                    ProductEntity entity = mapper.apiToEntity(product);
                    entity.setProductId(sequenceId); // 2. On l'assigne à l'entité
                    return repository.save(entity);  // 3. On sauvegarde
                })
                .map(mapper::entityToApi);
    }

    public Mono<Product> getProduct(int productId) {
        // Attention : Utilise bien findByProductId et non findById
        return repository.findByProductId(productId)
                .map(mapper::entityToApi);
    }

    public Mono<Void> deleteProduct(int productId) {
        return repository.findByProductId(productId)
                .flatMap(repository::delete);
    }
}