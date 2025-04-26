package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {

    private Shift shift;
    private Employee hrEmployee;
    private User hrUser;
    private Employee shiftManagerEmp;
    private User shiftManagerUser;
    private Employee regularEmp;
    private User regularUser;
    private Role sampleRole;

    @BeforeEach
    public void setUp() {
        shift = new Shift(WeekDay.TUESDAY, ShiftType.MORNING);

        hrEmployee = new Employee("HR", 100000001, "pass", "bank", 9000,
                java.time.LocalDate.now(), 2, 2, 10, 5);
        hrUser = new User(hrEmployee, Level.HRManager);

        shiftManagerEmp = new Employee("SM", 100000002, "pass", "bank", 8000,
                java.time.LocalDate.now(), 2, 2, 10, 5);
        shiftManagerUser = new User(shiftManagerEmp, Level.shiftManager);

        regularEmp = new Employee("Worker", 100000003, "pass", "bank", 6000,
                java.time.LocalDate.now(), 2, 2, 10, 5);
        regularUser = new User(regularEmp, Level.regularEmp);

        // Always ensure a sample role exists and is necessary for the shift
        sampleRole = new Role("Cashier");
        shift.addNecessaryRoles(hrUser, sampleRole);
    }

    /**
     * Test basic getters: day, type, and initial status of the shift.
     */
    @Test
    public void testGetters() {
        assertEquals(WeekDay.TUESDAY, shift.getDay());
        assertEquals(ShiftType.MORNING, shift.getType());
        assertEquals(Status.Empty, shift.getStatus());
    }

    /**
     * Test updating shift status by HR manager.
     */
    @Test
    public void testUpdateStatusAsHR() {
        shift.updateStatus(hrUser, Status.Full);
        assertEquals(Status.Full, shift.getStatus());
    }

    /**
     * Test adding an employee to the shift by a shift manager.
     */
    @Test
    public void testAddEmployeeByShiftManager() {
        shift.addEmployee(shiftManagerUser, regularEmp, sampleRole);
        assertTrue(shift.getEmployees().contains(regularEmp));
    }

    /**
     * Test adding an employee to the shift by HR manager.
     */
    @Test
    public void testAddEmployeeByHR() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        assertTrue(shift.getEmployees().contains(regularEmp));
    }

    /**
     * Test that a regular employee cannot add another employee to the shift.
     */
    @Test
    public void testAddEmployeeAccessDenied() {
        assertThrows(SecurityException.class, () -> {
            shift.addEmployee(regularUser, hrEmployee, sampleRole);
        });
    }

    /**
     * Test adding a necessary role to the shift.
     */
    @Test
    public void testAddNecessaryRole() {
        Role newRole = new Role("Admin");
        shift.addNecessaryRoles(hrUser, newRole);
        assertTrue(shift.getNecessaryRoles().contains(newRole));
    }

    /**
     * Test setting the shift manager for the shift.
     */
    @Test
    public void testSetShiftManager() {
        shift.setShiftManager(hrUser, shiftManagerEmp);
        assertEquals(shiftManagerEmp, shift.getShiftManager());
    }

    /**
     * Test that the shift's string representation includes employees and roles.
     */
    @Test
    public void testToStringIncludesEmployeesAndRoles() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        String out = shift.toString();
        assertTrue(out.contains("Worker"));
        assertTrue(out.contains("Cashier"));
    }

    /**
     * Test removing an employee from the shift by the shift manager.
     */
    @Test
    public void testRemoveEmployeeByShiftManager() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        shift.removeEmployee(shiftManagerUser, regularEmp);
        assertFalse(shift.getEmployees().contains(regularEmp));
    }

    /**
     * Test removing a role also removes the employee assigned to it.
     */
    @Test
    public void testRemoveRoleRemovesEmployee() {
        Role specialRole = new Role("SpecialRole");
        shift.addNecessaryRoles(hrUser, specialRole);
        shift.addEmployee(hrUser, regularEmp, specialRole);

        shift.removeRole(hrUser, specialRole);

        assertFalse(shift.getEmployees().contains(regularEmp));

        boolean roleStillFilled = shift.getFilledRoles().stream()
                .anyMatch(fr -> fr.getEmployee().equals(regularEmp));
        assertFalse(roleStillFilled);
    }

    /**
     * Test getting the list of not-occupied necessary roles in the shift.
     */
    @Test
    public void testGetNotOccupiedRoles() {
        List<Role> notOccupied = shift.getNotOccupiedRoles();
        assertTrue(notOccupied.contains(sampleRole));
    }

    // -------------------------
    // Additional Edge Case Tests
    // -------------------------

    /**
     * Test that adding an employee with a non-required role throws an exception.
     */
    @Test
    public void testAddEmployeeWithNotNecessaryRole() {
        Role nonRequiredRole = new Role("Security");
        assertThrows(IllegalArgumentException.class, () -> {
            shift.addEmployee(hrUser, regularEmp, nonRequiredRole);
        });
    }

    /**
     * Test that adding the same employee twice throws an exception.
     */
    @Test
    public void testAddSameEmployeeTwice() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        assertThrows(IllegalArgumentException.class, () -> {
            shift.addEmployee(hrUser, regularEmp, sampleRole);
        });
    }

    /**
     * Test that adding the same necessary role twice throws an exception.
     */
    @Test
    public void testAddSameNecessaryRoleTwice() {
        assertThrows(IllegalArgumentException.class, () -> {
            shift.addNecessaryRoles(hrUser, sampleRole);
        });
    }

    /**
     * Test that removing a non-existing role throws an exception.
     */
    @Test
    public void testRemoveNonExistingRole() {
        Role nonExistingRole = new Role("Security");
        assertThrows(IllegalArgumentException.class, () -> {
            shift.removeRole(hrUser, nonExistingRole);
        });
    }

    /**
     * Test that removing an employee not assigned to the shift throws an exception.
     */
    @Test
    public void testRemoveNonExistingEmployee() {
        assertThrows(IllegalArgumentException.class, () -> {
            shift.removeEmployee(shiftManagerUser, regularEmp);
        });
    }
}
