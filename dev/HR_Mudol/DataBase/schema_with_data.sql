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

INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, minDayShift, minEveningShift, sickDays, daysOff, branchID) VALUES
(100000001, 'Alice Levi', 'pass123', '123-456', 10000, '2022-01-15', 2, 2, 5, 3, 1),
(100000002, 'David Cohen', 'pass456', '789-101', 9500, '2021-03-10', 3, 2, 2, 2, 1),
(100000003, 'Sara Azulai', 'pass789', '202-303', 10500, '2023-07-01', 2, 1, 4, 5, 2);

INSERT INTO Roles (roleNumber, description) VALUES
(1, 'Shift Manager'),
(2, 'Cashier'),
(3, 'Stocker'),
(4, 'Driver');

INSERT INTO EmployeeRole (empID, roleNumber) VALUES
(100000001, 1),
(100000001, 2),
(100000002, 2),
(100000003, 3);

INSERT INTO Users (userID, level, empID) VALUES
(1, 'HR_MANAGER', 100000001),
(2, 'SHIFT_MANAGER', 100000002),
(3, 'EMPLOYEE', 100000003);

INSERT INTO Shifts (shiftID, branchID, deadline, day, type, status, shiftManager) VALUES
(501, 1, '2025-04-25', 'SUNDAY', 'MORNING', 'PLANNED', 100000001),
(502, 1, '2025-04-25', 'MONDAY', 'EVENING', 'OPEN', 100000002),
(503, 2, '2025-04-25', 'TUESDAY', 'MORNING', 'CONFIRMED', 100000001);

INSERT INTO Constraints (empID, weekID, ShiftType, WeekDay, explanation) VALUES
(100000001, 1, 'MORNING', 'SUNDAY', 'Doctor appointment'),
(100000002, 1, 'EVENING', 'MONDAY', 'Family event');

INSERT INTO RequiredRoles (branchID, shiftID, roleNumber, counter) VALUES
(1, 501, 2, 2),
(1, 502, 3, 1),
(2, 503, 4, 1);

INSERT INTO ShiftAssignments (branchID, shiftID, empID, roleNumber) VALUES
(1, 501, 100000001, 2),
(1, 502, 100000002, 3),
(2, 503, 100000003, 4);

INSERT INTO Archived_Employees (empID, archiveDate) VALUES
(999, '2023-12-31');
