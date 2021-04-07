--------------------------------------------
 SQL sample data
--------------------------------------------
/* 기존 데이터 리셋 */

DELETE FROM customer ;
DELETE FROM category;
DELETE FROM product;
DELETE FROM orders;
DELETE FROM shipment;
DELETE FROM shipment_company;

/* customer */

customer_insert_version2

INSERT INTO customer VALUES (1, 'Cho', 'Cho', 'Suwon', 010-1234-5678 , 1000, 100);
INSERT INTO customer VALUES (2, 'Kim', 'Kim', 'Seoul', 010-1234-8765 , 2000, 200);
INSERT INTO customer VALUES (3, 'Lee', 'Lee', 'Busan', 010-9876-5432 , 10000, 1000);
INSERT INTO customer VALUES (4, 'Choi', 'Choi', 'Seoul', 010-2222-3333 , 3000, 300);
INSERT INTO customer VALUES (5, 'Sohn', 'Sohn', 'Seoul', 010-1111-5555, 6000, 600);

/* product */

INSERT INTO product VALUES (1, 2, 'OO브랜드 T-shirt', '사두고 안입은 신상', 500, 1, '의류', 'READY', 1,-1);
INSERT INTO product VALUES (2, 3, 'ipad 에어 4 ', 'ipad air4 256GB 저렴하게 팔아요', 5000, 3, '디지털', 'READY', 2,-1);
INSERT INTO product VALUES (3, 4, '잠옷', '촉감 대박 잠옷 ㅍㅍ', 300, 1, '의류', 'READY', 3,-1);
INSERT INTO product VALUES (4, 5, '책상의자', '아이들 책상에 딱이에요. 연락주세요.', 1200, 2, '가구', 'READY', 4,-1);
INSERT INTO product VALUES (5, 1, '무선 충전기', '충전 빠르고 무선이라 편해요!', 1800, 3, '디지털', 'READY', 5,-1);
INSERT INTO product VALUES (6, 2, '이것이 자바다', '이것이 자바다 1, 2권 세트로 팜', 800, 4, '도서', 'READY', 6,-1);
INSERT INTO product VALUES (7, 3, '청바지', '한번 입고 안입은 청바지 팔아여', 1400, 1, '의류', 'READY', 7,-1);
INSERT INTO product VALUES (8, 4, '프린트 원피스', '사이즈 F 입니당', 1000, 1, '의류', 'READY', 8,-1);


/* category */

INSERT INTO category VALUES (1, '의류');
INSERT INTO category VALUES (2, '가구');
INSERT INTO category VALUES (3, '디지털');
INSERT INTO category VALUES (4, '도서');


/* orders */

INSERT INTO orders VALUES (1, sysdate, 2, 1, 2);
INSERT INTO orders VALUES (2, sysdate-10, 3, 3, 4);
INSERT INTO orders VALUES (3, sysdate-7, 4, 5, 1);
INSERT INTO orders VALUES (4, sysdate-3, 4, 6, 2);


/* shipment */

INSERT INTO shipment VALUES (1, 2, 1, 2);
INSERT INTO shipment VALUES (2, 3, 2, 3);
INSERT INTO shipment VALUES (3, 4, 3, 4);
INSERT INTO shipment VALUES (4, 5, 4, 5);
INSERT INTO shipment VALUES (5, 1, 5, 1);
INSERT INTO shipment VALUES (6, 2, 6, 3);
INSERT INTO shipment VALUES (7, 3, 7, 4);
INSERT INTO shipment VALUES (8, 4, 8, 5);

/* shipment company */

INSERT INTO shipment_company VALUES (1, '한진');
INSERT INTO shipment_company VALUES (2, '롯데');
INSERT INTO shipment_company VALUES (3, 'CJ대한통운');
INSERT INTO shipment_company VALUES (4, '로젠택배');
INSERT INTO shipment_company VALUES (5, '우체국');


 