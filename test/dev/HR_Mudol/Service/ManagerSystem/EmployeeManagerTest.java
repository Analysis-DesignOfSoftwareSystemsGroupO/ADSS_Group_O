package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.EmployeeManager;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeManagerTest {

    private Branch branch;
    private EmployeeManager employeeManager;
    private Employee hrEmployee;
    private User hrUser;
    private RoleManagerMock roleManagerMock;

    @BeforeEach
    public void setUp() {
        branch = new Branch();
        branch.getEmployees().clear(); // ריקון העובדים
        branch.getUsers().clear();      // ריקון המשתמשים

        employeeManager = new EmployeeManager(branch);

        hrEmployee = new Employee("HR Manager", 123456789, "password", "bank123", 10000,
                LocalDate.of(2020, 1, 1), 5, 5, 10, 15);
        hrUser = new User(hrEmployee, Level.HRManager);

        branch.getEmployees().add(hrEmployee);
        branch.getUsers().add(hrUser);

        roleManagerMock = new RoleManagerMock();
        employeeManager.setRoleManager(roleManagerMock);
    }

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
        employeeManager.setScanner(new Scanner(in));

        employeeManager.addEmployee(hrUser);

        assertEquals(2, branch.getEmployees().size());
        assertEquals(2, branch.getUsers().size());
    }

    @Test
    public void testRemoveEmployee() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.removeEmployee(hrUser);

        assertEquals(1, branch.getEmployees().size());
        assertEquals(1, branch.getUsers().size());
        assertTrue(branch.getOldEmployee().contains(employee));
    }

    @Test
    public void testUpdateBankAccount() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\nnewBankAccount\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.updateBankAccount(hrUser);
        assertEquals("newBankAccount", employee.getEmpBankAccount());
    }

    @Test
    public void testUpdateSalary() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n6000\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.updateSalary(hrUser);
        assertEquals(6000, employee.getEmpSalary());
    }

    @Test
    public void testUpdateMinDayShift() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.updateMinDayShift(hrUser);
        assertEquals(7, employee.getContract(hrUser).getMinDayShift(hrUser, employee));
    }

    @Test
    public void testUpdateMinEveningShift() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n8\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.updateMinEveningShift(hrUser);
        assertEquals(8, employee.getContract(hrUser).getMinEveninigShift(hrUser, employee));
    }

    @Test
    public void testSetInitialSickDays() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n12\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.setInitialsickDays(hrUser);
        assertEquals(12, employee.getContract(hrUser).getSickDays(hrUser, employee));
    }

    @Test
    public void testSetInitialDaysOff() {
        Employee employee = createEmployeeAndAddToBranch();
        String input = "111111111\n14\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        employeeManager.setScanner(new Scanner(in));

        employeeManager.setInitialdaysOff(hrUser);
        assertEquals(14, employee.getContract(hrUser).getDaysOff(hrUser, employee));
    }

    @Test
    public void testGetEmployeeByIdSuccess() {
        Employee employee = createEmployeeAndAddToBranch();
        Employee found = employeeManager.getEmployeeById(hrUser, employee.getEmpId());
        assertEquals(employee, found);
    }

    @Test
    public void testGetEmployeeByIdInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            employeeManager.getEmployeeById(hrUser, 123);
        });
    }

    @Test
    public void testAccessDeniedForNonManager() {
        User regularUser = new User(hrEmployee, Level.regularEmp);
        assertThrows(SecurityException.class, () -> {
            employeeManager.printAllEmployees(regularUser);
        });
    }

    private Employee createEmployeeAndAddToBranch() {
        Employee employee = new Employee("John", 111111111, "pass", "bank", 5000,
                LocalDate.now(), 5, 5, 10, 10);
        branch.getEmployees().add(employee);
        branch.getUsers().add(new User(employee, Level.regularEmp));
        return employee;
    }
}
