-- ������ Oracle SQL Developer Data Modeler 20.4.0.374.0801
--   ��ġ:        2021-03-31 11:29:46 KST
--   ����Ʈ:      Oracle Database 11g
--   ����:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE
DROP TABLE customer CASCADE CONSTRAINTS;
DROP TABLE product CASCADE CONSTRAINTS;
DROP TABLE shipment CASCADE CONSTRAINTS;
DROP TABLE shipment_company CASCADE CONSTRAINTS;
DROP TABLE orders CASCADE CONSTRAINTS;
DROP TABLE category CASCADE CONSTRAINTS;

-- 시퀀스 삭제

drop sequence customer_id_seq;
drop sequence product_id_seq;
drop sequence ORDERS_id_seq;
drop sequence shipment_id_seq;
drop sequence shipment_company_id_seq;
drop sequence category_id_seq;


-- shipment_id_idx, shipment_product_id_idx, shipment_shipment_company_id_idx

-------------------------------------------------------
CREATE SEQUENCE customer_id_seq
START WITH 1
INCREMENT BY 1
MAXVALUE 999999
MINVALUE 1
NOCYCLE;
--CACHE

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
-------------------------------------------------------

CREATE TABLE category (
    id    NUMBER(9) NOT NULL,
    name  VARCHAR2(50 CHAR) NOT NULL
);
CREATE INDEX category_id_idx
ON category(id);

ALTER TABLE category
ADD CONSTRAINT category_id_pk PRIMARY KEY(id);

-- 샘플 데이터 삽입
--insert into category values(1,'clothing');
--insert into category values(2,'digital');
--insert into category values(3,'furniture');
--insert into category values(4,'book');
--insert into category values(5,'etc');
--commit;

--insert into product values(1,1,'asdf','124',123,1,'의류',1);

-------------------------------------------------------



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




-------------------------------------------------------
CREATE TABLE shipment_company (
    id    NUMBER(9) NOT NULL,
    name  VARCHAR2(20 CHAR) NOT NULL
)
LOGGING;

ALTER TABLE shipment_company ADD CONSTRAINT shipment_company_id_pk PRIMARY KEY ( id );

-- 샘플 데이터


-------------------------------------------------------

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


-- 샘플데이터
-- insert into product values(1,1,'asdf','124',123,1,'의류',1);

"""
CREATE TABLE product (
    id           NUMBER(9) NOT NULL,
    customer_id  NUMBER(9) NOT NULL,
    name         VARCHAR2(50 CHAR) NOT NULL,
    information  VARCHAR2(200 CHAR),
    price        NUMBER(9) NOT NULL,
    category_id  NUMBER(9) NOT NULL,
    category_name VARCHAR2(50 CHAR) NOT NULL, -- 새로 추가됨 (카테고리 명)
	status VARCHAR2(10 CHAR) NOT NULL, --판매 상태 정보 추가 (READY,FINISH)
    shipment_id  NUMBER(9) NOT NULL
);
"""




CREATE INDEX product_id_idx
ON product(id);

--foreign Ű�� �޾ƿͼ� �ε��� ����
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

-- 일단 보류 product_insert 프로시저에서 에러발생(무결성 제약조건 위배)
-- ALTER TABLE product
-- ADD CONSTRAINT product_category_id_fk FOREIGN KEY(category_id)
-- REFERENCES category(id);

ALTER TABLE product
ADD CONSTRAINT product_customer_id_fk FOREIGN KEY(customer_id)
REFERENCES customer(id);



-------------------------------------------------------
CREATE TABLE shipment (
    id                      NUMBER(9) NOT NULL,
    shipment_company_id     NUMBER(9) NOT NULL,
    product_id              NUMBER(9) NOT NULL,
    product_customer_id     NUMBER(9) NOT NULL
)
LOGGING;
--인덱스 shipment_id_idx, shipment_product_id_idx, shipment_shipment_company_id_idx
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

-- 1:1 mandetory not null, unique �Ӽ�, fk

ALTER TABLE shipment
ADD CONSTRAINT shipment_product_id UNIQUE(product_id);

-- insert shipment에서 무결성 제약조건 걸려서 보류

-- ALTER TABLE shipment
-- ADD CONSTRAINT shipment_shipment_company_id_fk 
-- FOREIGN KEY(shipment_company_id)
-- REFERENCES shipment_company(id);

--shipment_product_id_fk 무결성 제약조건 걸려서 보류
-- ALTER TABLE shipment
-- ADD CONSTRAINT shipment_product_id_fk FOREIGN KEY (product_id, product_customer_id)
-- REFERENCES product(id, customer_id);


------------------------
---해보기------------------------

--drop table temp_shipment cascade constraint;
CREATE TABLE temp_shipment (
    id                      NUMBER(9) NOT NULL,

    shipment_company_id     NUMBER(9) NOT NULL,
    product_id              NUMBER(9) NOT NULL,
    product_customer_id     NUMBER(9) NOT NULL
)
LOGGING;
--인덱스 shipment_id_idx, shipment_product_id_idx, shipment_shipment_company_id_idx
CREATE INDEX shipment_id_idx
ON temp_shipment(id);

CREATE INDEX shipment_product_id_idx
ON temp_shipment(product_id);

CREATE INDEX shipment_shipment_company_id_idx
ON temp_shipment(shipment_company_id);

ALTER TABLE temp_shipment ADD CONSTRAINT shipment_id_pk PRIMARY KEY(id);

ALTER TABLE temp_shipment
ADD CONSTRAINT shipment_product_id_nn
CHECK (product_id is not null); 

-- 1:1 mandetory not null, unique �Ӽ�, fk

ALTER TABLE temp_shipment
ADD CONSTRAINT shipment_product_id UNIQUE(product_id);

-------------------------------------------------------------------

------------------------
-- 클러스터 생성

create cluster shipment_clu
(shipment_id number(9))
size 1k
tablespace users;

-- 클러스터용 인덱스 생성
create index shipment_index
on cluster shipment_clu;



-- 테이블을 클러스터 안에 생성
create table shipment
cluster shipment_clu(id)
as
select * from temp_shipment;
commit;

-- 이름 바꾸기
--rename temp_shipment to old_shipment;
--rename shipment to temp_shipment;

-- 테이블에 추가 인덱스 생성
create index shipment_id_clu_idx
on shipment(id);








-------------------------------------------------------
-- product ���̺��� ���� �������� �߰�
ALTER TABLE product
ADD CONSTRAINT product_shipment_id_fk
FOREIGN KEY(shipment_id) REFERENCES shipment(id)
deferrable initially deferred;

-------------------------------------------------------
-- ORDER


CREATE TABLE ORDERS (
    id                   NUMBER(9) NOT NULL,
    contract_date        DATE NOT NULL,
    customer_id          NUMBER(9) NOT NULL,
    product_id           NUMBER(9) NOT NULL,
    product_customer_id  NUMBER(9) NOT NULL,

    PRIMARY KEY(id, customer_id, product_id, product_customer_id)
)ORGANIZATION INDEX;

-- 오류 발생 시     set sqlblanklines on 

CREATE INDEX ORDERS_id_idx
ON ORDERS(id);

CREATE INDEX ORDERS_customer_buyer_id_idx
ON ORDERS(customer_id);

CREATE INDEX ORDERS_product_id_idx
ON ORDERS(product_id);

-- ALTER TABLE ORDERS
-- ADD CONSTRAINT ORDERS_id_pk PRIMARY KEY(id);

"""
이부분 복합키에 외래키가 포함되도록..


외래키만 지정할 경우. 식별 값이 없어도 테이블에 삽입이 가능해서.

"""
ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_customer_buyer_id_fk FOREIGN KEY(customer_id)
REFERENCES customer(id);

ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_product_id_fk FOREIGN KEY (product_id, product_customer_id)
REFERENCES product(id, customer_id);


---------------------------------------------
---------------------------------------------
---------------------------------------------
-- 





------------------------------------------------------------
---------------------
-- 데이터 삽입

"""
insert into customer values(1,'김남수','1234','1234','01021091323',123,12);
insert into category values(1,'의류');
insert into product values(1,1,'김남수','12441',123,1,1);
insert into orders values(1,'20190210',1,1,1);
select * from orders;
select * from product;

"""

"""
이전 코드

CREATE TABLE ORDERS (
    id                   NUMBER(9) NOT NULL,
    contract_date        DATE NOT NULL,
    customer_id          NUMBER(9) NOT NULL,
    product_id           NUMBER(9) NOT NULL,
    product_customer_id  NUMBER(9) NOT NULL
);

CREATE INDEX ORDERS_id_idx
ON ORDERS(id);

CREATE INDEX ORDERS_customer_buyer_id_idx
ON ORDERS(customer_id);

CREATE INDEX ORDERS_product_id_idx
ON ORDERS(product_id);

ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_id_pk PRIMARY KEY(id);

ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_customer_buyer_id_fk FOREIGN KEY(customer_id)
REFERENCES customer(id);

ALTER TABLE ORDERS
ADD CONSTRAINT ORDERS_product_id_fk FOREIGN KEY (product_id, product_customer_id)
REFERENCES product(id, customer_id);
"""
-------------------------------------------------------









-- https://coding-factory.tistory.com/422--
-- Oracle SQL Developer Data Modeler ��� ������: 
-- 
-- CREATE TABLE                             6
-- CREATE INDEX                             2
-- ALTER TABLE                             13
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0