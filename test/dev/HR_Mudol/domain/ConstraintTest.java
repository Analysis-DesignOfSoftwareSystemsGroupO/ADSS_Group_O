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

    @Test
    public void testGetters() {
        assertEquals("Vacation", constraint.getExplanation());
        assertEquals(WeekDay.MONDAY, constraint.getDay());
        assertEquals(ShiftType.MORNING, constraint.getType());
    }

    @Test
    public void testSetExplanationAsHR() {
        constraint.setExplanation(hrUser, employee, "Updated");
        assertEquals("Updated", constraint.getExplanation());
    }

    @Test
    public void testSetExplanationAsSelf() {
        constraint.setExplanation(employeeUser, employee, "New Reason");
        assertEquals("New Reason", constraint.getExplanation());
    }

    @Test
    public void testSetDayAsSelf() {
        constraint.setDay(employeeUser, employee, WeekDay.WEDNESDAY);
        assertEquals(WeekDay.WEDNESDAY, constraint.getDay());
    }

    @Test
    public void testSetTypeAsSelf() {
        constraint.setType(employeeUser, employee, ShiftType.EVENING);
        assertEquals(ShiftType.EVENING, constraint.getType());
    }

    @Test
    public void testSetDayAccessDenied() {
        Employee other = new Employee("Other", 111222333, "pass", "acc", 4000,
                java.time.LocalDate.now(), 2, 2, 5, 10);
        User otherUser = new User(other, Level.regularEmp);
        assertThrows(SecurityException.class, () -> {
            constraint.setDay(otherUser, employee, WeekDay.SUNDAY);
        });
    }

    @Test
    public void testSetExplanationAccessDenied() {
        Employee other = new Employee("Other", 111222333, "pass", "acc", 4000,
                java.time.LocalDate.now(), 2, 2, 5, 10);
        User otherUser = new User(other, Level.regularEmp);
        assertThrows(SecurityException.class, () -> {
            constraint.setExplanation(otherUser, employee, "Invalid");
        });
    }
}
