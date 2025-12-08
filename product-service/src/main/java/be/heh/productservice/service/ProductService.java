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
    private final SequenceGeneratorService sequenceGenerator;

    public Mono<Product> createProduct(Product product) {
        return sequenceGenerator.generateSequence("products_sequence")  // Generate a new sequence ID
                .flatMap(sequenceId -> {
                    ProductEntity entity = mapper.apiToEntity(product);
                    entity.setProductId(sequenceId);                            // Set the generated sequence ID
                    return repository.save(entity);
                })
                .map(mapper::entityToApi);                                      // Convert saved entity back to API model
    }

    public Mono<Product> getProduct(int productId) {
        return repository.findByProductId(productId)
                .map(mapper::entityToApi);
    }

    public Mono<Void> deleteProduct(int productId) {
        return repository.findByProductId(productId)
                .flatMap(repository::delete);
    }
}