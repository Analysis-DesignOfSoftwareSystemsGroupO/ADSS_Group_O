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