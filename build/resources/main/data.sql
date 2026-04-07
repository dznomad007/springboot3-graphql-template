-- Sample data for testing Spring Boot 3 + GraphQL Template
-- Automatically loaded on startup (H2 in-memory DB)

INSERT INTO category (created_at, updated_at, name, description)
VALUES
    (CURRENT_TIMESTAMP, null, '전자제품', '스마트폰, 노트북, 태블릿 등 전자기기'),
    (CURRENT_TIMESTAMP, null, '의류', '남성복, 여성복, 아동복 등 의류 제품'),
    (CURRENT_TIMESTAMP, null, '도서', '소설, 기술서적, 자기계발서 등 도서'),
    (CURRENT_TIMESTAMP, null, '스포츠', '운동기구, 스포츠용품, 아웃도어 장비');

INSERT INTO product (created_at, updated_at, name, description, price, stock, status, release_date, category_id)
VALUES
    (CURRENT_TIMESTAMP, null, '삼성 갤럭시 S24 Ultra', '최신 플래그십 스마트폰', 1890000.00, 150, 'ACTIVE', '2024-01-17', 1),
    (CURRENT_TIMESTAMP, null, 'Apple MacBook Pro 14', 'M3 Pro 칩셋 탑재 프리미엄 노트북', 2490000.00, 80, 'ACTIVE', '2023-11-07', 1),
    (CURRENT_TIMESTAMP, null, 'LG 그램 16', '초경량 고성능 노트북', 1690000.00, 45, 'ACTIVE', '2024-02-15', 1),
    (CURRENT_TIMESTAMP, null, 'Sony WH-1000XM5', '업계 최고의 노이즈 캔슬링 헤드폰', 449000.00, 200, 'ACTIVE', '2022-05-12', 1),
    (CURRENT_TIMESTAMP, null, '나이키 에어맥스 2024', '최신 에어맥스 시리즈 운동화', 189000.00, 300, 'ACTIVE', '2024-01-01', 2),
    (CURRENT_TIMESTAMP, null, '아디다스 울트라부스트 23', '편안한 러닝화', 219000.00, 0, 'OUT_OF_STOCK', '2023-03-01', 2),
    (CURRENT_TIMESTAMP, null, '무신사 스탠다드 오버핏 티셔츠', '기본 오버핏 반팔 티셔츠', 29000.00, 500, 'ACTIVE', null, 2),
    (CURRENT_TIMESTAMP, null, '클린 코드', '애자일 소프트웨어 장인 정신 - 로버트 C. 마틴', 33000.00, 120, 'ACTIVE', '2013-12-24', 3),
    (CURRENT_TIMESTAMP, null, '자바 ORM 표준 JPA 프로그래밍', '김영한 저 - JPA 완벽 가이드', 43000.00, 85, 'ACTIVE', '2015-07-28', 3),
    (CURRENT_TIMESTAMP, null, '도메인 주도 개발 시작하기', 'DDD 핵심 개념 정리부터 구현까지', 32000.00, 60, 'ACTIVE', '2022-05-02', 3),
    (CURRENT_TIMESTAMP, null, '나이키 줌 페가수스 40', '전문 러닝화', 149000.00, 0, 'DISCONTINUED', '2023-04-01', 4),
    (CURRENT_TIMESTAMP, null, '윌슨 프로 스태프 97', '테니스 라켓 - 중급자용', 320000.00, 30, 'ACTIVE', '2023-08-01', 4),
    (CURRENT_TIMESTAMP, null, '삼성 갤럭시 탭 S9', '프리미엄 안드로이드 태블릿', 990000.00, 70, 'INACTIVE', '2023-07-26', 1),
    (CURRENT_TIMESTAMP, null, '요가매트 TPE 6mm', '친환경 TPE 소재 요가매트', 35000.00, 180, 'ACTIVE', null, 4);
