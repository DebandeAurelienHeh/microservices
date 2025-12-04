package be.heh.productcompositeservice.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductComposite {
    private Product product;
    private List<Recommendation> recommendations;
    private List<Review> reviews;
}
