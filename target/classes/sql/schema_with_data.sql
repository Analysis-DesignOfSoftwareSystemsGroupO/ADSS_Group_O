
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

CREATE TABLE IF NOT EXISTS Roles(
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
('Warehouse'),
('Butcher'),
('Baker'),
('Security Guard'),
('Customer Service Representative'),
('Cleaning Staff'),
('Shelf Replenisher'),
('Inventory Manager'),
('Dairy Section Worker'),
('Frozen Goods Worker'),
('Produce Section Worker'),
('Fishmonger'),
('Florist'),
('Delivery Coordinator'),
('Online Orders Picker'),
('Maintenance Technician'),
('HR Representative'),
('Assistant Store Manager'),
('Store Manager'),
('Marketing Promoter'),
('Driver')
ON CONFLICT (description) DO NOTHING;

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
(300000006, 'Tamar Green', 'tamar333', 'IL303', 8500, '2023-12-30', 4, 3, 8, 10, 2),
(400000000, 'Yarden Shalev', 'yarden123', 'IL321', 8810, '2023-11-01', 3, 3, 10, 14, 3),
(400000001, 'Or Peled', 'or456', 'IL367', 8960, '2022-05-11', 5, 3, 10, 14, 3),
(400000002, 'Gal Neeman', 'gal789', 'IL395', 7984, '2022-09-13', 3, 1, 5, 14, 4),
(400000003, 'Talia Ron', 'talia111', 'IL341', 8250, '2023-12-23', 3, 1, 9, 14, 4),
(400000004, 'Yotam Azulay', 'yotam222', 'IL333', 8190, '2022-11-16', 3, 2, 9, 13, 5),
(400000005, 'Roei Michaeli', 'roei333', 'IL373', 8555, '2023-10-04', 5, 1, 9, 12, 5),
(400000006, 'Neta Golan', 'neta444', 'IL363', 8147, '2024-08-02', 3, 2, 7, 9, 6),
(400000007, 'Amit Mor', 'amit555', 'IL367', 8273, '2022-02-20', 3, 1, 5, 11, 6),
(400000008, 'Shani Tal', 'shani666', 'IL349', 8645, '2023-02-26', 3, 2, 7, 10, 7),
(400000009, 'Tom Regev', 'tom777', 'IL341', 8586, '2023-04-07', 3, 3, 8, 10, 7),
(400000010, 'Rotem Saar', 'rotem888', 'IL333', 8212, '2024-11-08', 3, 2, 9, 9, 8),
(400000011, 'Maor Elbaz', 'maor999', 'IL347', 8907, '2024-07-13', 3, 3, 8, 11, 8)
ON CONFLICT (empID) DO NOTHING;



INSERT INTO EmployeeRole (empID, roleNumber) VALUES
(100000002, 2),  -- Alice Cohen - Cashier
(100000002, 1),
(100000002, 7),  -- Customer Service Representative
(100000002, 15), -- Florist
(100000003, 9),  -- David Levi - Shelf Replenisher
(100000003, 10), -- Inventory Manager
(100000003, 11), -- Dairy Section Worker
(100000004, 12), -- Rina Azulay - Frozen Goods Worker
(100000004, 13), -- Produce Section Worker
(100000004, 21), -- Store Manager
(200000002, 1),  -- Boaz - Shift Manager
(200000002, 20), -- Assistant Store Manager
(200000002, 7),  -- Customer Service Representative
(200000003, 2),  -- Itay Bar - Cashier
(200000003, 6),  -- Security Guard
(200000003, 19), -- HR Representative
(200000004, 8),  -- Noa Kimchi - Cleaning Staff
(200000004, 13), -- Produce Section Worker
(200000004, 3),  -- Warehouse
(200000005, 3),  -- Gil Peretz - Warehouse
(200000005, 14), -- Fishmonger
(200000005, 16), -- Delivery Coordinator
(300000003, 3),  -- Dana - Warehouse
(300000003, 17), -- Online Orders Picker
(300000003, 6),  -- Security Guard
(300000004, 2),  -- Shir Ben-David - Cashier
(300000004, 15), -- Florist
(300000004, 7),  -- Customer Service Representative
(300000005, 5),  -- Lior Mor - Baker
(300000005, 10), -- Inventory Manager
(300000005, 18), -- Maintenance Technician
(300000006, 18), -- Tamar Green - Maintenance Technician
(300000006, 19), -- HR Representative
(300000006, 4),  -- Butcher
(400000000, 20),
(400000000, 9),
(400000000, 8),
(400000001, 18),
(400000001, 10),
(400000001, 5),
(400000002, 9),
(400000002, 8),
(400000002, 16),
(400000003, 17),
(400000003, 22),
(400000003, 11),
(400000004, 16),
(400000004, 3),
(400000004, 5),
(400000005, 18),
(400000005, 9),
(400000005, 12),
(400000006, 5),
(400000006, 14),
(400000006, 19),
(400000007, 18),
(400000007, 10),
(400000007, 16),
(400000008, 16),
(400000008, 17),
(400000008, 21),
(400000009, 11),
(400000009, 9),
(400000009, 15),
(400000010, 20),
(400000010, 3),
(400000010, 13),
(400000011, 17),
(400000011, 1),
(400000011, 15)
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
(100000001, 'HRManager'),
(100000002, 'shiftManager'),
(100000003, 'regularEmp'),
(100000004, 'regularEmp'),
(200000002, 'shiftManager'),
(200000003, 'HRManager'),
(200000004, 'regularEmp'),
(200000005, 'regularEmp'),
(300000003, 'HRManager'),
(300000004, 'HRManager'),
(300000005, 'regularEmp'),
(300000006, 'regularEmp'),
(400000000, 'regularEmp'),
(400000001, 'regularEmp'),
(400000002, 'regularEmp'),
(400000003, 'regularEmp'),
(400000004, 'regularEmp'),
(400000005, 'regularEmp'),
(400000006, 'regularEmp'),
(400000007, 'regularEmp'),
(400000008, 'regularEmp'),
(400000009, 'regularEmp'),
(400000010, 'regularEmp'),
(400000011, 'regularEmp')
ON CONFLICT (userID) DO NOTHING;


-- טבלת משמרות חדשה לשבוע 1.6–7.6 עבור סניף 0
INSERT INTO Shifts (shiftID, branchID, deadline, day, type, status, shiftManager) VALUES
(100, 0, '2025-06-01', 'Sunday', 'Morning', 'Empty', 100000002),
(101, 0, '2025-06-01', 'Sunday', 'Evening', 'Problem', 100000002),
(102, 0, '2025-06-02', 'Monday', 'Morning', 'Full', 100000002),
(103, 0, '2025-06-02', 'Monday', 'Evening', 'Problem', 100000002),
(104, 0, '2025-06-03', 'Tuesday', 'Morning', 'Full', 100000002),
(105, 0, '2025-06-03', 'Tuesday', 'Evening', 'Empty', 100000002),
(106, 0, '2025-06-04', 'Wednesday', 'Morning', 'Problem', 100000002),
(107, 0, '2025-06-04', 'Wednesday', 'Evening', 'Empty', 100000002),
(108, 0, '2025-06-05', 'Thursday', 'Morning', 'Full', 100000002),
(109, 0, '2025-06-05', 'Thursday', 'Evening', 'Problem', 100000002),
(110, 0, '2025-06-06', 'Friday', 'Morning', 'Full', 100000002),
(111, 0, '2025-06-06', 'Friday', 'Evening', 'Problem', 100000002),
(112, 0, '2025-06-07', 'Saturday', 'Morning', 'Empty', 100000002)
ON CONFLICT DO NOTHING;

-- RequiredRoles - הגדרת תפקידים נדרשים לכל משמרת לדוגמה
INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) VALUES
(0, 100, 1, 1), -- Shift Manager
(0, 100, 2, 2), -- Cashier
(0, 101, 3, 1),
(0, 102, 7, 1),
(0, 103, 9, 2),
(0, 104, 10, 1),
(0, 105, 11, 1),
(0, 106, 12, 1),
(0, 107, 13, 1),
(0, 108, 14, 1),
(0, 109, 15, 1),
(0, 110, 16, 1),
(0, 111, 17, 1),
(0, 112, 18, 1)
ON CONFLICT DO NOTHING;


-- ShiftAssignments - שיבוץ עובדים מסניף 0 בלבד
INSERT INTO ShiftAssignments (branchID, shiftID, empID, roleNumber) VALUES
(0, 100, 100000002, 1),
(0, 100, 100000003, 2),
(0, 101, 100000004, 3),
(0, 102, 100000002, 7),
(0, 103, 100000003, 9),
(0, 104, 100000004, 10),
(0, 105, 100000002, 11),
(0, 106, 100000003, 12),
(0, 107, 100000004, 13),
(0, 108, 100000002, 14),
(0, 109, 100000003, 15),
(0, 110, 100000004, 16),
(0, 111, 100000002, 17),
(0, 112, 100000003, 18)
ON CONFLICT DO NOTHING;


