package be.heh.recommendationservice;

public record Recommendation(
    Integer productId,
    Integer recommendationId,
    String author,
    Integer rate,
    String content
) {}