--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-05-27 14:07:33

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

--
-- TOC entry 6 (class 2615 OID 16464)
-- Name: HR; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "HR";


ALTER SCHEMA "HR" OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 218 (class 1259 OID 16473)
-- Name: Branch; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Branch" (
    "branchID" integer NOT NULL,
    "Name" text,
    "District" text
);


ALTER TABLE "HR"."Branch" OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16499)
-- Name: Constraint; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Constraint" (
    "constraintID" integer NOT NULL,
    "empNum" integer,
    "constraintDeadline" date,
    "ShiftType" text,
    "WeekDay" text,
    explanation text
);


ALTER TABLE "HR"."Constraint" OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16506)
-- Name: Contract; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Contract" (
    "ContractID" integer NOT NULL,
    "empId" integer,
    "daysOff" integer,
    "sickDays" integer,
    "minEveninigShift" integer,
    "minDayShift" integer
);


ALTER TABLE "HR"."Contract" OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16492)
-- Name: Employee; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Employee" (
    "empNum" integer NOT NULL,
    "branchID" integer,
    "empName" text,
    "empId" integer,
    "empPassword " text,
    "empBankAccount" text,
    "empSalary" integer,
    "empStartDate" date,
    "ContractID" integer
);


ALTER TABLE "HR"."Employee" OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16523)
-- Name: FilledRole; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."FilledRole" (
    "shiftID" integer NOT NULL,
    "empNum" integer NOT NULL,
    "roleNumber" integer NOT NULL
);


ALTER TABLE "HR"."FilledRole" OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16511)
-- Name: Managers; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Managers" (
    "empId" integer NOT NULL
);


ALTER TABLE "HR"."Managers" OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16550)
-- Name: RequiredRoles; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."RequiredRoles" (
    "shiftID" integer NOT NULL,
    "roleNumber" integer NOT NULL,
    counter integer
);


ALTER TABLE "HR"."RequiredRoles" OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16516)
-- Name: Role; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Role" (
    "roleNumber" integer NOT NULL,
    description text
);


ALTER TABLE "HR"."Role" OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16538)
-- Name: Shifts; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Shifts" (
    "weekID" integer,
    "shiftID" integer NOT NULL,
    day text,
    type text,
    status text,
    "shiftManager" integer
);


ALTER TABLE "HR"."Shifts" OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16531)
-- Name: Users; Type: TABLE; Schema: HR; Owner: postgres
--

CREATE TABLE "HR"."Users" (
    "userID" integer NOT NULL,
    level text
);


ALTER TABLE "HR"."Users" OWNER TO postgres;



--
-- TOC entry 4732 (class 2606 OID 16477)
-- Name: Branch Branch_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Branch"
    ADD CONSTRAINT "Branch_pkey" PRIMARY KEY ("branchID");


--
-- TOC entry 4736 (class 2606 OID 16505)
-- Name: Constraint Constraint_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Constraint"
    ADD CONSTRAINT "Constraint_pkey" PRIMARY KEY ("constraintID");


--
-- TOC entry 4738 (class 2606 OID 16510)
-- Name: Contract Contract_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Contract"
    ADD CONSTRAINT "Contract_pkey" PRIMARY KEY ("ContractID");


--
-- TOC entry 4734 (class 2606 OID 16498)
-- Name: Employee Employee_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Employee"
    ADD CONSTRAINT "Employee_pkey" PRIMARY KEY ("empNum");


--
-- TOC entry 4744 (class 2606 OID 16527)
-- Name: FilledRole FilledRole_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."FilledRole"
    ADD CONSTRAINT "FilledRole_pkey" PRIMARY KEY ("shiftID", "empNum", "roleNumber");


--
-- TOC entry 4740 (class 2606 OID 16515)
-- Name: Managers Managers_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Managers"
    ADD CONSTRAINT "Managers_pkey" PRIMARY KEY ("empId");


--
-- TOC entry 4750 (class 2606 OID 16554)
-- Name: RequiredRoles RequiredRoles_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."RequiredRoles"
    ADD CONSTRAINT "RequiredRoles_pkey" PRIMARY KEY ("shiftID", "roleNumber");


--
-- TOC entry 4742 (class 2606 OID 16522)
-- Name: Role Role_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Role"
    ADD CONSTRAINT "Role_pkey" PRIMARY KEY ("roleNumber");


--
-- TOC entry 4748 (class 2606 OID 16544)
-- Name: Shifts Shifts_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Shifts"
    ADD CONSTRAINT "Shifts_pkey" PRIMARY KEY ("shiftID");


--
-- TOC entry 4746 (class 2606 OID 16537)
-- Name: Users Users_pkey; Type: CONSTRAINT; Schema: HR; Owner: postgres
--

ALTER TABLE ONLY "HR"."Users"
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY ("userID");


-- Completed on 2025-05-27 14:07:34

--
-- PostgreSQL database dump complete
--

