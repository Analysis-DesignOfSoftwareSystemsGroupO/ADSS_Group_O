-- GENERAL SETTINGS
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

-- SCHEMA
CREATE SCHEMA IF NOT EXISTS supplierinventorydb;
ALTER SCHEMA supplierinventorydb OWNER TO postgres;
SET default_tablespace = '';
SET default_table_access_method = heap;

-- BASE TABLES
CREATE TABLE supplierinventorydb.branch (
                                            id integer PRIMARY KEY,
                                            city character varying(50),
                                            address character varying(50)
);

CREATE TABLE supplierinventorydb.supplier (
                                              id integer PRIMARY KEY,
                                              name character varying(100),
                                              paymentMethod character varying(50),
                                              deliveryMethod character varying(50)

);

CREATE TABLE supplierinventorydb.product (
                                             id integer PRIMARY KEY,
                                             name character varying(50) NOT NULL,
                                             manufacturer character varying(50) NOT NULL,
                                             shelfLifeDays integer NOT NULL
);

-- SEQUENCES for base tables
CREATE SEQUENCE supplierinventorydb.branch_id_seq START 1;
ALTER SEQUENCE supplierinventorydb.branch_id_seq OWNED BY supplierinventorydb.branch.id;

CREATE SEQUENCE supplierinventorydb.supplier_id_seq START 1;
ALTER SEQUENCE supplierinventorydb.supplier_id_seq OWNED BY supplierinventorydb.supplier.id;

CREATE SEQUENCE supplierinventorydb.product_id_seq START 1;
ALTER SEQUENCE supplierinventorydb.product_id_seq OWNED BY supplierinventorydb.product.id;

-- RELATIONAL TABLES
CREATE TABLE supplierinventorydb.agreement (
                                               branchID integer NOT NULL,
                                               supplierID integer NOT NULL,
                                               PRIMARY KEY (branchID, supplierID),
                                               FOREIGN KEY (branchID) REFERENCES supplierinventorydb.branch(id),
                                               FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id)  ON DELETE CASCADE
);

CREATE TABLE supplierinventorydb.bank (
                                          supplierID integer PRIMARY KEY,
                                          bankAccountNumber character varying(50) NOT NULL,
                                          bankNumber character varying(50) NOT NULL,
                                          bankBranch character varying(50) NOT NULL,
                                          FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id) ON DELETE CASCADE
);

CREATE TABLE supplierinventorydb.discount (
                                              quantity integer CHECK (quantity > 0),
                                              discountAmount integer,
                                              productID integer,
                                              supplierID integer,
                                              branchID integer,
                                              PRIMARY KEY (branchID, supplierID, productID),
                                              FOREIGN KEY (branchID) REFERENCES supplierinventorydb.branch(id),
                                              FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id) ON DELETE CASCADE,
                                              FOREIGN KEY (productID) REFERENCES supplierinventorydb.product(id)
);

CREATE TABLE supplierinventorydb.informationcontact (
                                                        supplierID integer,
                                                        contactName character varying(50) PRIMARY KEY,
                                                        contactPhone character varying(50),
                                                        title character varying(50),
                                                        FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id) ON DELETE CASCADE
);

CREATE TABLE supplierinventorydb."order" (
                                             id integer PRIMARY KEY,
                                             date date,
                                             totalPrice integer,
                                             branchID integer,
                                             supplierID integer,
                                             FOREIGN KEY (branchID) REFERENCES supplierinventorydb.branch(id),
                                             FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id)
);

CREATE SEQUENCE supplierinventorydb.order_id_seq START 1;
ALTER SEQUENCE supplierinventorydb.order_id_seq OWNED BY supplierinventorydb."order".id;

CREATE TABLE supplierinventorydb.productOfSupplier (
                                                       productID integer,
                                                       supplierID integer,
                                                       price integer check ( price > 0 ),
                                                       PRIMARY KEY (productID, supplierID),
                                                       FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id) ON DELETE CASCADE,
                                                       FOREIGN KEY (productID) REFERENCES supplierinventorydb.product(id)
);

CREATE TABLE supplierinventorydb.productsinorder (
                                                     quantity integer CHECK (quantity > 0),
                                                     orderID integer PRIMARY KEY,
                                                     suppliedItemID integer,
                                                     FOREIGN KEY (orderID) REFERENCES supplierinventorydb."order"(id),
                                                     FOREIGN KEY (suppliedItemID) REFERENCES supplierinventorydb.product(id) -- Assuming supplieditem → product
);

CREATE TABLE supplierinventorydb.productInAgreement (
                                                        price integer check ( price > 0 ),
                                                        productID integer,
                                                        branchID integer,
                                                        supplierID integer,
                                                        PRIMARY KEY (productID, supplierID, branchID),
                                                        FOREIGN KEY (branchID) REFERENCES supplierinventorydb.branch(id),
                                                        FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id) ON DELETE CASCADE,
                                                        FOREIGN KEY (productID) REFERENCES supplierinventorydb.product(id)
);

CREATE TABLE supplierinventorydb.constantorders (
                                                                   branchID integer,
                                                                   supplierID integer,
                                                                   suppliedItemID integer,
                                                                   quantity integer check ( quantity > 0 ) ,
                                                                   dayOfWeek character varying(50),
                                                                   PRIMARY KEY (branchID, supplierID, suppliedItemID),
                                                                   FOREIGN KEY (branchID) REFERENCES supplierinventorydb.branch(id),
                                                                   FOREIGN KEY (supplierID) REFERENCES supplierinventorydb.supplier(id) ON DELETE CASCADE,
                                                                   FOREIGN KEY (suppliedItemID) REFERENCES supplierinventorydb.product(id)
);

-- DEFAULT ID VALUES
ALTER TABLE ONLY supplierinventorydb.branch ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.branch_id_seq');
ALTER TABLE ONLY supplierinventorydb."order" ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.order_id_seq');
ALTER TABLE ONLY supplierinventorydb.product ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.product_id_seq');
ALTER TABLE ONLY supplierinventorydb.supplier ALTER COLUMN id SET DEFAULT nextval('supplierinventorydb.supplier_id_seq');

-- SET INITIAL VALUES
SELECT pg_catalog.setval('supplierinventorydb.branch_id_seq', 1, false);
SELECT pg_catalog.setval('supplierinventorydb.order_id_seq', 1, false);
SELECT pg_catalog.setval('supplierinventorydb.product_id_seq', 1, false);
SELECT pg_catalog.setval('supplierinventorydb.supplier_id_seq', 1, false);