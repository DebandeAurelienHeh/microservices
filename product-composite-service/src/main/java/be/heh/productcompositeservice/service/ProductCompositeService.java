// service/ProductCompositeService.java
package be.heh.productcompositeservice.service;

import be.heh.productcompositeservice.DTOs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductCompositeService {

    private final RestTemplate restTemplate;
    private final String PRODUCT_SERVICE_URL = "http://localhost:7002";
    private final String RECOMMENDATION_SERVICE_URL = "http://localhost:7003";
    private final String REVIEW_SERVICE_URL = "http://localhost:7004";

    @Autowired
    public ProductCompositeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductComposite getProduct(int productId) {
        try {
            // Get product
            Product product = getProductInfo(productId);
            if (product == null) {
                return null;
            }

            // Get recommendations
            List<Recommendation> recommendations = getRecommendations(productId);

            // Get reviews
            List<Review> reviews = getReviews(productId);

            return new ProductComposite(product, recommendations, reviews);
        } catch (Exception ex) {
            log.error("Error creating composite product: {}", ex.getMessage());
            throw ex;
        }
    }

    private Product getProductInfo(int productId) {
        try {
            return restTemplate.getForObject(
                PRODUCT_SERVICE_URL + "/product/{productId}",
                Product.class,
                productId
            );
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw ex;
        } catch (Exception ex) {
            log.error("Error retrieving product: {}", ex.getMessage());
            throw ex;
        }
    }

    private List<Recommendation> getRecommendations(int productId) {
        try {
            Recommendation[] recommendations = restTemplate.getForObject(
                RECOMMENDATION_SERVICE_URL + "/recommendations/{productId}",
                Recommendation[].class,
                productId
            );
            return recommendations != null ? Arrays.asList(recommendations) : Collections.emptyList();
        } catch (Exception ex) {
            log.error("Error retrieving recommendations: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Review> getReviews(int productId) {
        try {
            Review[] reviews = restTemplate.getForObject(
                REVIEW_SERVICE_URL + "/reviews/{productId}",
                Review[].class,
                productId
            );
            return reviews != null ? Arrays.asList(reviews) : Collections.emptyList();
        } catch (Exception ex) {
            log.error("Error retrieving reviews: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }
}