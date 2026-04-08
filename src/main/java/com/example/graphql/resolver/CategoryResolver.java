package com.example.graphql.resolver;

import com.example.graphql.common.PageResult;
import com.example.graphql.dto.CategoryDTO;
import com.example.graphql.dto.ProductDTO;
import com.example.graphql.dto.input.CategoryInput;
import com.example.graphql.dto.input.PageInput;
import com.example.graphql.repository.ProductRepository;
import com.example.graphql.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CategoryResolver {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;

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

    /**
     * @BatchMapping: 현재 요청에서 조회된 모든 CategoryDTO를 한 번에 받아
     * IN 쿼리 1번으로 처리 → N+1 문제 해결
     *
     * categories { products { ... } } 쿼리 시
     * - @SchemaMapping : category 수 만큼 쿼리 N번 발생
     * - @BatchMapping  : 쿼리 1번 (WHERE category_id IN (1,2,3,4))
     */
    @BatchMapping(typeName = "Category", field = "products")
    public Map<CategoryDTO, List<ProductDTO>> products(List<CategoryDTO> categories) {
        Set<Long> ids = categories.stream()
                .map(CategoryDTO::getId)
                .collect(Collectors.toSet());

        Map<Long, List<ProductDTO>> byCategory = productRepository.findByCategoryIdIn(ids)
                .stream()
                .map(ProductDTO::from)
                .collect(Collectors.groupingBy(dto -> dto.getCategory().getId()));

        return categories.stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> byCategory.getOrDefault(c.getId(), List.of())
                ));
    }
}
