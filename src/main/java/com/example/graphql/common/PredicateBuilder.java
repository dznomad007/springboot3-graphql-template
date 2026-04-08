package com.example.graphql.common;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.function.Function;

/**
 * QueryDSL BooleanBuilder를 플루언트 API로 감싸는 유틸리티.
 *
 * <pre>
 * QProduct p = QProduct.product;
 * BooleanBuilder predicate = new PredicateBuilder()
 *     .ifHasText(filter.getName(),        p.name)
 *     .ifPresent(filter.getMinPrice(),    p.price::goe)
 *     .ifPresent(filter.getMaxPrice(),    p.price::loe)
 *     .ifNotEmpty(filter.getStatus(),     p.status::in)
 *     .build();
 * </pre>
 */
public class PredicateBuilder {

    private final BooleanBuilder builder = new BooleanBuilder();

    /** null·공백 문자열 무시, 일치하는 경우 containsIgnoreCase 적용 */
    public PredicateBuilder ifHasText(String value, StringPath path) {
        if (StringUtils.hasText(value)) {
            builder.and(path.containsIgnoreCase(value));
        }
        return this;
    }

    /** null 값 무시, non-null 이면 expression 함수를 적용 */
    public <T> PredicateBuilder ifPresent(T value, Function<T, BooleanExpression> expression) {
        if (value != null) {
            builder.and(expression.apply(value));
        }
        return this;
    }

    /** null·빈 컬렉션 무시, 원소가 있으면 expression 함수를 적용 */
    public <T> PredicateBuilder ifNotEmpty(Collection<T> values, Function<Collection<T>, BooleanExpression> expression) {
        if (!CollectionUtils.isEmpty(values)) {
            builder.and(expression.apply(values));
        }
        return this;
    }

    public BooleanBuilder build() {
        return builder;
    }
}
