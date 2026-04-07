package com.example.graphql.service;

import com.example.graphql.domain.Category;
import com.example.graphql.dto.CategoryDTO;
import com.example.graphql.dto.input.CategoryInput;
import com.example.graphql.dto.input.PageInput;
import com.example.graphql.repository.CategoryRepository;
import graphql.GraphQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(PageInput pageInput) {
        if (pageInput == null) pageInput = new PageInput();
        Sort sort = Sort.by(
                Sort.Direction.fromString(pageInput.getSortDirection()),
                pageInput.getSortBy()
        );
        PageRequest pageable = PageRequest.of(pageInput.getPage(), pageInput.getSize(), sort);
        return categoryRepository.findAll(pageable).map(CategoryDTO::from);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryDTO::from)
                .orElseThrow(() -> new GraphQLException("Category not found with id: " + id));
    }

    @Transactional
    public CategoryDTO create(CategoryInput input) {
        Category category = Category.builder()
                .name(input.getName())
                .description(input.getDescription())
                .build();
        return CategoryDTO.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryInput input) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Category not found with id: " + id));
        category.setName(input.getName());
        category.setDescription(input.getDescription());
        return CategoryDTO.from(categoryRepository.save(category));
    }

    @Transactional
    public boolean delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new GraphQLException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
