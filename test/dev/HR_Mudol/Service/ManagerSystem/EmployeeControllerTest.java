package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.EmployeeController;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeControllerTest {

    private Branch branch;
    private EmployeeController employeeController;
    private Employee hrEmployee;
    private User hrUser;
    private RoleControllerMock roleManagerMock;

    // Setup method to initialize a clean branch and HR user before each test
    @BeforeEach
    public void setUp() {
        branch = new Branch();
        branch.getEmployees().clear();
        branch.getUsers().clear();

        employeeController = new EmployeeController(branch);

        hrEmployee = new Employee("HR Manager", 123456789, "password", "bank123", 10000,
                LocalDate.of(2020, 1, 1), 5, 5, 10, 15);
        hrUser = new User(hrEmployee, Level.HRManager);

        branch.getEmployees().add(hrEmployee);
        branch.getUsers().add(hrUser);

        roleManagerMock = new RoleControllerMock();
        employeeController.setRoleManager(roleManagerMock);
    }

    // Test adding a new employee through user input simulation
    @Test
    public void testAddEmployee() {
        String input = """
            John Doe
            111111111
            password123
            bankAccount123
            5000
            5
            5
            10
            15
            """;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.addEmployee(hrUser);

        assertEquals(2, branch.getEmployees().size());
        assertEquals(2, branch.getUsers().size());
    }

    // Test removing an employee from the branch
    @Test
    public void testRemoveEmployee() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.removeEmployee(hrUser);

        assertEquals(1, branch.getEmployees().size());
        assertEquals(1, branch.getUsers().size());
        assertTrue(branch.getOldEmployee().contains(employee));
    }

    // Test updating employee's bank account
    @Test
    public void testUpdateBankAccount() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\nnewBankAccount\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.updateBankAccount(hrUser);
        assertEquals("newBankAccount", employee.getEmpBankAccount());
    }

    // Test updating employee's salary
    @Test
    public void testUpdateSalary() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n6000\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.updateSalary(hrUser);
        assertEquals(6000, employee.getEmpSalary());
    }

    // Test updating minimum required day shifts
    @Test
    public void testUpdateMinDayShift() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.updateMinDayShift(hrUser);
        assertEquals(7, employee.getContract(hrUser).getMinDayShift(hrUser, employee));
    }

    // Test updating minimum required evening shifts
    @Test
    public void testUpdateMinEveningShift() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n8\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.updateMinEveningShift(hrUser);
        assertEquals(8, employee.getContract(hrUser).getMinEveninigShift(hrUser, employee));
    }

    // Test setting initial sick days
    @Test
    public void testSetInitialSickDays() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n12\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.setInitialsickDays(hrUser);
        assertEquals(12, employee.getContract(hrUser).getSickDays(hrUser, employee));
    }

    // Test setting initial days off
    @Test
    public void testSetInitialDaysOff() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n14\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.setInitialdaysOff(hrUser);
        assertEquals(14, employee.getContract(hrUser).getDaysOff(hrUser, employee));
    }

    // Test fetching employee by ID (success case)
    @Test
    public void testGetEmployeeByIdSuccess() {
        Employee employee = createEmployeeAndAddToBranch();
        Employee found = employeeController.getEmployeeById(hrUser, employee.getEmpId());
        assertEquals(employee, found);
    }


    // Test that regular employees cannot access HR management functions
    @Test
    public void testAccessDeniedForNonManager() {
        User regularUser = new User(hrEmployee, Level.regularEmp);
        assertThrows(SecurityException.class, () -> {
            employeeController.printAllEmployees(regularUser);
        });
    }

    // Helper method to create and add a test employee
    private Employee createEmployeeAndAddToBranch() {
        Employee employee = new Employee("John", 111111111, "pass", "bank", 5000,
                LocalDate.now(), 5, 5, 10, 10);
        branch.getEmployees().add(employee);
        branch.getUsers().add(new User(employee, Level.regularEmp));
        return employee;
    }
    // Attempt to add employee with duplicate ID
    @Test
    public void testAddEmployeeWithDuplicateId() {
        createEmployeeAndAddToBranch(); // Add employee with 111111111

        String input = """
            John Doe
            111111111
            222222222
            password123
            bankAccount123
            5000
            5
            5
            10
            15
            """;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.addEmployee(hrUser);

        assertEquals(3, branch.getEmployees().size()); // HR + original + second valid
    }

    // Attempt to remove employee with invalid ID
    @Test
    public void testRemoveEmployeeInvalidId() {
        String input = "999999999\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeController.setScanner(new Scanner(in));

        employeeController.removeEmployee(hrUser);

        // No one removed, HR Manager still exists
        assertEquals(1, branch.getEmployees().size());
    }


    // Non-manager tries to update salary
    @Test
    public void testUpdateSalaryByNonManager() {
        Employee employee = createEmployeeAndAddToBranch();
        User regularUser = new User(employee, Level.regularEmp);

        assertThrows(SecurityException.class, () -> {
            employeeController.updateSalary(regularUser);
        });
    }

}
