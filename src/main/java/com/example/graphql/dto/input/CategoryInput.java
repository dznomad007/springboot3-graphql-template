package com.example.graphql.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryInput {
    @NotBlank
    private String name;
    private String description;
}
