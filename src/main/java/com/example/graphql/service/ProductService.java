package com.example.graphql.service;

import com.example.graphql.domain.Category;
import com.example.graphql.domain.Product;
import com.example.graphql.dto.ProductDTO;
import com.example.graphql.dto.input.PageInput;
import com.example.graphql.dto.input.ProductFilterInput;
import com.example.graphql.dto.input.ProductInput;
import com.example.graphql.mapper.ProductMapper;
import com.example.graphql.repository.CategoryRepository;
import com.example.graphql.repository.ProductRepository;
import graphql.GraphQLException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(ProductFilterInput filter, PageInput pageInput) {
        if (pageInput == null) pageInput = new PageInput();
        Sort sort = Sort.by(
                Sort.Direction.fromString(pageInput.getSortDirection()),
                pageInput.getSortBy()
        );
        PageRequest pageable = PageRequest.of(pageInput.getPage(), pageInput.getSize(), sort);
        var predicate = ProductRepository.buildPredicate(filter);
        return productRepository.findAll(predicate, pageable)
                .map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new GraphQLException("Product not found with id: " + id));
    }

    @Transactional
    public ProductDTO create(ProductInput input) {
        Product product = mapInputToProduct(input, new Product());
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductInput input) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new GraphQLException("Product not found with id: " + id));
        mapInputToProduct(input, product);
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public boolean delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new GraphQLException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        return true;
    }

    private Product mapInputToProduct(ProductInput input, Product product) {
        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setPrice(input.getPrice());
        product.setStock(input.getStock());
        product.setStatus(input.getStatus());
        product.setReleaseDate(input.getReleaseDate());
        if (input.getCategoryId() != null) {
            Category category = categoryRepository.findById(input.getCategoryId())
                    .orElseThrow(() -> new GraphQLException("Category not found with id: " + input.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        return product;
    }
}
