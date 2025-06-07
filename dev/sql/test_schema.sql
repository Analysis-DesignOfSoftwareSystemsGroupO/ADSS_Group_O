-- Drop if exists (clean start)
DROP TABLE IF EXISTS shiftassignments CASCADE;
DROP TABLE IF EXISTS requiredroles CASCADE;
DROP TABLE IF EXISTS shifts CASCADE;
DROP TABLE IF EXISTS constraints CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS employmentcontracts CASCADE;
DROP TABLE IF EXISTS employeerole CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS archived_employees CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS branches CASCADE;

-- Branches
CREATE TABLE branches (
    branchID INT PRIMARY KEY,
    name VARCHAR(255),
    district VARCHAR(255)
);

-- Employees
CREATE TABLE employees (
    empID BIGINT PRIMARY KEY,
    empName VARCHAR(255),
    empPassword VARCHAR(255),
    empBankAccount VARCHAR(255),
    empSalary INT,
    empStartDate DATE,
    minDayShift INT,
    minEveningShift INT,
    sickDays INT,
    daysOff INT,
    branchID INT REFERENCES branches(branchID)
);

-- Archived Employees
CREATE TABLE archived_employees (
    empID BIGINT PRIMARY KEY,
    archiveDate DATE
);

-- Roles
CREATE TABLE roles (
    roleNumber SERIAL PRIMARY KEY,
    description TEXT UNIQUE NOT NULL
);

-- EmployeeRole
CREATE TABLE employeerole (
    empID BIGINT REFERENCES employees(empID),
    roleNumber INT REFERENCES roles(roleNumber),
    PRIMARY KEY (empID, roleNumber)
);

-- EmploymentContracts
CREATE TABLE employmentcontracts (
    contractID INT PRIMARY KEY,
    minDayShift INT,
    minEveningShift INT,
    sickDays INT,
    daysOff INT,
    ownerID BIGINT REFERENCES employees(empID)
);

-- Users
CREATE TABLE users (
    userID BIGINT PRIMARY KEY REFERENCES employees(empID),
    level VARCHAR(255)
);

-- Constraints
CREATE TABLE constraints (
    constraintID SERIAL,
    empID BIGINT REFERENCES employees(empID),
    ShiftType VARCHAR(255),
    WeekDay VARCHAR(255),
    explanation TEXT,
    date_created DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (constraintID, empID, WeekDay, ShiftType)
);

-- Shifts
CREATE TABLE shifts (
    shiftID INT PRIMARY KEY,
    branchID INT REFERENCES branches(branchID),
    deadline DATE,
    day VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    shiftManager BIGINT REFERENCES employees(empID)
);

ALTER TABLE shifts ADD CONSTRAINT unique_shift_per_day_type_branch
    UNIQUE (deadline, type, branchID);

-- RequiredRoles
CREATE TABLE requiredroles (
    branchID INT REFERENCES branches(branchID),
    shiftID INT REFERENCES shifts(shiftID),
    roleNumber INT REFERENCES roles(roleNumber),
    counter INT,
    PRIMARY KEY (branchID, shiftID, roleNumber)
);

-- ShiftAssignments
CREATE TABLE shiftassignments (
    branchID INT REFERENCES branches(branchID),
    shiftID INT REFERENCES shifts(shiftID),
    empID BIGINT REFERENCES employees(empID),
    roleNumber INT REFERENCES roles(roleNumber)
);
-- Branches
INSERT INTO branches (branchID, name, district) VALUES
(101, 'Test Branch A', 'Test District');

-- Roles
INSERT INTO roles (description) VALUES
('Manager'), ('Cashier');

-- Employees
INSERT INTO employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID)
VALUES (999999999, 'Test Employee', 'test123', 'IL001', 10000, '2024-01-01', 3, 2, 10, 5, 101);

-- Users
INSERT INTO users (userID, level) VALUES
(999999999, 'HRManager');
