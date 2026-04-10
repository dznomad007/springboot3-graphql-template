# Spring Boot 3 + GraphQL Template

A backend template project using Spring Boot 3 and GraphQL.
Includes commonly used patterns in production: QueryDSL-based dynamic queries, N+1 problem resolution via `@BatchMapping`, and pagination.

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.3 |
| API | Spring GraphQL |
| ORM | Spring Data JPA + QueryDSL 5.1.0 |
| Database | H2 (in-memory, MySQL compatibility mode) |
| Build | Gradle |
| Utility | Lombok |

## Key Features

- **GraphQL API** - Query / Mutation implementation (Product, Category CRUD)
- **Dynamic Queries** - QueryDSL + `PredicateBuilder` Fluent API
- **N+1 Problem Resolution** - `@BatchMapping` executes a single IN query
- **Pagination** - Consistent page response based on `PageInput` / `PageResult`
- **Extended Scalars** - `BigDecimal`, `Date`, `DateTime`, `Long`
- **Input Validation** - Jakarta Validation (`@NotBlank`, `@NotNull`, etc.)

## Project Structure

```
src/main/java/com/example/graphql/
├── domain/
│   ├── BaseEntity.java          # Common fields (id, createdAt, updatedAt)
│   ├── Category.java
│   ├── Product.java
│   └── enums/
│       └── ProductStatus.java   # ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED
├── dto/
│   ├── CategoryDTO.java
│   ├── ProductDTO.java
│   └── input/
│       ├── CategoryInput.java
│       ├── ProductInput.java
│       ├── ProductFilterInput.java
│       └── PageInput.java
├── repository/
│   ├── CategoryRepository.java
│   └── ProductRepository.java   # QueryDSL Predicate builder
├── service/
│   ├── CategoryService.java
│   └── ProductService.java
├── resolver/
│   ├── CategoryResolver.java    # @BatchMapping applied
│   └── ProductResolver.java
├── common/
│   ├── PredicateBuilder.java    # QueryDSL Fluent builder
│   └── PageResult.java
└── config/
    └── GraphqlConfig.java       # Extended Scalar registration
```

## Getting Started

```bash
./gradlew bootRun
```

After the server starts, access GraphiQL:

```
http://localhost:8080/graphiql
```

GraphQL endpoint:

```
POST http://localhost:8080/graphql
```

## GraphQL Schema Overview

### Query

```graphql
products(filter: ProductFilterInput, page: PageInput): ProductPage
product(id: ID!): Product
categories(page: PageInput): CategoryPage
category(id: ID!): Category
```

### Mutation

```graphql
createProduct(input: ProductInput!): Product
updateProduct(id: ID!, input: ProductInput!): Product
deleteProduct(id: ID!): Boolean

createCategory(input: CategoryInput!): Category
updateCategory(id: ID!, input: CategoryInput!): Category
deleteCategory(id: ID!): Boolean
```

### Filter Example

```graphql
query {
  products(
    filter: {
      name: "MacBook"
      minPrice: 1000000
      maxPrice: 5000000
      status: [ACTIVE]
      categoryId: 1
    }
    page: { page: 0, size: 10, sortBy: "price", sortDirection: "DESC" }
  ) {
    content {
      id
      name
      price
      status
      category { name }
    }
    totalElements
    totalPages
  }
}
```

### N+1 Resolution Example (@BatchMapping)

```graphql
query {
  categories(page: { page: 0, size: 10 }) {
    content {
      id
      name
      products {        # @BatchMapping: handled with a single IN query
        id
        name
        price
      }
    }
  }
}
```

## HTTP Test Files

The `http/` directory contains test files for IntelliJ HTTP Client.

```
http/
├── category.http          # Category CRUD tests
├── product.http           # Product CRUD + filter tests
└── http-client.env.json   # Environment variables (baseUrl)
```

## Initial Data

Sample data is automatically loaded via `data.sql` on application startup.

- 4 categories: 전자제품, 의류, 도서, 스포츠
- 14 products: 삼성 갤럭시, MacBook Pro, 나이키, etc.
