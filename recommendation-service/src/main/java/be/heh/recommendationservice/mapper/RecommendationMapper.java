package be.heh.recommendationservice.mapper;

import be.heh.recommendationservice.model.Recommendation;
import be.heh.recommendationservice.persistence.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    RecommendationEntity apiToEntity(Recommendation api);

    @Mapping(target = "recommendationId", source = "id")
    @Mapping(target = "productId", source = "productId")
    Recommendation entityToApi(RecommendationEntity entity);
}
