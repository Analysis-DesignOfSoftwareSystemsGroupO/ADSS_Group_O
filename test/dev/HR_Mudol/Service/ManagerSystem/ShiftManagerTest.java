package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.ShiftManager;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftManagerTest {

    private ShiftManager shiftManager;
    private Shift shift;
    private Employee employee;
    private User managerUser;
    private RoleManagerMock roleManagerMock;

    @BeforeEach
    public void setUp() {
        // Initialize test environment
        roleManagerMock = new RoleManagerMock();
        shiftManager = new ShiftManager(roleManagerMock);

        shift = new Shift(WeekDay.MONDAY, ShiftType.MORNING);
        employee = new Employee("John Doe", 123456789, "pass", "bank", 5000,
                java.time.LocalDate.now(), 5, 5, 10, 10);

        managerUser = new User(employee, Level.HRManager);
    }

    @Test
    public void testAssignEmployeeToShift_Success() {
        // Fix: first add role to shift's necessaryRoles
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, role);

        shiftManager.assignEmployeeToShift(managerUser, shift, employee, role);

        assertTrue(shift.getEmployees().contains(employee));
    }

    @Test
    public void testAssignEmployeeToShift_AccessDenied() {
        User regularUser = new User(employee, Level.regularEmp);
        Role role = new Role("Cashier");

        // No need to add role — testing access
        assertThrows(SecurityException.class, () -> {
            shiftManager.assignEmployeeToShift(regularUser, shift, employee, role);
        });
    }

    @Test
    public void testRemoveEmployeeFromShift_Success() {
        // Fix: add role to necessaryRoles before adding employee
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, role);
        shift.addEmployee(managerUser, employee, role);

        String input = "1\n"; // Choose first employee
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftManager.removeEmployeeFromShift(managerUser, shift);

        assertFalse(shift.getEmployees().contains(employee));
    }

    @Test
    public void testRemoveRoleFromShift_Success() {
        Role cashier = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, cashier);

        String input = cashier.getRoleNumber() + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftManager.removeRoleFromShift(managerUser, shift);

        assertFalse(shift.getNecessaryRoles().contains(cashier));
    }

    @Test
    public void testChooseRelevantRoleForShift_Success() {
        // Add a new role manually to the mock
        Role cashier = new Role("Cashier");
        roleManagerMock.addMockRole(cashier);

        String input = cashier.getRoleNumber() + "\nD\n"; // select role and finish
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftManager.chooseRelevantRoleForShift(managerUser, shift);

        assertFalse(shift.getNecessaryRoles().isEmpty());
    }


    @Test
    public void testAssignSameEmployeeTwice() {
        // Fix: add role before assigning
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, role);

        shiftManager.assignEmployeeToShift(managerUser, shift, employee, role);

        // Trying to assign again the same employee
        assertThrows(IllegalArgumentException.class, () -> {
            shiftManager.assignEmployeeToShift(managerUser, shift, employee, role);
        });
    }

    @Test
    public void testAssignEmployeeWithNonManagerUser() {
        User regularUser = new User(employee, Level.regularEmp);
        Role role = new Role("Cashier");

        assertThrows(SecurityException.class, () -> {
            shiftManager.assignEmployeeToShift(regularUser, shift, employee, role);
        });
    }

    @Test
    public void testPrintShift_Success() {
        assertDoesNotThrow(() -> shiftManager.printShift(managerUser, shift));
    }
}
