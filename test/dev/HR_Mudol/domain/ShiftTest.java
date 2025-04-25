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
    }

    @Test
    public void testGetters() {
        assertEquals(WeekDay.TUESDAY, shift.getDay());
        assertEquals(ShiftType.MORNING, shift.getType());
        assertEquals(Status.Empty, shift.getStatus());
    }

    @Test
    public void testSetStatusAsHR() {
        shift.setStatus(hrUser, Status.Full);
        assertEquals(Status.Full, shift.getStatus());
    }

    @Test
    public void testAddEmployeeByShiftManager() {
        shift.addEmployee(shiftManagerUser, regularEmp);
        assertTrue(shift.getEmployees().contains(regularEmp));
    }

    @Test
    public void testAddEmployeeByHR() {
        shift.addEmployee(hrUser, regularEmp);
        assertTrue(shift.getEmployees().contains(regularEmp));
    }

    @Test
    public void testAddEmployeeAccessDenied() {
        assertThrows(SecurityException.class, () -> {
            shift.addEmployee(regularUser, hrEmployee);
        });
    }

    @Test
    public void testAddNecessaryRole() {
        Role role = new Role("Cashier");
        shift.addNecessaryRoles(hrUser, role);
        assertTrue(shift.getNecessaryRoles().contains(role));
    }

    @Test
    public void testSetEmployees() {
        List<Employee> list = new LinkedList<>();
        list.add(hrEmployee);
        list.add(regularEmp);
        shift.setEmployees(hrUser, list);
        assertEquals(2, shift.getEmployees().size());
    }

    @Test
    public void testSetShiftManager() {
        shift.setShiftManager(hrUser, shiftManagerEmp);
        assertEquals(shiftManagerEmp, shift.getShiftManager());
    }

    @Test
    public void testToStringIncludesEmployeesAndRoles() {
        shift.addEmployee(hrUser, regularEmp);
        Role role = new Role("Admin");
        shift.addNecessaryRoles(hrUser, role);
        String out = shift.toString();
        assertTrue(out.contains("Worker"));
        assertTrue(out.contains("Admin"));
    }
}
