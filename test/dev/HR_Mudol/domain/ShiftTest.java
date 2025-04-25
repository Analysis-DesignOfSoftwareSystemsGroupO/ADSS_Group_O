package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.LinkedList;

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

        sampleRole = new Role("Cashier");
        shift.addNecessaryRoles(hrUser, sampleRole); // נוסיף תפקיד לצורך בדיקות addEmployee
    }

    @Test
    public void testGetters() {
        assertEquals(WeekDay.TUESDAY, shift.getDay());
        assertEquals(ShiftType.MORNING, shift.getType());
        assertEquals(Status.Empty, shift.getStatus());
    }

    @Test
    public void testUpdateStatusAsHR() {
        shift.updateStatus(hrUser, Status.Full);
        assertEquals(Status.Full, shift.getStatus());
    }

    @Test
    public void testAddEmployeeByShiftManager() {
        shift.addEmployee(shiftManagerUser, regularEmp, sampleRole);
        assertTrue(shift.getEmployees().contains(regularEmp));
    }

    @Test
    public void testAddEmployeeByHR() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        assertTrue(shift.getEmployees().contains(regularEmp));
    }

    @Test
    public void testAddEmployeeAccessDenied() {
        assertThrows(SecurityException.class, () -> {
            shift.addEmployee(regularUser, hrEmployee, sampleRole);
        });
    }

    @Test
    public void testAddNecessaryRole() {
        Role newRole = new Role("Admin");
        shift.addNecessaryRoles(hrUser, newRole);
        assertTrue(shift.getNecessaryRoles().contains(newRole));
    }

    @Test
    public void testSetShiftManager() {
        shift.setShiftManager(hrUser, shiftManagerEmp);
        assertEquals(shiftManagerEmp, shift.getShiftManager());
    }

    @Test
    public void testToStringIncludesEmployeesAndRoles() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        String out = shift.toString();
        assertTrue(out.contains("Worker"));
        assertTrue(out.contains("Cashier"));
    }

    @Test
    public void testRemoveEmployeeByShiftManager() {
        shift.addEmployee(hrUser, regularEmp, sampleRole);
        shift.removeEmployee(shiftManagerUser, regularEmp);
        assertFalse(shift.getEmployees().contains(regularEmp));
    }

    @Test
    public void testRemoveRoleRemovesEmployee() {
        // הוספת תפקיד נחוץ למשמרת
        shift.addNecessaryRoles(hrUser, sampleRole);

        // שיוך עובד למשמרת עם אותו תפקיד
        shift.addEmployee(hrUser, regularEmp, sampleRole);

        // הסרה של התפקיד
        shift.removeRole(hrUser, sampleRole);

        // בדיקה שהעובד הוסר מהמשמרת
        assertFalse(shift.getEmployees().contains(regularEmp));

        // בדיקה שה־FilledRole הוסר גם הוא
        boolean roleStillFilled = shift.getFilledRoles().stream()
                .anyMatch(fr -> fr.getEmployee().equals(regularEmp));
        assertFalse(roleStillFilled);
    }

    @Test
    public void testGetNotOccupiedRoles() {
        List<Role> notOccupied = shift.getNotOccupiedRoles();
        assertTrue(notOccupied.contains(sampleRole));
    }
}
