// controller/ProductCompositeController.java
package be.heh.productcompositeservice.controller;

import be.heh.productcompositeservice.DTOs.ProductComposite;
import be.heh.productcompositeservice.service.ProductCompositeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product-composite")
@Slf4j
public class ProductCompositeController {

    private final ProductCompositeService productCompositeService;

    @Autowired
    public ProductCompositeController(ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }

    @GetMapping("/{productId}")
    public Mono<ResponseEntity<ProductComposite>> getProductComposite(@PathVariable int productId) {
        log.info("ProductCompositeController: Retrieving composite product for productId: {}", productId);

        return productCompositeService.getProduct(productId)
                .map(composite -> {
                    log.info("ProductCompositeController: Successfully retrieved composite product for productId: {}", productId);
                    return ResponseEntity.ok(composite);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> {
                    if (response.getStatusCode().value() == 404) {
                        log.info("ProductCompositeController: Product with id {} not found", productId);
                    }
                });
    }
}