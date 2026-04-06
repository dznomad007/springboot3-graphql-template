package com.example.graphql.dto.input;

import com.example.graphql.domain.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductInput {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    @NotNull
    @PositiveOrZero
    private Integer stock;
    @NotNull
    private ProductStatus status;
    private LocalDate releaseDate;
    private Long categoryId;
}
