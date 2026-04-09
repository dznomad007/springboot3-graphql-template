# Spring Boot 3 + GraphQL Template

Spring Boot 3와 GraphQL을 활용한 백엔드 템플릿 프로젝트입니다.
QueryDSL 기반 동적 쿼리, `@BatchMapping`을 통한 N+1 문제 해결, 페이지네이션 등 실무에서 자주 사용하는 패턴을 포함합니다.

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.2.3 |
| API | Spring GraphQL |
| ORM | Spring Data JPA + QueryDSL 5.1.0 |
| Database | H2 (인메모리, MySQL 호환 모드) |
| Build | Gradle |
| Utility | Lombok |

## 주요 기능

- **GraphQL API** - Query / Mutation 구현 (상품, 카테고리 CRUD)
- **동적 쿼리** - QueryDSL + `PredicateBuilder` Fluent API
- **N+1 문제 해결** - `@BatchMapping`으로 IN 쿼리 1회 처리
- **페이지네이션** - `PageInput` / `PageResult` 기반 일관된 페이지 응답
- **확장 Scalar** - `BigDecimal`, `Date`, `DateTime`, `Long`
- **입력 검증** - Jakarta Validation (`@NotBlank`, `@NotNull` 등)

## 프로젝트 구조

```
src/main/java/com/example/graphql/
├── domain/
│   ├── BaseEntity.java          # 공통 필드 (id, createdAt, updatedAt)
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
│   └── ProductRepository.java   # QueryDSL Predicate 빌드
├── service/
│   ├── CategoryService.java
│   └── ProductService.java
├── resolver/
│   ├── CategoryResolver.java    # @BatchMapping 적용
│   └── ProductResolver.java
├── common/
│   ├── PredicateBuilder.java    # QueryDSL Fluent 빌더
│   └── PageResult.java
└── config/
    └── GraphqlConfig.java       # Extended Scalar 등록
```

## 실행 방법

```bash
./gradlew bootRun
```

서버 시작 후 GraphiQL 접속:

```
http://localhost:8080/graphiql
```

GraphQL 엔드포인트:

```
POST http://localhost:8080/graphql
```

## GraphQL 스키마 요약

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

### 필터 예시

```graphql
query {
  products(
    filter: {
      name: "맥북"
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

### N+1 해결 예시 (@BatchMapping)

```graphql
query {
  categories(page: { page: 0, size: 10 }) {
    content {
      id
      name
      products {        # @BatchMapping: IN 쿼리 1회로 처리
        id
        name
        price
      }
    }
  }
}
```

## HTTP 테스트 파일

`http/` 디렉터리에 IntelliJ HTTP Client용 테스트 파일이 포함되어 있습니다.

```
http/
├── category.http          # 카테고리 CRUD 테스트
├── product.http           # 상품 CRUD + 필터링 테스트
└── http-client.env.json   # 환경변수 (baseUrl)
```

## 초기 데이터

애플리케이션 시작 시 `data.sql`로 샘플 데이터가 자동 로드됩니다.

- 카테고리 4개: 전자제품, 의류, 도서, 스포츠
- 상품 14개: 삼성 갤럭시, MacBook Pro, 나이키 등