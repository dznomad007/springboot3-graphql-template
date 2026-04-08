package com.example.graphql.repository;

import com.example.graphql.common.PredicateBuilder;
import com.example.graphql.domain.Product;
import com.example.graphql.domain.QProduct;
import com.example.graphql.dto.input.ProductFilterInput;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /** @BatchMapping 전용: 여러 categoryId를 한 번의 IN 쿼리로 조회 */
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.id IN :categoryIds")
    List<Product> findByCategoryIdIn(@Param("categoryIds") Collection<Long> categoryIds);

    static BooleanBuilder buildPredicate(ProductFilterInput filter) {
        if (filter == null) return new BooleanBuilder();
        QProduct p = QProduct.product;
        return new PredicateBuilder()
                .ifHasText(filter.getName(),             p.name)
                .ifHasText(filter.getDescription(),      p.description)
                .ifPresent(filter.getMinPrice(),         p.price::goe)
                .ifPresent(filter.getMaxPrice(),         p.price::loe)
                .ifNotEmpty(filter.getStatus(),          p.status::in)
                .ifPresent(filter.getCategoryId(),       p.category.id::eq)
                .ifPresent(filter.getReleaseDateFrom(),  p.releaseDate::goe)
                .ifPresent(filter.getReleaseDateTo(),    p.releaseDate::loe)
                .build();
    }
}
