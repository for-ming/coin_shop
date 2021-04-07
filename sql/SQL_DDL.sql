--------------------------------------------
 SQL : DDL
					update 04/04 16:00
--------------------------------------------

/* 기존 테이블 삭제 */
DROP TABLE customer CASCADE CONSTRAINTS;
DROP TABLE product CASCADE CONSTRAINTS;
DROP TABLE shipment CASCADE CONSTRAINTS;
DROP TABLE shipment_company CASCADE CONSTRAINTS;
DROP TABLE orders CASCADE CONSTRAINTS;
DROP TABLE category CASCADE CONSTRAINTS;

/* 기존 시퀀스 삭제 */
drop sequence customer_id_seq;
drop sequence product_id_seq;
drop sequence ORDERS_id_seq;
drop sequence shipment_id_seq;
drop sequence shipment_company_id_seq;
drop sequence category_id_seq;

/* 시퀀스 생성 */

CREATE SEQUENCE customer_id_seq
START WITH 1
INCREMENT BY 1
MAXVALUE 999999
MINVALUE 1
NOCYCLE;

CREATE SEQUENCE product_id_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 999999999
NOCYCLE;


CREATE SEQUENCE ORDERS_id_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 999999999
NOCYCLE;


CREATE SEQUENCE shipment_id_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 999999999
NOCYCLE;


CREATE SEQUENCE shipment_company_id_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 999999999
NOCYCLE;

CREATE SEQUENCE category_id_seq
INCREMENT BY 1
START WITH 1
MINVALUE 1
MAXVALUE 999999999
NOCYCLE;

/* 테이블 생성 */

set sqlblanklines on 

-- 카테고리 테이블

CREATE TABLE category (
    id    NUMBER(9) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);

CREATE INDEX category_id_idx
ON category(id);

ALTER TABLE category
ADD CONSTRAINT category_id_pk PRIMARY KEY(id);

-- 고객 테이블

CREATE TABLE customer (
    id                      NUMBER(9) NOT NULL,
    name                    VARCHAR2(20 CHAR) NOT NULL,
    password                VARCHAR2(20 CHAR) NOT NULL,
    zipcode                 VARCHAR2(50 CHAR) NOT NULL,
    phone_number            VARCHAR2(14 CHAR) NOT NULL,
    coin                    NUMBER(9) NOT NULL,
    volunteer_working_time  NUMBER(9) NOT NULL
);


CREATE INDEX customer_id_idx
ON customer(id);

ALTER TABLE customer
ADD CONSTRAINT customer_id_pk PRIMARY KEY(id);

-- 배송 업체 테이블

CREATE TABLE shipment_company (
    id    NUMBER(9) NOT NULL,
    name  VARCHAR2(20 CHAR) NOT NULL
)
LOGGING;

ALTER TABLE shipment_company ADD CONSTRAINT shipment_company_id_pk PRIMARY KEY ( id );

-- 상품 테이블

create table PRODUCT (
  id       number(9) NOT NULL,
  customer_id      number(9) NOT NULL,
  name             varchar2(50) NOT NULL,
  information       varchar2(200 CHAR),
  price            number(9) NOT NULL,
  category_id      number(9) NOT NULL,
  category_name    varchar2(50) NOT NULL, --
  product_status   VARCHAR2(10 CHAR) NOT NULL, --판매 상태 정보 추가 (READY,PROGRESS,FINISH)
  shipment_id      number(9) NOT NULL,
  buy_customer_id  number(9)   -- 구매자 정보 추가
  )
  
partition by list(category_id)
(
  --partition P_CLOTHING    values ('clothing','fashion'),
  partition P_CLOTHING    values (1,2),
  --partition P_DIGITAL    values ('digital','home appliances'),
  partition P_DIGITAL    values (3,4),
  --partition P_FURNITURE   values ('furniture','interior'),
  partition P_FURNITURE   values (5,6),
  --partition P_BOOK    values ('book,ticket'),
  partition P_BOOK    values (7,8),
  --partition P_ETC    values ('etc','unknown')
  partition P_ETC    values (9,10)
  --partition P_ETC values (default)
);

CREATE INDEX product_id_idx
ON product(id);

CREATE INDEX product_customer_id_idx
ON product(customer_id);

CREATE INDEX product_shipment_id_idx
ON product(shipment_id);

CREATE INDEX product_category_id_idx
ON product(category_id);

ALTER TABLE product 
ADD CONSTRAINT product_id_customer_id_pk PRIMARY KEY(id, customer_id);

ALTER TABLE product
ADD CONSTRAINT product_shipment_id_nn
CHECK(shipment_id IS NOT NULL);

ALTER TABLE product 
ADD CONSTRAINT product_shipment_id_uk UNIQUE(shipment_id);

ALTER TABLE product
ADD CONSTRAINT product_category_id_nn CHECK(category_id IS NOT NULL);

ALTER TABLE product
ADD CONSTRAINT product_customer_id_fk FOREIGN KEY(customer_id)
REFERENCES customer(id);

-- 배송 테이블

CREATE TABLE shipment (
    id                      NUMBER(9) NOT NULL,
    shipment_company_id     NUMBER(9) NOT NULL,
    product_id              NUMBER(9) NOT NULL,
    product_customer_id     NUMBER(9) NOT NULL
)
LOGGING;

CREATE INDEX shipment_id_idx
ON shipment(id);

CREATE INDEX shipment_product_id_idx
ON shipment(product_id);

CREATE INDEX shipment_shipment_company_id_idx
ON shipment(shipment_company_id);

ALTER TABLE shipment ADD CONSTRAINT shipment_id_pk PRIMARY KEY(id);

ALTER TABLE shipment
ADD CONSTRAINT shipment_product_id_nn
CHECK (product_id is not null); 

ALTER TABLE shipment
ADD CONSTRAINT shipment_product_id UNIQUE(product_id);

-- 클러스터 생성

create cluster shipment_clu
(shipment_id number(9))
size 1k
tablespace users;

create index shipment_index
on cluster shipment_clu;

-- 테이블을 클러스터 안에 생성
create table shipment
cluster shipment_clu(id)
as
select * from temp_shipment;
commit;

create index shipment_id_clu_idx
on shipment(id);

-- 상품, 배송 테이블 연결

ALTER TABLE product
ADD CONSTRAINT product_shipment_id_fk
FOREIGN KEY(shipment_id) REFERENCES shipment(id)
deferrable initially deferred;

-- 주문 테이블

CREATE TABLE ORDERS (
    id                   NUMBER(9) NOT NULL,
    contract_date        DATE NOT NULL,
    customer_id          NUMBER(9) NOT NULL,
    product_id           NUMBER(9) NOT NULL,
    product_customer_id  NUMBER(9) NOT NULL,
	
    PRIMARY KEY(id, customer_id, product_id, product_customer_id)
)ORGANIZATION INDEX;

CREATE INDEX ORDERS_id_idx
ON ORDERS(id);

CREATE INDEX ORDERS_customer_buyer_id_idx
ON ORDERS(customer_id);

CREATE INDEX ORDERS_product_id_idx
ON ORDERS(product_id);

ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_customer_buyer_id_fk FOREIGN KEY(customer_id)
REFERENCES customer(id);

ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_product_id_fk FOREIGN KEY (product_id, product_customer_id)
REFERENCES product(id, customer_id);

ALTER TABLE PRODUCT ENABLE ROW MOVEMENT;