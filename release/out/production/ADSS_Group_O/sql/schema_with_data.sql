CREATE TABLE Branches (
    branchID INT PRIMARY KEY,
    name VARCHAR(255),
    district VARCHAR(255)
);

CREATE TABLE Employees (
    empID INT PRIMARY KEY,
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

CREATE TABLE Roles (
    roleNumber INT PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE EmployeeRole (
    empID INT REFERENCES Employees(empID),
    roleNumber INT REFERENCES Roles(roleNumber),
    PRIMARY KEY (empID, roleNumber)
);

CREATE TABLE EmploymentContracts (
    contractID INT PRIMARY KEY,
    minDayShift INT,
    minEveningShift INT,
    sickDays INT,
    daysOff INT,
    ownerID INT REFERENCES Employees(empID)
);

CREATE TABLE Users (
    userID INT PRIMARY KEY REFERENCES Employees(empID),
    level VARCHAR(255)
);

CREATE TABLE EmployeeRole (
    empID INT REFERENCES Employees(empID),
    roleNumber INT REFERENCES Roles(roleNumber),
    PRIMARY KEY (empID, roleNumber)
);

CREATE TABLE Constraints (
    constraintID SERIAL PRIMARY KEY,
    empID INT REFERENCES Employees(empID),
    ShiftType VARCHAR(255),
    WeekDay VARCHAR(255),
    explanation TEXT
);

CREATE TABLE Shifts (
    shiftID INT PRIMARY KEY,
    branchID INT REFERENCES Branches(branchID),
    deadline DATE,
    day VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    shiftManager INT REFERENCES Employees(empID)
);

CREATE TABLE RequiredRoles (
    branchID INT REFERENCES Branches(branchID),
    shiftID INT REFERENCES Shifts(shiftID),
    roleNumber INT REFERENCES Roles(roleNumber),
    counter INT
);

CREATE TABLE ShiftAssignments (
    branchID INT REFERENCES Branches(branchID),
    shiftID INT REFERENCES Shifts(shiftID),
    empID INT REFERENCES Employees(empID),
    roleNumber INT REFERENCES Roles(roleNumber)
);

CREATE TABLE Archived_Employees (
    empID INT PRIMARY KEY,
    archiveDate DATE
);

-- Insert data into Branches
INSERT INTO Branches (branchID, name, district) VALUES
(1, 'Branch 1', 'North'),
(2, 'Branch 2', 'Center'),
(3, 'Branch 3', 'South'),
(4, 'Branch 4', 'North'),
(5, 'Branch 5', 'Center'),
(6, 'Branch 6', 'South'),
(7, 'Branch 7', 'North'),
(8, 'Branch 8', 'Center'),
(9, 'Branch 9', 'South');

-- Additional data for demonstration
INSERT INTO Roles (roleNumber, description) VALUES
(101, 'Manager'),
(102, 'Cashier'),
(103, 'Stocker');

INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID) VALUES
(1, 'Alice Cohen', 'pass123', 'IL001', 12000, '2022-01-10', 4, 2, 10, 12, 1),
(2, 'Boaz Levi', 'pass456', 'IL002', 9500, '2023-03-15', 3, 3, 8, 10, 2),
(3, 'Dana Shalev', 'pass789', 'IL003', 8000, '2021-07-22', 5, 1, 5, 14, 3);

INSERT INTO EmployeeRole (empID, roleNumber) VALUES
(1, 101),
(2, 102),
(3, 103);

INSERT INTO EmploymentContracts (contractID, minDayShift, minEveningShift, sickDays, daysOff, ownerID) VALUES
(1, 4, 2, 10, 12, 1),
(2, 3, 3, 8, 10, 2),
(3, 5, 1, 5, 14, 3);

INSERT INTO Users (userID, level) VALUES
(1, 'HR_MANAGER'),
(2, 'SHIFT_MANAGER'),
(3, 'REGULAR_EMP');


INSERT INTO Shifts (shiftID, branchID, deadline, day, type, status, shiftManager) VALUES
(1001, 1, '2024-06-10', 'Monday', 'Morning', 'Planned', 1),
(1002, 2, '2024-06-11', 'Tuesday', 'Evening', 'Planned', 2);

INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) VALUES
(1, 1001, 102, 2),
(2, 1002, 103, 1);

INSERT INTO ShiftAssignments (branchID, shiftID, empID, roleNumber) VALUES
(1, 1001, 2, 102),
(2, 1002, 3, 103);

INSERT INTO Archived_Employees (empID, archiveDate) VALUES
(4, '2023-12-31');
