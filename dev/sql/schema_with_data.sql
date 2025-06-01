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
    explanation TEXT
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
(9, 'Branch 9', 'South')
ON CONFLICT (branchID) DO NOTHING;

INSERT INTO Roles (roleNumber, description) VALUES
(101, 'Shift Manager'),
(102, 'Cashier'),
(103, 'Stocker')
ON CONFLICT (roleNumber) DO NOTHING;

INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID) VALUES
(100000001, 'The HR', 'pass123', 'IL001', 12000, '2022-01-10', 4, 2, 10, 12, 1),
(200000002, 'Boaz shiftManager', 'pass456', 'IL002', 9500, '2023-03-15', 3, 3, 8, 10, 2),
(300000003, 'Dana the emp', 'pass789', 'IL003', 8000, '2021-07-22', 5, 1, 5, 14, 3)
ON CONFLICT (empID) DO NOTHING;

INSERT INTO EmployeeRole (empID, roleNumber) VALUES
(100000001, 102),
(200000002, 101),
(300000003, 103)
ON CONFLICT (empID, roleNumber) DO NOTHING;

INSERT INTO EmploymentContracts (contractID, minDayShift, minEveningShift, sickDays, daysOff, ownerID) VALUES
(1, 4, 2, 10, 12, 1),
(2, 3, 3, 8, 10, 2),
(3, 5, 1, 5, 14, 3)
ON CONFLICT (contractID) DO NOTHING;

INSERT INTO Shifts (shiftID, branchID, deadline, day, type, status, shiftManager) VALUES
(1001, 1, '2024-06-10', 'Monday', 'Morning', 'Planned', 1),
(1002, 2, '2024-06-11', 'Tuesday', 'Evening', 'Planned', 2)
ON CONFLICT (shiftID) DO NOTHING;

INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) VALUES
(1, 1001, 102, 2),
(2, 1002, 103, 1)
ON CONFLICT DO NOTHING;

INSERT INTO ShiftAssignments (branchID, shiftID, empID, roleNumber) VALUES
(1, 1001, 200000002, 102),
(2, 1002, 300000003, 103)
ON CONFLICT DO NOTHING;

INSERT INTO Archived_Employees (empID, archiveDate) VALUES
(400000004, '2023-12-31')
ON CONFLICT (empID) DO NOTHING;

INSERT INTO Users (userID, level) VALUES
(100000001, 'HRManager'),
(200000002, 'shiftManager'),
(300000003, 'regularEmp')
ON CONFLICT (userID) DO NOTHING;
