
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS empID BIGINT;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS empName VARCHAR(255);
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS empPassword VARCHAR(255);
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS empBankAccount VARCHAR(255);
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS empSalary INT;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS empStartDate DATE;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS minDayShift INT;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS minEveningShift INT;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS sickDays INT;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS daysOff INT;
ALTER TABLE Employees ADD COLUMN IF NOT EXISTS branchID INT;

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

CREATE TABLE IF NOT EXISTS Roles;
CREATE TABLE Roles (
    roleNumber SERIAL PRIMARY KEY,
    description TEXT UNIQUE NOT NULL
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

ALTER TABLE Shifts
ADD CONSTRAINT unique_shift_per_day_type_branch
UNIQUE (deadline, type, branchID);


CREATE TABLE RequiredRoles (
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


-- Insert data into Branches
INSERT INTO Branches (branchID, name, district) VALUES
(0, 'Branch 0', 'North'),
(1, 'Branch 1', 'Center'),
(2, 'Branch 2', 'South'),
(3, 'Branch 3', 'North'),
(4, 'Branch 4', 'Center'),
(5, 'Branch 5', 'South'),
(6, 'Branch 6', 'North'),
(7, 'Branch 7', 'Center'),
(8, 'Branch 8', 'South')
ON CONFLICT (branchID) DO NOTHING;

INSERT INTO Roles (description) VALUES
('Shift Manager'),
('Cashier'),
('Stocker')
ON CONFLICT (roleNumber) DO NOTHING;

INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID) VALUES
(100000001, 'The HR', 'pass123', 'IL001', 12000, '2022-01-10', 4, 2, 10, 12, 0),
(100000002, 'Alice Cohen', 'alice123', 'IL101', 7800, '2023-04-12', 3, 2, 7, 10, 0),
(100000003, 'David Levi', 'david456', 'IL102', 8200, '2022-11-05', 4, 3, 6, 11, 0),
(100000004, 'Rina Azulay', 'rina789', 'IL103', 7900, '2024-01-18', 5, 2, 8, 12, 0),
(200000002, 'Boaz shiftManager', 'pass456', 'IL002', 9500, '2023-03-15', 3, 3, 8, 10, 1),
(200000003, 'Itay Bar', 'itay321', 'IL201', 7300, '2023-06-20', 3, 2, 6, 9, 1),
(200000004, 'Noa Kimchi', 'noa654', 'IL202', 7600, '2022-09-03', 4, 2, 7, 10, 1),
(200000005, 'Gil Peretz', 'gil987', 'IL203', 8100, '2024-02-22', 3, 3, 5, 13, 1),
(300000003, 'Dana the emp', 'shay', 'IL003', 8000, '2021-07-22', 5, 1, 5, 14, 2),
(300000004, 'Shir Ben-David', 'shir111', 'IL301', 7700, '2023-01-25', 4, 2, 9, 11, 2),
(300000005, 'Lior Mor', 'lior222', 'IL302', 7900, '2022-08-14', 3, 2, 6, 12, 2),
(300000006, 'Tamar Green', 'tamar333', 'IL303', 8500, '2023-12-30', 4, 3, 8, 10, 2)
ON CONFLICT (empID) DO NOTHING;



INSERT INTO EmployeeRole (empID, roleNumber) VALUES
(100000001, 1),
(200000002, 2),
(300000003, 3)
ON CONFLICT (empID, roleNumber) DO NOTHING;

INSERT INTO EmploymentContracts (contractID, minDayShift, minEveningShift, sickDays, daysOff, ownerID) VALUES
(1, 4, 2, 10, 12, 100000001),
(2, 3, 3, 8, 10, 200000002),
(3, 5, 1, 5, 14, 300000003)
ON CONFLICT (contractID) DO NOTHING;


INSERT INTO Archived_Employees (empID, archiveDate) VALUES
(400000004, '2023-12-31')
ON CONFLICT (empID) DO NOTHING;

INSERT INTO Users (userID, level) VALUES
(100000001, 'HRManager'),           -- The HR
(100000002, 'shiftManager'),          -- Alice Cohen
(100000003, 'regularEmp'),          -- David Levi
(100000004, 'regularEmp'),          -- Rina Azulay
(200000002, 'shiftManager'),        -- Boaz shiftManager
(200000003, 'HRManager'),          -- Itay Bar
(200000004, 'regularEmp'),          -- Noa Kimchi
(200000005, 'regularEmp'),          -- Gil Peretz
(300000003, 'HRManager'),          -- Dana the emp
(300000004, 'HRManager'),          -- Shir Ben-David
(300000005, 'regularEmp'),          -- Lior Mor
(300000006, 'regularEmp')           -- Tamar Green
ON CONFLICT (userID) DO NOTHING;
