package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.ShiftController;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ShiftManager class.
 */
public class ShiftControllerTest {

    private ShiftController shiftController;
    private Shift shift;
    private Employee employee;
    private User managerUser;
    private RoleControllerMock roleManagerMock;

    @BeforeEach
    public void setUp() {
        // Initialize test environment before each test
        roleManagerMock = new RoleControllerMock();
        shiftController = new ShiftController(roleManagerMock);

        shift = new Shift(WeekDay.MONDAY, ShiftType.MORNING);
        employee = new Employee("John Doe", 123456789, "pass", "bank", 5000,
                java.time.LocalDate.now(), 5, 5, 10, 10);

        managerUser = new User(employee, Level.HRManager);
    }

    /**
     * Test assigning an employee to a shift successfully.
     */
    @Test
    public void testAssignEmployeeToShift_Success() {
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, role);

        shiftController.assignEmployeeToShift(managerUser, shift, employee, role);

        assertTrue(shift.getEmployees().contains(employee));
    }

    /**
     * Test that assigning an employee with insufficient permissions throws SecurityException.
     */
    @Test
    public void testAssignEmployeeToShift_AccessDenied() {
        User regularUser = new User(employee, Level.regularEmp);
        Role role = new Role("Cashier");

        assertThrows(SecurityException.class, () -> {
            shiftController.assignEmployeeToShift(regularUser, shift, employee, role);
        });
    }

    /**
     * Test removing an employee from a shift successfully.
     */
    @Test
    public void testRemoveEmployeeFromShift_Success() {
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, role);
        shift.addEmployee(managerUser, employee, role);

        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftController.removeEmployeeFromShift(managerUser, shift);

        assertFalse(shift.getEmployees().contains(employee));
    }

    /**
     * Test removing a role from a shift successfully.
     */
    @Test
    public void testRemoveRoleFromShift_Success() {
        Role cashier = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, cashier);

        String input = cashier.getRoleNumber() + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftController.removeRoleFromShift(managerUser, shift);

        assertFalse(shift.getNecessaryRoles().contains(cashier));
    }

    /**
     * Test that assigning the same employee twice to a shift throws IllegalArgumentException.
     */
    @Test
    public void testAssignSameEmployeeTwice() {
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, role);

        shiftController.assignEmployeeToShift(managerUser, shift, employee, role);

        assertThrows(IllegalArgumentException.class, () -> {
            shiftController.assignEmployeeToShift(managerUser, shift, employee, role);
        });
    }

    /**
     * Test that assigning an employee with a non-manager user throws SecurityException.
     */
    @Test
    public void testAssignEmployeeWithNonManagerUser() {
        User regularUser = new User(employee, Level.regularEmp);
        Role role = new Role("Cashier");

        assertThrows(SecurityException.class, () -> {
            shiftController.assignEmployeeToShift(regularUser, shift, employee, role);
        });
    }

    /**
     * Test printing a shift successfully with authorized user.
     */
    @Test
    public void testPrintShift_Success() {
        assertDoesNotThrow(() -> shiftController.printShift(managerUser, shift));
    }

    /**
     * Test removing an employee from an empty shift does not throw an exception.
     */
    @Test
    public void testRemoveEmployeeFromEmptyShift() {
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertDoesNotThrow(() -> shiftController.removeEmployeeFromShift(managerUser, shift));
    }

    /**
     * Test removing a role from a shift with no roles does not throw an exception.
     */
    @Test
    public void testRemoveRoleFromEmptyShift() {
        String input = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertDoesNotThrow(() -> shiftController.removeRoleFromShift(managerUser, shift));
    }
    /**
     * Test printing a shift with unauthorized user throws SecurityException.
     */
    @Test
    public void testPrintShift_AccessDenied() {
        User regularUser = new User(employee, Level.regularEmp);

        assertThrows(SecurityException.class, () -> shiftController.printShift(regularUser, shift));
    }

    /**
     * Test assigning an employee with a role not in the shift's necessaryRoles throws an exception.
     */
    @Test
    public void testAssignEmployeeWithoutRoleInShift() {
        Role cashier = new Role("Cashier");

        assertThrows(IllegalArgumentException.class, () -> {
            shiftController.assignEmployeeToShift(managerUser, shift, employee, cashier);
        });
    }
}