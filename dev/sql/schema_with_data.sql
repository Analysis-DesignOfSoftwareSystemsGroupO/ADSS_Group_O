
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
    roleNumber INT,
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
    shiftID SERIAL PRIMARY KEY,
    branchID INT REFERENCES Branches(branchID),
    deadline DATE,
    day VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    shiftManager BIGINT REFERENCES Employees(empID)
);

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
('Marketing Promoter')
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
(100000002, 2),
(100000002, 1), --hr1
(100000002, 7),
(100000002, 15),
(100000003, 9),
(100000003, 10),
(100000003, 11),
(100000004, 12),
(100000004, 13),
(100000004, 21),
(200000002, 1),  --hr2
(200000002, 20),
(200000002, 7),
(200000003, 2),
(200000003, 6),
(200000003, 19),
(200000004, 8),
(200000004, 13),
(200000004, 3),
(200000005, 3),
(200000005, 14),
(200000005, 16),
(300000003, 1),  --hr3
(300000004, 17),
(300000004, 6),
(300000004, 2),
(300000004, 15),
(300000004, 7),
(300000005, 5),
(300000005, 18),
(300000006, 18),
(300000006, 19),
(300000006, 4),
(400000000, 1),--hr4
(400000001, 9),
(400000001, 8),
(400000001, 18),
(400000001, 10),
(400000001, 5),
(400000002, 1), --hr5
(400000003, 8),
(400000003, 16),
(400000003, 17),
(400000003, 22),
(400000003, 11),
(400000004, 1), --hr6
(400000005, 3),
(400000005, 5),
(400000005, 18),
(400000005, 9),
(400000005, 12),
(400000006, 6), --hr6
(400000007, 14),
(400000007, 19),
(400000007, 18),
(400000007, 10),
(400000007, 16),
(400000008, 1), --hr7
(400000009, 17),
(400000009, 21),
(400000009, 11),
(400000009, 9),
(400000009, 15),
(400000010, 1), --hr8
(400000011, 3),
(400000011, 13),
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
(300000004, 'regularEmp'),
(300000005, 'regularEmp'),
(300000006, 'regularEmp'),
(400000000, 'HRManager'),
(400000001, 'regularEmp'),
(400000002, 'HRManager'),
(400000003, 'regularEmp'),
(400000004, 'HRManager'),
(400000005, 'regularEmp'),
(400000006, 'regularEmp'),
(400000007, 'regularEmp'),
(400000008, 'HRManager'),
(400000009, 'regularEmp'),
(400000010, 'HRManager'),
(400000011, 'regularEmp')
ON CONFLICT (userID) DO NOTHING;


