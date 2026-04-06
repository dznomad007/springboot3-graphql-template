package com.example.graphql.mapper;

import com.example.graphql.domain.Product;
import com.example.graphql.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    @Override
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDTO dto);
}
