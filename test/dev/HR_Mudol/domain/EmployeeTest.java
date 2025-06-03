package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    private Employee employee;
    private User hrManagerCaller;

    @BeforeEach
    public void setUp() {
        employee = new Employee(
                "John Doe", 123456789, "pass123", "IL58-1234-5678",
                8000, LocalDate.of(2023, 1, 1),
                2, 2, 10, 5
        );
        hrManagerCaller = new User(employee, Level.HRManager);
    }

    /**
     * Test that the employee's name is set correctly.
     */
    @Test
    public void testGetName() {
        assertEquals("John Doe", employee.getEmpName());
    }

    /**
     * Test that the employee's ID is set correctly.
     */
    @Test
    public void testGetID() {
        assertEquals(123456789, employee.getEmpId());
    }

    /**
     * Test setting and retrieving the employee's password.
     */
    @Test
    public void testSetAndGetPassword() {
        employee.setEmpPassword("newPass456", hrManagerCaller);
        assertEquals("newPass456", employee.getEmpPassword());
    }

    /**
     * Test that the employee's initial bank account is set correctly.
     */
    @Test
    public void testInitialBankAccount() {
        assertEquals("IL58-1234-5678", employee.getEmpBankAccount());
    }

    /**
     * Test changing and retrieving the employee's bank account.
     */
    @Test
    public void testChangeBankAccount() {
        employee.setEmpBankAccount(hrManagerCaller, "IL00-9999-0000");
        assertEquals("IL00-9999-0000", employee.getEmpBankAccount());
    }

    /**
     * Test adding a new relevant role to the employee.
     */
    @Test
    public void testAddRelevantRole() {
        Role cashier = new Role("Cashier");
        employee.addNewRole(hrManagerCaller, cashier);
        assertTrue(employee.getRelevantRoles(hrManagerCaller).contains(cashier));
    }

    /**
     * Test that all constraint lists are initialized properly (not null).
     */
    @Test
    public void testConstraintsInitialization() {
        assertNotNull(employee.getWeeklyConstraints(hrManagerCaller));
        assertNotNull(employee.getMorningConstraints(hrManagerCaller));
        assertNotNull(employee.getEveningConstraints(hrManagerCaller));
        assertNotNull(employee.getLockedConstraints(hrManagerCaller));
    }

    /**
     * Test adding a new weekly constraint to the employee.
     */
    @Test
    public void testAddWeeklyConstraint() {
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);

        employee.addNewConstraints(hrManagerCaller, c);
        assertEquals(1, employee.getWeeklyConstraints(hrManagerCaller).size());
        assertEquals(c, employee.getWeeklyConstraints(hrManagerCaller).get(0));
    }

    /**
     * Test adding a morning constraint to the employee.
     */
    @Test
    public void testAddMorningConstraint() {
        Constraint c = new Constraint("Morning off", WeekDay.TUESDAY, ShiftType.MORNING);

        employee.addNewMorningConstraints(hrManagerCaller, c);
        assertEquals(1, employee.getMorningConstraints(hrManagerCaller).size());
        assertEquals(c, employee.getMorningConstraints(hrManagerCaller).get(0));
    }

    /**
     * Test adding an evening constraint to the employee.
     */
    @Test
    public void testAddEveningConstraint() {
        Constraint c = new Constraint("Evening off", WeekDay.THURSDAY, ShiftType.EVENING);

        employee.addNewEveningConstraints(hrManagerCaller, c);
        assertEquals(1, employee.getEveningConstraints(hrManagerCaller).size());
        assertEquals(c, employee.getEveningConstraints(hrManagerCaller).get(0));
    }

    /**
     * Test that the employee's employment contract values are set correctly.
     */
    @Test
    public void testEmploymentContractValues() {
        EmploymentContract ec = employee.getContract(hrManagerCaller);
        assertEquals(2, ec.getMinDayShift(hrManagerCaller, employee));
        assertEquals(2, ec.getMinEveninigShift(hrManagerCaller, employee));
        assertEquals(10, ec.getSickDays(hrManagerCaller, employee));
        assertEquals(5, ec.getDaysOff(hrManagerCaller, employee));
    }

    /**
     * Test that the employee's start date is set correctly.
     */
    @Test
    public void testStartDate() {
        assertEquals(LocalDate.of(2023, 1, 1), employee.getEmpStartDate());
    }

    /**
     * Test that a regular employee cannot add a new role (should throw SecurityException).
     */
    @Test
    public void testRegularEmployeeCannotAddRole() {
        User regularUser = new User(employee, Level.regularEmp);
        Role newRole = new Role("Cleaner");

        assertThrows(SecurityException.class, () -> {
            employee.addNewRole(regularUser, newRole);
        });
    }

    /**
     * Test that a regular employee cannot access relevant roles (should throw SecurityException).
     */
    @Test
    public void testRegularEmployeeCannotViewRoles() {
        // יצירת עובד אחר שהוא ינסה לקרוא לו
        Employee anotherEmployee = new Employee(
                "Jane Smith", 987654321, "pass456", "IL00-0000-0000",
                7500, LocalDate.of(2022, 6, 10), 2, 2, 10, 5
        );
        User regularUser = new User(anotherEmployee, Level.regularEmp);

        assertThrows(SecurityException.class, () -> {
            employee.getRelevantRoles(regularUser);
        });
    }


    /**
     * Test that a different regular employee cannot lock constraints for another employee.
     */
    @Test
    public void testRegularEmployeeCannotLockConstraints() {
        // Create a second employee and wrap him as a regular user
        Employee anotherEmployee = new Employee(
                "Jane Smith", 987654321, "pass456", "IL00-0000-0000",
                7500, LocalDate.of(2022, 6, 10), 2, 2, 10, 5
        );
        User regularUser = new User(anotherEmployee, Level.regularEmp);

        // Try to lock constraints of the original employee with a different employee
        assertThrows(SecurityException.class, () -> {
            employee.lockWeeklyConstraints(regularUser);
        });
    }


    /**
     * Test locking weekly constraints moves them into locked constraints and clears weekly lists.
     */
    @Test
    public void testLockWeeklyConstraints() {
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);
        employee.addNewConstraints(hrManagerCaller, c);

        employee.lockWeeklyConstraints(hrManagerCaller);

        assertEquals(0, employee.getWeeklyConstraints(hrManagerCaller).size());
        assertEquals(1, employee.getLockedConstraints(hrManagerCaller).size());
        assertEquals(c, employee.getLockedConstraints(hrManagerCaller).get(0));
    }

    /**
     * Test that an HR Manager can access locked constraints.
     */
    @Test
    public void testHRManagerCanAccessLockedConstraints() {
        // Add a dummy constraint
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);
        employee.addNewConstraints(hrManagerCaller, c);
        employee.lockWeeklyConstraints(hrManagerCaller);

        // HR Manager should be able to access
        assertEquals(1, employee.getLockedConstraints(hrManagerCaller).size());
    }

    /**
     * Test that a regular employee cannot access locked constraints (should throw SecurityException).
     */
    @Test
    public void testRegularEmployeeCannotAccessLockedConstraints() {
        User regularUser = new User(employee, Level.regularEmp);

        assertThrows(SecurityException.class, () -> {
            employee.getLockedConstraints(regularUser);
        });
    }
}
