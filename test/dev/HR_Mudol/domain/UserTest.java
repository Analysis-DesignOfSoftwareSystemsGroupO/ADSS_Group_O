package dev.HR_Mudol.domain;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.HRManager;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private HRManager hrManager;
    private Employee employee;
    private User hrUser;
    private User regularUser;

    @BeforeEach
    void setUp() {
        hrManager = new HRManager("Rami", 111111111, "admin", "123", 10000,
                LocalDate.now(), 2, 2, 5, 10);
        employee = new Employee("Dana", 123456789, "pass", "456", 5000,
                LocalDate.now(), 2, 2, 5, 10);

        hrUser = new User(hrManager, Level.HRManager);
        regularUser = new User(employee, Level.regularEmp);
    }

    /**
     * Test that isManager returns true for HR users and false otherwise.
     */
    @Test
    void testIsManager() {
        assertTrue(hrUser.isManager());
        assertFalse(regularUser.isManager());
    }

    /**
     * Test that isShiftManager returns true for shift managers and false otherwise.
     */
    @Test
    void testIsShiftManager() {
        User shiftUser = new User(employee, Level.shiftManager);
        assertTrue(shiftUser.isShiftManager());
        assertFalse(hrUser.isShiftManager());
    }

    /**
     * Test that isSameEmployee compares the User's employee reference correctly.
     */
    @Test
    void testIsSameEmployee() {
        assertTrue(regularUser.isSameEmployee(employee));

        Employee other = new Employee("Other", 999999999, "p", "b", 3000,
                LocalDate.now(), 1, 1, 1, 1);
        assertFalse(regularUser.isSameEmployee(other));
    }

    /**
     * Test that an HR user can successfully change another user's level.
     */
    @Test
    void testSetLevel_Success() {
        regularUser.setLevel(hrUser, Level.shiftManager);
        assertEquals(Level.shiftManager, regularUser.getLevel());
    }

    /**
     * Test that a non-manager cannot change another user's level.
     */
    @Test
    void testSetLevel_FailureByPermission() {
        assertThrows(SecurityException.class, () -> hrUser.setLevel(regularUser, Level.shiftManager));
    }

    /**
     * Test that getUser returns the correct Employee/HRManager object.
     */
    @Test
    void testGetUser() {
        assertEquals(hrManager, hrUser.getUser());
        assertEquals(employee, regularUser.getUser());
    }

    /**
     * Test that getLevel returns the current access level of the user.
     */
    @Test
    void testGetLevel() {
        assertEquals(Level.HRManager, hrUser.getLevel());
        assertEquals(Level.regularEmp, regularUser.getLevel());
    }

    // -------------------- Additional edge cases ----------------------

    /**
     * Test creating a user with null employee should throw an exception.
     */
    @Test
    void testCreateUserWithNullEmployee() {
        assertThrows(NullPointerException.class, () -> new User(null, Level.regularEmp));
    }

    /**
     * Test setting a null level should throw an exception.
     */
    @Test
    void testSetNullLevel() {
        assertThrows(NullPointerException.class, () -> regularUser.setLevel(hrUser, null));
    }

    /**
     * Test that setting the same level doesn't change anything.
     */
    @Test
    void testSetSameLevel() {
        regularUser.setLevel(hrUser, Level.regularEmp);
        assertEquals(Level.regularEmp, regularUser.getLevel());
    }
}




