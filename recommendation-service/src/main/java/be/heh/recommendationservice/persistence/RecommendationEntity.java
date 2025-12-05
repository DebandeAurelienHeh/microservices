package be.heh.recommendationservice.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationEntity {

    @Id
    private String id;

    @Version
    private Integer version;

    @Indexed
    private int productId;

    private String author;
    private int rate;
    private String content;
}