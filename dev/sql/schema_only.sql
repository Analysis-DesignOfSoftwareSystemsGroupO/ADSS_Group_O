DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS Branches (
    branchID INT PRIMARY KEY,
    name VARCHAR(255),
    district VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Employees (
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
    branchID INT REFERENCES Branches(branchID)
);

CREATE TABLE IF NOT EXISTS Branches (
    branchID INT PRIMARY KEY,
    name VARCHAR(255),
    district VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Employees (
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
    branchID INT REFERENCES Branches(branchID)
);

CREATE TABLE IF NOT EXISTS Roles(
    roleNumber SERIAL PRIMARY KEY,
    description TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS EmployeeRole (
    empID BIGINT REFERENCES Employees(empID),
    roleNumber INT REFERENCES Roles(roleNumber),
    PRIMARY KEY (empID, roleNumber)
);

CREATE TABLE IF NOT EXISTS EmploymentContracts (
    contractID INT PRIMARY KEY,
    minDayShift INT,
    minEveningShift INT,
    sickDays INT,
    daysOff INT,
    ownerID BIGINT REFERENCES Employees(empID)
);
CREATE TABLE IF NOT EXISTS Users (
    userID BIGINT PRIMARY KEY REFERENCES Employees(empID),
    level VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS constraints (
    constraintID SERIAL,
    empID BIGINT REFERENCES Employees(empID),
    ShiftType VARCHAR(255),
    WeekDay VARCHAR(255),
    explanation TEXT,
    date_created DATE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (constraintID, empID, WeekDay, ShiftType)
);

CREATE TABLE IF NOT EXISTS Shifts (
    shiftID INT PRIMARY KEY,
    branchID INT REFERENCES Branches(branchID),
    deadline DATE,
    day VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    shiftManager BIGINT REFERENCES Employees(empID)
);

ALTER TABLE Shifts
ADD CONSTRAINT unique_shift_per_day_type_branch
UNIQUE (deadline, type, branchID);


CREATE TABLE iF NOT EXISTS RequiredRoles (
    branchID INT REFERENCES Branches(branchID),
    shiftID INT REFERENCES Shifts(shiftID),
    roleNumber INT REFERENCES Roles(roleNumber),
    counter INT,
    PRIMARY KEY (branchID, shiftID, roleNumber)
);

CREATE TABLE IF NOT EXISTS ShiftAssignments (
    branchID INT REFERENCES Branches(branchID),
    shiftID INT REFERENCES Shifts(shiftID),
    empID BIGINT REFERENCES Employees(empID),
    roleNumber INT REFERENCES Roles(roleNumber)
);

CREATE TABLE IF NOT EXISTS Archived_Employees (
    empID BIGINT PRIMARY KEY,
    archiveDate DATE
);

INSERT INTO Branches (branchID, name, district) VALUES
(0, 'Branch 1', 'North'),
(1, 'Branch 2', 'Center'),
(2, 'Branch 3', 'South'),
(3, 'Branch 4', 'North'),
(4, 'Branch 5', 'Center'),
(5, 'Branch 6', 'South'),
(6, 'Branch 7', 'North'),
(7, 'Branch 8', 'Center'),
(8, 'Branch 9', 'South')
ON CONFLICT (branchID) DO NOTHING;

INSERT INTO Roles (roleNumber, description) VALUES
(101, 'Shift Manager'),
(102, 'Cashier'),
(103, 'Warehouse'),
ON CONFLICT (roleNumber) DO NOTHING;

INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID) VALUES
(100000001, 'The HR', 'pass123', 'IL001', 12000, '2022-01-10', 4, 2, 10, 12, 1);

INSERT INTO Users (userID, level) VALUES
(100000001, 'HRManager');