package com.example.graphql.dto.input;

import com.example.graphql.domain.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProductFilterInput {
    private String name;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<ProductStatus> status;
    private Long categoryId;
    private LocalDate releaseDateFrom;
    private LocalDate releaseDateTo;
}
