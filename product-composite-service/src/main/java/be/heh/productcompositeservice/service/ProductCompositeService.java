// service/ProductCompositeService.java
package be.heh.productcompositeservice.service;

import be.heh.productcompositeservice.DTOs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.product-service.host}")
    private String productServiceHost;

    @Value("${app.product-service.port}")
    private int productServicePort;

    @Value("${app.review-service.host}")
    private String reviewServiceHost;

    @Value("${app.review-service.port}")
    private int reviewServicePort;

    @Value("${app.recommendation-service.host}")
    private String recommendationServiceHost;

    @Value("${app.recommendation-service.port}")
    private int recommendationServicePort;

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
            String url = "http://" + productServiceHost + ":" + productServicePort + "/product/" + productId;
            return restTemplate.getForObject(url, Product.class);
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
            String url = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendations/" + productId;
            Recommendation[] recommendations = restTemplate.getForObject(url, Recommendation[].class);
            return recommendations != null ? Arrays.asList(recommendations) : Collections.emptyList();
        } catch (Exception ex) {
            log.error("Error retrieving recommendations: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Review> getReviews(int productId) {
        try {
            String url = "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews/" + productId;
            Review[] reviews = restTemplate.getForObject(url, Review[].class);
            return reviews != null ? Arrays.asList(reviews) : Collections.emptyList();
        } catch (Exception ex) {
            log.error("Error retrieving reviews: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }
}