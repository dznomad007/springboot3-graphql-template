package com.example.graphql.mapper;

import com.example.graphql.domain.Category;
import com.example.graphql.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

    @Override
    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryDTO dto);
}
