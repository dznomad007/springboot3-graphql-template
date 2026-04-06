package com.example.graphql.resolver;

import com.example.graphql.common.PageResult;
import com.example.graphql.dto.CategoryDTO;
import com.example.graphql.dto.ProductDTO;
import com.example.graphql.dto.input.CategoryInput;
import com.example.graphql.dto.input.PageInput;
import com.example.graphql.mapper.ProductMapper;
import com.example.graphql.repository.ProductRepository;
import com.example.graphql.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryResolver {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @QueryMapping
    public PageResult<CategoryDTO> categories(@Argument PageInput page) {
        return PageResult.from(categoryService.findAll(page));
    }

    @QueryMapping
    public CategoryDTO category(@Argument Long id) {
        return categoryService.findById(id);
    }

    @MutationMapping
    public CategoryDTO createCategory(@Argument CategoryInput input) {
        return categoryService.create(input);
    }

    @MutationMapping
    public CategoryDTO updateCategory(@Argument Long id, @Argument CategoryInput input) {
        return categoryService.update(id, input);
    }

    @MutationMapping
    public boolean deleteCategory(@Argument Long id) {
        return categoryService.delete(id);
    }

    // Lazy-loaded products field for Category type
    @SchemaMapping(typeName = "Category", field = "products")
    public List<ProductDTO> products(CategoryDTO category) {
        return productRepository.findByCategoryId(category.getId())
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
}
