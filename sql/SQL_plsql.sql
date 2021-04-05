--------------------------------------------
 SQL sample data
--------------------------------------------

/* 회원가입 */

-- 중복 확인
CREATE OR REPLACE PROCEDURE C_NAMECHECK
(
  c_name IN customer.name%TYPE,
  R OUT NUMBER
)
IS
BEGIN
  SELECT COUNT(*) INTO R
  FROM customer
  WHERE name = c_name ;
END;
/

-- 회원 가입 프로시저

CREATE OR REPLACE PROCEDURE customer_insert_version2
(
	name IN customer.name%TYPE,
	password IN customer.password%TYPE,
	zipcode IN customer.zipcode%TYPE,
	phone_number IN customer.phone_number%TYPE,
	coin IN customer.coin%TYPE,
	volunteer_working_time IN customer.volunteer_working_time%TYPE,
    possible OUT NUMBER
)
IS
  MR NUMBER;
BEGIN
  -- 중복체크
  C_NAMECHECK(name, MR);
  IF MR = 0 THEN
    --테이블에 데이터 넣기
    INSERT INTO CUSTOMER(id,name,password,zipcode,phone_number,coin, volunteer_working_time)
	VALUES(customer_id_seq.nextval,name, password, zipcode, phone_number, coin,  volunteer_working_time);
    possible := 1 ; --possible 반대,
    COMMIT;
  ELSE
    possible := 0 ;
  END IF;
  DBMS_OUTPUT.PUT_LINE(TO_CHAR(MR));
END ;
/

------- 1 리턴되면 회원가입 성공, 0 리턴 회원가입 실패


/* 로그인 */

-- 비밀번호 검사
CREATE OR REPLACE PROCEDURE customer_login_check
(
  c_name IN customer.name%TYPE,
  c_password IN customer.password%TYPE,
  R OUT NUMBER
)
IS
BEGIN
  SELECT COUNT(*) INTO R
  FROM customer
  WHERE name = c_name AND password = c_password ;

END;
/


CREATE OR REPLACE PROCEDURE customer_login
(
    --id IN customer.id%TYPE,
	name IN customer.name%TYPE,
	password IN customer.password%TYPE,
    possible OUT NUMBER
)
IS
  MR NUMBER;
BEGIN
  -- 중복체크
  customer_login_check(name,password, MR);
  IF MR = 0 THEN
	
    possible := 0 ; -- 0이면 등록된 아이디가 없거나 비밀번호가 틀렸습니다.
	
  ELSE
  
    possible := 1 ; -- 1이면 로그인 성공
	
  END IF;
  DBMS_OUTPUT.PUT_LINE(TO_CHAR(MR));
END ;
/

/* 테이블 조회하기 */

-- 현재 로그인한 회원 정보 가져오기
CREATE OR REPLACE procedure sp_get_customer_info ( c_name IN customer.name%TYPE, record_list OUT SYS_REFCURSOR )
AS
BEGIN
OPEN record_list FOR
SELECT *
FROM Customer
WHERE name = c_name;
END;
/

-- 카테고리 조회하기
CREATE OR REPLACE procedure get_category_info(record_list OUT SYS_REFCURSOR)
AS
BEGIN
OPEN record_list FOR
SELECT *
FROM category;
END;
/

-- 배송회사 조회하기
CREATE OR REPLACE procedure get_company_info(record_list OUT SYS_REFCURSOR)
AS
BEGIN
OPEN record_list FOR
SELECT *
FROM shipment_company;
END;
/

-- 전체 상품 조회

CREATE OR REPLACE procedure ALL_product
(
ALL_PRODUCT_record  OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN ALL_PRODUCT_record FOR
SELECT p.id, p.name, p.information, p.price, c.name, p.category_name, p.product_status, sp.name, (select distinct c.name  from customer where c.id = p.buy_customer_id) as buy_custmoer_name
FROM product p
right outer JOIN customer c on p.customer_id = c.id
INNER JOIN shipment s on p.shipment_id = s.id
INNER JOIN shipment_company sp on sp.id = s.shipment_company_id;
END;
/

-- 전체 상품 조회 /* 뷰 버전 */

sqlplus system/oracle@localhost:1521/xepdb1

alter user coin
quota unlimited on users;

grant create any table to coin;
grant create view to coin;

create or replace view all_product_view
AS
SELECT p.id, p.name, p.information, p.price, c.name as product_custmoer_name, p.category_name, p.product_status, sp.name as ship_company_name, (select name from customer where id= p.buy_customer_id) as buy_custmoer_name
FROM product p
INNER JOIN customer c on p.customer_id = c.id
INNER JOIN shipment s on p.shipment_id = s.id
INNER JOIN shipment_company sp on sp.id = s.shipment_company_id;

CREATE OR REPLACE procedure all_product_view_select
(
   all_product_view_record  OUT       SYS_REFCURSOR
)
IS
BEGIN
   OPEN all_product_view_record FOR
   SELECT *
   FROM all_product_view;
END;
/

-- 카테고리별 조회

CREATE OR REPLACE PROCEDURE select_category
(
    customer_id IN product.customer_id%TYPE,
	category_id IN product.category_id%TYPE,
	category_name IN product.category_name%TYPE,
	s_category_record OUT SYS_REFCURSOR
	
)
IS
BEGIN
  
  
	OPEN s_category_record FOR
	SELECT p.name, p.price, (select name from customer where id= customer_id), p.category_name, p.product_status
	FROM PRODUCT p inner join category c
	on p.category_id = c.id
	order by p.id asc;
	
END ;
/

/* 물품 구매 */
-- 최신버전 성공은 1, 실패는 0반환
CREATE OR REPLACE PROCEDURE buy_item
(
   p_id IN product.id%Type,
   product_customer_name IN customer.name%Type, -- 판매자 id값
   c_id IN customer.id%TYPE,
   contract_date IN orders.contract_date%Type, -- 현재 날짜도 넣어줘.
   ispossible OUT NUMBER
)
IS
   seller_id NUMBER;
   p_product_status CLOB;
BEGIN
   select product_status into p_product_status
   from product
   where p_id=id;
   IF p_product_status = 'READY' THEN
	   SELECT id INTO seller_id
	   FROM customer
	   where name = product_customer_name;
	  
		 
	   UPDATE product SET PRODUCT_STATUS = 'PROGRESS', buy_customer_id = c_id WHERE p_id = id;
	  
	   INSERT INTO orders(id, contract_date, customer_id, product_id, product_customer_id)
	   VALUES(ORDERS_id_seq.nextval, contract_date, c_id, p_id, seller_id);
	   ispossible := 1;
	   commit;
	  
	ELSE
	   ispossible := 0;
	
    END IF;
END ;
/

/* 물품 등록 */
CREATE OR REPLACE PROCEDURE product_insert
(

	customer_id IN customer.id%TYPE, -- 고객 id
    p_name IN product.name%Type, -- 제품이름
	information IN product.information%TYPE, --제품 설명
	price IN product.price%TYPE, -- 제품 가격
    category_name IN category.name%TYPE, --카테고리 이름
	shipment_name IN shipment_company.name%TYPE -- 배송회사 이름
	
)
IS 
	category_id NUMBER;
	shipcom_id NUMBER;
	ship_id NUMBER := shipment_id_seq.nextval;
	product_id NUMBER :=product_id_seq.nextval;
	
BEGIN

	--- category 아이디 변수로 저장
	SELECT id INTO category_id
	FROM category
	where name=category_name;
	
	SELECT id INTO shipcom_id
	FROM shipment_company
	where name=shipment_name;
	
	
	
	-- product 테이블에 저장
	
	insert into product(id,customer_id,name,information,price,category_id,category_name,product_status,shipment_id)
	values(product_id,customer_id,p_name, information, price,
	category_id,category_name, 'READY', ship_id);
	
	-- product customer id 판매자
	
	
	insert into shipment(id,shipment_company_id,product_id, PRODUCT_CUSTOMER_ID)
	values(ship_id, shipcom_id, product_id, customer_id);
	
	commit;
	
END; 
/

/* 등록 물품 수정 */
ALTER TABLE PRODUCT ENABLE ROW MOVEMENT; -- 파티션 수정

CREATE OR REPLACE PROCEDURE mysell_product_update
(
	p_id IN product.id%TYPE,
	customer_id IN customer.id%TYPE, -- 고객 id
    p_name IN product.name%Type, -- 제품이름
	P_information IN product.information%TYPE, --제품 설명
	p_price IN product.price%TYPE, -- 제품 가격
    p_category_name IN category.name%TYPE, --카테고리 이름
	shipment_name IN shipment_company.name%TYPE -- 배송회사 이름
	
)
IS 
c_category_id NUMBER;
shipcom_id NUMBER;
BEGIN

	SELECT id INTO c_category_id
	FROM category
	where name=p_category_name;
	
	SELECT id INTO shipcom_id
	FROM shipment_company
	where name=shipment_name;

	UPDATE product SET
	name= p_name,
	information= P_information,
	price = p_price,
	category_id= c_category_id,
	category_name= p_category_name
	where p_id = id ;
	
	DBMS_OUTPUT.PUT_LINE(shipcom_id);
	UPDATE shipment SET shipment_company_id = shipcom_id
	where id = p_id ;
	
	commit;
END;
/

/* 등록 물품 취소 */
CREATE OR REPLACE PROCEDURE mysell_product_cancel
(
	p_id IN product.id%TYPE
)
IS 	
BEGIN
	-- 판매 취소
	
	
	DELETE FROM product
	where p_id = id ;
	
	
	DELETE FROM shipment
	where p_id = product_id ;
	
	commit;
END;
/

/* 수령 확인 */
CREATE OR REPLACE PROCEDURE receipt_click
(
 -- 제품 id 받아서 상태만 변경
   p_id IN product.id%TYPE
)
IS
BEGIN
   UPDATE product SET PRODUCT_STATUS = 'FINISH' WHERE p_id = id;
   commit;
END;
/

/* 수령 확인 트리거 */

CREATE OR REPLACE TRIGGER TRIG_TEST
	AFTER UPDATE OF PRODUCT_STATUS ON PRODUCT   --컬럼명 ON 테이블 명
	FOR EACH ROW
DECLARE
--p_status product.PRODUCT_STATUS%TYPE;
BEGIN
	-- 1. 확인 버튼 누르면 상태값(progress -> finish) 변경
	
	-- 2. 마일리지를 판매자에게 지급. 거래 성사되어 마일리지 양쪽에 100씩 지급
	
	IF :NEW.PRODUCT_STATUS = 'FINISH' THEN
		UPDATE CUSTOMER
		SET coin = coin + :NEW.PRICE +100
		WHERE :NEW.CUSTOMER_ID = ID;
		
		
		UPDATE CUSTOMER
		SET coin = coin + 100
		WHERE :NEW.BUY_CUSTOMER_ID = ID;
		
		DBMS_OUTPUT.PUT_LINE('거래성공! + 마일리지 100 지급!');
		
	ELSIF :NEW.PRODUCT_STATUS = 'PROGRESS' THEN
		
		UPDATE CUSTOMER
		SET coin = coin - :NEW.price
		where :NEW.BUY_CUSTOMER_ID = id;
		DBMS_OUTPUT.PUT_LINE('거래 신청 완료!!');
	END IF;

END;
/