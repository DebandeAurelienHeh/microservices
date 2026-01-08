package be.heh.productcompositeservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {
    private Integer productId;
    private String recommendationId;
    private String author;
    private Integer rate;
    private String content;
}
