package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConstraintTest {

    private Constraint constraint;
    private Employee employee;
    private User employeeUser;
    private User hrUser;

    @BeforeEach
    public void setUp() {
        employee = new Employee("Dana", 123456789, "pass", "123456", 5000,
                java.time.LocalDate.now(), 2, 2, 5, 10);
        constraint = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);
        employeeUser = new User(employee, Level.regularEmp);
        hrUser = new User(employee, Level.HRManager);
    }

    /**
     * Test that the getters of Constraint return the correct initial values.
     */
    @Test
    public void testGetters() {
        assertEquals("Vacation", constraint.getExplanation());
        assertEquals(WeekDay.MONDAY, constraint.getDay());
        assertEquals(ShiftType.MORNING, constraint.getType());
    }

    /**
     * Test that an HR manager can update the explanation field successfully.
     */
    @Test
    public void testSetExplanationAsHR() {
        constraint.setExplanation(hrUser, employee, "Updated");
        assertEquals("Updated", constraint.getExplanation());
    }

    /**
     * Test that the employee can update their own explanation field.
     */
    @Test
    public void testSetExplanationAsSelf() {
        constraint.setExplanation(employeeUser, employee, "New Reason");
        assertEquals("New Reason", constraint.getExplanation());
    }

    /**
     * Test that the employee can update their own day field.
     */
    @Test
    public void testSetDayAsSelf() {
        constraint.setDay(employeeUser, employee, WeekDay.WEDNESDAY);
        assertEquals(WeekDay.WEDNESDAY, constraint.getDay());
    }

    /**
     * Test that the employee can update their own shift type field.
     */
    @Test
    public void testSetTypeAsSelf() {
        constraint.setType(employeeUser, employee, ShiftType.EVENING);
        assertEquals(ShiftType.EVENING, constraint.getType());
    }

    /**
     * Test that another employee cannot modify the day field of this constraint.
     * Should throw SecurityException.
     */
    @Test
    public void testSetDayAccessDenied() {
        Employee other = new Employee("Other", 111222333, "pass", "acc", 4000,
                java.time.LocalDate.now(), 2, 2, 5, 10);
        User otherUser = new User(other, Level.regularEmp);
        assertThrows(SecurityException.class, () -> {
            constraint.setDay(otherUser, employee, WeekDay.SUNDAY);
        });
    }

    /**
     * Test that another employee cannot modify the explanation field of this constraint.
     * Should throw SecurityException.
     */
    @Test
    public void testSetExplanationAccessDenied() {
        Employee other = new Employee("Other", 111222333, "pass", "acc", 4000,
                java.time.LocalDate.now(), 2, 2, 5, 10);
        User otherUser = new User(other, Level.regularEmp);
        assertThrows(SecurityException.class, () -> {
            constraint.setExplanation(otherUser, employee, "Invalid");
        });
    }
    /**
     * Test that setting a null explanation throws NullPointerException.
     */
    @Test
    public void testSetExplanationNull() {
        assertThrows(NullPointerException.class, () -> {
            constraint.setExplanation(employeeUser, employee, null);
        });
    }

    /**
     * Test that setting a null day throws NullPointerException.
     */
    @Test
    public void testSetDayNull() {
        assertThrows(NullPointerException.class, () -> {
            constraint.setDay(employeeUser, employee, null);
        });
    }

    /**
     * Test that setting a null shift type throws NullPointerException.
     */
    @Test
    public void testSetTypeNull() {
        assertThrows(NullPointerException.class, () -> {
            constraint.setType(employeeUser, employee, null);
        });
    }

    /**
     * Test that setting an empty explanation is allowed but should update to an empty string.
     */
    @Test
    public void testSetExplanationEmptyString() {
        constraint.setExplanation(employeeUser, employee, "");
        assertEquals("", constraint.getExplanation());
    }

    /**
     * Test that setting a field with null user throws NullPointerException.
     */
    @Test
    public void testSetWithNullUser() {
        assertThrows(NullPointerException.class, () -> {
            constraint.setDay(null, employee, WeekDay.SUNDAY);
        });
    }

    /**
     * Test that setting a field with null employee triggers SecurityException due to authorization check.
     */
    @Test
    public void testSetWithNullEmployee() {
        assertThrows(SecurityException.class, () -> {
            constraint.setType(employeeUser, null, ShiftType.MORNING);
        });
    }


}
