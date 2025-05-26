

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


CREATE SCHEMA supplierinventorydb;


ALTER SCHEMA supplierinventorydb OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;



CREATE TABLE supplierinventorydb.agreement (
    branchID integer NOT NULL,
    supplierID integer NOT NULL
);


ALTER TABLE supplierinventorydb.agreement OWNER TO postgres;


CREATE TABLE supplierinventorydb.bank (
    supplierID integer,
    bankAccountNumber character varying(50) NOT NULL,
    bankNumber character varying(50) NOT NULL,
    bankBranch character varying(50) NOT NULL
);


ALTER TABLE supplierinventorydb.bank OWNER TO postgres;


CREATE TABLE supplierinventorydb.branch (
    id integer NOT NULL,
    city character varying(50),
    address character varying(50)
);


ALTER TABLE supplierinventorydb.branch OWNER TO postgres;



CREATE SEQUENCE supplierinventorydb.branch_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE supplierinventorydb.branch_id_seq OWNER TO postgres;


ALTER SEQUENCE supplierinventorydb.branch_id_seq OWNED BY supplierinventorydb.branch.id;




CREATE TABLE supplierinventorydb.constantdelivery (
    supplierID integer,
    constantDay character varying(50)
);


ALTER TABLE supplierinventorydb.constantdelivery OWNER TO postgres;


CREATE TABLE supplierinventorydb.discount (
    quantity integer,
    discountAmount integer,
    suppliedItemID integer,
    CONSTRAINT discount_quantity_check CHECK ((quantity > 0))
);


ALTER TABLE supplierinventorydb.discount OWNER TO postgres;



CREATE TABLE supplierinventorydb.immidiatedelivery (
    supplierID integer,
    daysAfterNotice integer
);


ALTER TABLE supplierinventorydb.immidiatedelivery OWNER TO postgres;


CREATE TABLE supplierinventorydb.informationcontact (
    supplierID integer,
    contactName character varying(50),
    contactPhone character varying(50),
    title character varying(50)
);


ALTER TABLE supplierinventorydb.informationcontact OWNER TO postgres;


CREATE TABLE supplierinventorydb."order" (
    id integer NOT NULL primary key ,
    date date,
    totalPrice integer,
    branchID integer,
    supplierID integer
);


ALTER TABLE supplierinventorydb."order" OWNER TO postgres;



CREATE SEQUENCE supplierinventorydb.order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE supplierinventorydb.order_id_seq OWNER TO postgres;



ALTER SEQUENCE supplierinventorydb.order_id_seq OWNED BY supplierinventorydb."order".id;



CREATE TABLE supplierinventorydb.product (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    manufacturer character varying(50) NOT NULL,
    shelfLifeDays integer NOT NULL
);


ALTER TABLE supplierinventorydb.product OWNER TO postgres;



CREATE SEQUENCE supplierinventorydb.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE supplierinventorydb.product_id_seq OWNER TO postgres;



ALTER SEQUENCE supplierinventorydb.product_id_seq OWNED BY supplierinventorydb.product.id;




CREATE TABLE supplierinventorydb.productcatalog (
    productID integer,
    supplierID integer,
    price integer
);


ALTER TABLE supplierinventorydb.productcatalog OWNER TO postgres;


CREATE TABLE supplierinventorydb.productsinorder (
    quantity integer,
    orderID integer,
    suppliedItemID integer,
    CONSTRAINT productsinorder_quantity_check CHECK ((quantity > 0))
);


ALTER TABLE supplierinventorydb.productsinorder OWNER TO postgres;



CREATE TABLE supplierinventorydb.selfpickupdelivery (
    supplierID integer
);


ALTER TABLE supplierinventorydb.selfpickupdelivery OWNER TO postgres;


CREATE TABLE supplierinventorydb.supplieditem (
    price integer,
    productID integer,
    branchID integer ,
    supplierID integer
);


ALTER TABLE supplierinventorydb.supplieditem OWNER TO postgres;



CREATE SEQUENCE supplierinventorydb.supplieditem_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE supplierinventorydb.supplieditem_id_seq OWNER TO postgres;



ALTER SEQUENCE supplierinventorydb.supplieditem_id_seq OWNED BY supplierinventorydb.supplieditem.id;




CREATE TABLE supplierinventorydb.supplier (
    id integer NOT NULL,
    name character varying(100),
    paymentMethod character varying(50)
);


ALTER TABLE supplierinventorydb.supplier OWNER TO postgres;


CREATE SEQUENCE supplierinventorydb.supplier_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE supplierinventorydb.supplier_id_seq OWNER TO postgres;



ALTER SEQUENCE supplierinventorydb.supplier_id_seq OWNED BY supplierinventorydb.supplier.id;




ALTER TABLE ONLY supplierinventorydb.branch ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.branch_id_seq'::regclass);




ALTER TABLE ONLY supplierinventorydb."order" ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.order_id_seq'::regclass);




ALTER TABLE ONLY supplierinventorydb.product ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.product_id_seq'::regclass);




ALTER TABLE ONLY supplierinventorydb.supplieditem ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.supplieditem_id_seq'::regclass);




ALTER TABLE ONLY supplierinventorydb.supplier ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.supplier_id_seq'::regclass);




SELECT pg_catalog.setval('supplierinventorydb.branch_id_seq', 1, false);

SELECT pg_catalog.setval('supplierinventorydb.order_id_seq', 1, false);




SELECT pg_catalog.setval('supplierinventorydb.product_id_seq', 1, false);




SELECT pg_catalog.setval('supplierinventorydb.supplieditem_id_seq', 1, false);



SELECT pg_catalog.setval('supplierinventorydb.supplier_id_seq', 1, false);



ALTER TABLE ONLY supplierinventorydb.agreement
    ADD CONSTRAINT agreement_pkey PRIMARY KEY (branchid, supplierid);



ALTER TABLE ONLY supplierinventorydb.bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (bankaccountnumber, banknumber, bankbranch);




ALTER TABLE ONLY supplierinventorydb.branch
    ADD CONSTRAINT branch_pkey PRIMARY KEY (id);



ALTER TABLE ONLY supplierinventorydb."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);




ALTER TABLE ONLY supplierinventorydb.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);




ALTER TABLE ONLY supplierinventorydb.supplieditem
    ADD CONSTRAINT supplieditem_pkey PRIMARY KEY (id);




ALTER TABLE ONLY supplierinventorydb.supplier
    ADD CONSTRAINT supplier_pkey PRIMARY KEY (id);




ALTER TABLE ONLY supplierinventorydb.agreement
    ADD CONSTRAINT agreement_branchid_fkey FOREIGN KEY (branchid) REFERENCES supplierinventorydb.branch(id);



ALTER TABLE ONLY supplierinventorydb.agreement
    ADD CONSTRAINT agreement_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);




ALTER TABLE ONLY supplierinventorydb.bank
    ADD CONSTRAINT bank_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);


ALTER TABLE ONLY supplierinventorydb.constantdelivery
    ADD CONSTRAINT constantdelivery_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);




ALTER TABLE ONLY supplierinventorydb.discount
    ADD CONSTRAINT discount_supplieditemid_fkey FOREIGN KEY (supplieditemid) REFERENCES supplierinventorydb.supplieditem(id);




ALTER TABLE ONLY supplierinventorydb.immidiatedelivery
    ADD CONSTRAINT immidiatedelivery_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);




ALTER TABLE ONLY supplierinventorydb.informationcontact
    ADD CONSTRAINT informationcontact_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);



ALTER TABLE ONLY supplierinventorydb."order"
    ADD CONSTRAINT order_branchid_fkey FOREIGN KEY (branchid) REFERENCES supplierinventorydb.branch(id);




ALTER TABLE ONLY supplierinventorydb."order"
    ADD CONSTRAINT order_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);



ALTER TABLE ONLY supplierinventorydb.productcatalog
    ADD CONSTRAINT productcatalog_productid_fkey FOREIGN KEY (productid) REFERENCES supplierinventorydb.product(id);




ALTER TABLE ONLY supplierinventorydb.productcatalog
    ADD CONSTRAINT productcatalog_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);



ALTER TABLE ONLY supplierinventorydb.productsinorder
    ADD CONSTRAINT productsinorder_orderid_fkey FOREIGN KEY (orderid) REFERENCES supplierinventorydb."order"(id);



ALTER TABLE ONLY supplierinventorydb.productsinorder
    ADD CONSTRAINT productsinorder_supplieditemid_fkey FOREIGN KEY (supplieditemid) REFERENCES supplierinventorydb.supplieditem(id);



ALTER TABLE ONLY supplierinventorydb.selfpickupdelivery
    ADD CONSTRAINT selfpickupdelivery_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);




ALTER TABLE ONLY supplierinventorydb.supplieditem
    ADD CONSTRAINT supplieditem_branchid_fkey FOREIGN KEY (branchid) REFERENCES supplierinventorydb.branch(id);




ALTER TABLE ONLY supplierinventorydb.supplieditem
    ADD CONSTRAINT supplieditem_productid_fkey FOREIGN KEY (productid) REFERENCES supplierinventorydb.product(id);




ALTER TABLE ONLY supplierinventorydb.supplieditem
    ADD CONSTRAINT supplieditem_supplierid_fkey FOREIGN KEY (supplierid) REFERENCES supplierinventorydb.supplier(id);




