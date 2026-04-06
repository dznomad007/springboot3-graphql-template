package com.example.graphql.repository;

import com.example.graphql.domain.Product;
import com.example.graphql.domain.QProduct;
import com.example.graphql.dto.input.ProductFilterInput;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    static BooleanBuilder buildPredicate(ProductFilterInput filter) {
        BooleanBuilder builder = new BooleanBuilder();
        if (filter == null) return builder;

        QProduct product = QProduct.product;

        if (StringUtils.hasText(filter.getName())) {
            builder.and(product.name.containsIgnoreCase(filter.getName()));
        }
        if (StringUtils.hasText(filter.getDescription())) {
            builder.and(product.description.containsIgnoreCase(filter.getDescription()));
        }
        if (filter.getMinPrice() != null) {
            builder.and(product.price.goe(filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            builder.and(product.price.loe(filter.getMaxPrice()));
        }
        if (!CollectionUtils.isEmpty(filter.getStatus())) {
            builder.and(product.status.in(filter.getStatus()));
        }
        if (filter.getCategoryId() != null) {
            builder.and(product.category.id.eq(filter.getCategoryId()));
        }
        if (filter.getReleaseDateFrom() != null) {
            builder.and(product.releaseDate.goe(filter.getReleaseDateFrom()));
        }
        if (filter.getReleaseDateTo() != null) {
            builder.and(product.releaseDate.loe(filter.getReleaseDateTo()));
        }

        return builder;
    }
}
