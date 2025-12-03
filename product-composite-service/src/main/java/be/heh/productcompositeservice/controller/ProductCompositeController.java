// controller/ProductCompositeController.java
package be.heh.productcompositeservice.controller;

import be.heh.productcompositeservice.DTOs.ProductComposite;
import be.heh.productcompositeservice.service.ProductCompositeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductComposite> getProductComposite(@PathVariable int productId) {
        log.info("ProductCompositeController: Retrieving composite product for productId: {}", productId);
        
        ProductComposite composite = productCompositeService.getProduct(productId);
        if (composite == null) {
            log.info("ProductCompositeController: Product with id {} not found", productId);
            return ResponseEntity.notFound().build();
        }

        log.info("ProductCompositeController: Successfully retrieved composite product for productId: {}", productId);
        return ResponseEntity.ok(composite);
    }
}