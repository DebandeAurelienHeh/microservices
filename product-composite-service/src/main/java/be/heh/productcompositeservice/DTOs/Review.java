package be.heh.productcompositeservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int productId;
    private int reviewId;
    private String author;
    private String subject;
    private String content;
}
