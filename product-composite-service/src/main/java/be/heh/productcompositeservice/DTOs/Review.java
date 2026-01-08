package be.heh.productcompositeservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Integer productId;
    private Integer reviewId;
    private String author;
    private String subject;
    private String content;
}
