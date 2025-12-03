package be.heh.productservice.service;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String s) {
        super(s);
    }
}
