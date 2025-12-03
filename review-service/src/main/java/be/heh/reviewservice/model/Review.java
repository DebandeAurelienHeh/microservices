package be.heh.reviewservice.model;

public record Review (
    Integer productId,
    Integer reviewId,
    String author,
    String subject,
    String content
) {}