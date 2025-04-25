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

    @Test
    void testIsManager() {
        assertTrue(hrUser.isManager());
        assertFalse(regularUser.isManager());
    }

    @Test
    void testIsShiftManager() {
        User shiftUser = new User(employee, Level.shiftManager);
        assertTrue(shiftUser.isShiftManager());
        assertFalse(hrUser.isShiftManager());
    }

    @Test
    void testIsSameEmployee() {
        assertTrue(regularUser.isSameEmployee(employee));

        Employee other = new Employee("Other", 999999999, "p", "b", 3000,
                LocalDate.now(), 1, 1, 1, 1);
        assertFalse(regularUser.isSameEmployee(other));
    }

    @Test
    void testSetLevel_Success() {
        regularUser.setLevel(hrUser, Level.shiftManager);
        assertEquals(Level.shiftManager, regularUser.getLevel());
    }

    @Test
    void testSetLevel_FailureByPermission() {
        assertThrows(SecurityException.class, () -> hrUser.setLevel(regularUser, Level.shiftManager));
    }

    @Test
    void testGetUser() {
        assertEquals(hrManager, hrUser.getUser());
        assertEquals(employee, regularUser.getUser());
    }

    @Test
    void testGetLevel() {
        assertEquals(Level.HRManager, hrUser.getLevel());
        assertEquals(Level.regularEmp, regularUser.getLevel());
    }
}
