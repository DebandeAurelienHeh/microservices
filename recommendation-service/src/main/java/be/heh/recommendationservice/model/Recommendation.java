package be.heh.recommendationservice.model;

public record Recommendation(
    Integer productId,
    String recommendationId,
    String author,
    Integer rate,
    String content
) {}