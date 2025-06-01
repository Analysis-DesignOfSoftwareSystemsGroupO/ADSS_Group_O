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
    roleNumber INT PRIMARY KEY,
    description VARCHAR(255)
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

CREATE TABLE IF NOT EXISTS Constraints (
    constraintID SERIAL PRIMARY KEY,
    empID BIGINT REFERENCES Employees(empID),
    ShiftType VARCHAR(255),
    WeekDay VARCHAR(255),
    explanation TEXT,
      date_created DATE DEFAULT  CURRENT_TIMESTAMP
);

ALTER TABLE Constraints
ADD COLUMN date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

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

INSERT INTO Branches (branchID, name, district) VALUES
(1, 'Branch 1', 'North'),
(2, 'Branch 2', 'Center'),
(3, 'Branch 3', 'South'),
(4, 'Branch 4', 'North'),
(5, 'Branch 5', 'Center'),
(6, 'Branch 6', 'South'),
(7, 'Branch 7', 'North'),
(8, 'Branch 8', 'Center'),
(9, 'Branch 9', 'South')
ON CONFLICT (branchID) DO NOTHING;

-- Additional data for demonstration
INSERT INTO Roles (roleNumber, description) VALUES
(101, 'Shift Manager'),
(102, 'Warehouse'),
(103, 'Driver')
ON CONFLICT (roleNumber) DO NOTHING;

INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID)
SELECT * FROM (VALUES
(111111111, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 1),
(222222222, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 2),
(333333333, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 3),
(444444444, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 4),
(555555555, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 5),
(666666666, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 6),
(777777777, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 7),
(888888888, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 8),
(999999999, 'admin', 'admin', NULL, CAST(NULL AS INTEGER), CAST(NULL AS DATE), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), CAST(NULL AS INTEGER), 9)
) AS vals(empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID)
ON CONFLICT (empID) DO NOTHING;

INSERT INTO Users (userID, level) VALUES
(111111111, 'HRManager'),
(222222222, 'HRManager'),
(333333333, 'HRManager'),
(444444444, 'HRManager'),
(555555555, 'HRManager'),
(666666666, 'HRManager'),
(777777777, 'HRManager'),
(888888888, 'HRManager'),
(999999999, 'HRManager')
ON CONFLICT (userID) DO NOTHING;