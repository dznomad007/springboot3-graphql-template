package com.example.graphql.resolver;

import com.example.graphql.common.PageResult;
import com.example.graphql.dto.ProductDTO;
import com.example.graphql.dto.input.PageInput;
import com.example.graphql.dto.input.ProductFilterInput;
import com.example.graphql.dto.input.ProductInput;
import com.example.graphql.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductResolver {

    private final ProductService productService;

    @QueryMapping
    public PageResult<ProductDTO> products(
            @Argument ProductFilterInput filter,
            @Argument PageInput page) {
        return PageResult.from(productService.findAll(filter, page));
    }

    @QueryMapping
    public ProductDTO product(@Argument Long id) {
        return productService.findById(id);
    }

    @MutationMapping
    public ProductDTO createProduct(@Argument ProductInput input) {
        return productService.create(input);
    }

    @MutationMapping
    public ProductDTO updateProduct(@Argument Long id, @Argument ProductInput input) {
        return productService.update(id, input);
    }

    @MutationMapping
    public boolean deleteProduct(@Argument Long id) {
        return productService.delete(id);
    }

}
