package be.heh.productservice.mapper;

import be.heh.productservice.model.Product;
import be.heh.productservice.persistence.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    ProductEntity apiToEntity(Product api);

    @Mapping(target = "productId", source = "id")
    Product entityToApi(ProductEntity entity);
}