package com.example.graphql.dto.input;

import lombok.Data;

@Data
public class PageInput {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}
