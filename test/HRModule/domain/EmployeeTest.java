package HRModule.domain;

import HR_Mudol.domain.*;
import HR_Mudol.domain.Objects.*;
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
        employee.setEmpPassword("newPass456");
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
        assertTrue(employee.getRelevantRoles().contains(cashier));
    }

    /**
     * Test that all constraint lists are initialized properly (not null).
     */
    @Test
    public void testConstraintsInitialization() {
        assertNotNull(employee.getWeeklyConstraints());
        assertNotNull(employee.getMorningConstraints());
        assertNotNull(employee.getEveningConstraints());
        assertNotNull(employee.getLockedConstraints());
    }

    /**
     * Test adding a new weekly constraint to the employee.
     */
    @Test
    public void testAddWeeklyConstraint() {
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);

        employee.addNewConstraints( c);
        assertEquals(1, employee.getWeeklyConstraints().size());
        assertEquals(c, employee.getWeeklyConstraints().get(0));
    }

    /**
     * Test adding a morning constraint to the employee.
     */
    @Test
    public void testAddMorningConstraint() {
        Constraint c = new Constraint("Morning off", WeekDay.TUESDAY, ShiftType.MORNING);

        employee.addNewMorningConstraints( c);
        assertEquals(1, employee.getMorningConstraints().size());
        assertEquals(c, employee.getMorningConstraints().get(0));
    }

    /**
     * Test adding an evening constraint to the employee.
     */
    @Test
    public void testAddEveningConstraint() {
        Constraint c = new Constraint("Evening off", WeekDay.THURSDAY, ShiftType.EVENING);

        employee.addNewEveningConstraints( c);
        assertEquals(1, employee.getEveningConstraints().size());
        assertEquals(c, employee.getEveningConstraints().get(0));
    }

    /**
     * Test that the employee's employment contract values are set correctly.
     */
    @Test
    public void testEmploymentContractValues() {
        EmploymentContract ec = employee.getContract();
        assertEquals(2, ec.getMinDayShift( employee));
        assertEquals(2, ec.getMinEveninigShift( employee));
        assertEquals(10, ec.getSickDays( employee));
        assertEquals(5, ec.getDaysOff( employee));
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
     * Test locking weekly constraints moves them into locked constraints and clears weekly lists.
     */
    @Test
    public void testLockWeeklyConstraints() {
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);
        employee.addNewConstraints( c);

        employee.lockWeeklyConstraints();

        assertEquals(0, employee.getWeeklyConstraints().size());
        assertEquals(1, employee.getLockedConstraints().size());
        assertEquals(c, employee.getLockedConstraints().get(0));
    }

    /**
     * Test that an HR Manager can access locked constraints.
     */
    @Test
    public void testHRManagerCanAccessLockedConstraints() {
        // Add a dummy constraint
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);
        employee.addNewConstraints( c);
        employee.lockWeeklyConstraints();

        // HR Manager should be able to access
        assertEquals(1, employee.getLockedConstraints().size());
    }


}
