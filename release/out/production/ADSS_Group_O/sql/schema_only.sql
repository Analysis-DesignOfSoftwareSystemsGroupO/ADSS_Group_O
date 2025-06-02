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

CREATE TABLE IF NOT EXISTS Roles (
    roleNumber SERIAL,
    description VARCHAR(255) PRIMARY KEY
);

ALTER TABLE roles ADD CONSTRAINT unique_description UNIQUE (description);

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

CREATE TABLE IF NOT EXISTS RequiredRoles (
    branchID INT REFERENCES Branches(branchID),
    shiftID INT REFERENCES Shifts(shiftID),
    roleNumber INT REFERENCES Roles(roleNumber),
    counter INT
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
