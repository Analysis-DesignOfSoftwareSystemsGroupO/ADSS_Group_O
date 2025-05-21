package dev.HR_Mudol.Service.EmployeeSystem;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeSystemTest {

    private Employee employee;
    private User employeeUser;

    @BeforeEach
    public void setUp() {
        employee = new Employee("Test Emp", 111111111, "pass", "bank123", 6000,
                LocalDate.of(2022, 1, 1), 2, 2, 5, 5);
        employeeUser = new User(employee, Level.regularEmp);
    }

    @Test
    public void testViewPersonalDetails() {
        EmployeeSystem system = new EmployeeSystem();
        system.viewPersonalDetails(employeeUser, employee);
    }

    @Test
    public void testViewPersonalDetails_UnauthorizedAccess() {
        Employee other = new Employee("Other", 222222222, "pass", "bank", 5000, LocalDate.now(), 2, 2, 5, 5);
        User otherUser = new User(other, Level.regularEmp);

        EmployeeSystem system = new EmployeeSystem();
        assertThrows(SecurityException.class, () -> system.viewPersonalDetails(otherUser, employee));
    }

    @Test
    public void testChangePassword_Success() {
        String input = "pass\nnewpass123\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        system.changePassword(employeeUser, employee);
        assertEquals("newpass123", employee.getEmpPassword());
    }

    @Test
    public void testChangePassword_SamePassword() {
        String input = "pass\npass\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        system.changePassword(employeeUser, employee);
        assertEquals("pass", employee.getEmpPassword());
    }

    @Test
    public void testSubmitConstraint_SimpleFlow() {
        String input = """
        yes
        Morning explanation 1
        yes
        Morning explanation 2
        no
        no
        no
        no
        no
        yes
        Evening explanation 1
        yes
        Evening explanation 2
        no
        no
        no
        no
        no
        """;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        Week currentWeek = new Week();
        system.submitConstraint(employeeUser, employee, currentWeek);

        int morningCount = employee.getMorningConstraints(employeeUser).size();
        int eveningCount = employee.getEveningConstraints(employeeUser).size();

        assertEquals(2, morningCount);
        assertEquals(2, eveningCount);
    }

    @Test
    public void testSubmitConstraint_InvalidInputFlow() {
        String input = """
        maybe
        no
        sometimes
        no
        no
        no
        no
        no
        nah
        no
        no
        no
        no
        no
        """;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        Week currentWeek = new Week();
        system.submitConstraint(employeeUser, employee, currentWeek);

        int morningCount = employee.getMorningConstraints(employeeUser).size();
        int eveningCount = employee.getEveningConstraints(employeeUser).size();

        assertEquals(0, morningCount);
        assertEquals(0, eveningCount);
    }

    @Test
    public void testViewMyShifts_NoShifts() {
        EmployeeSystem system = new EmployeeSystem();
        Week currentWeek = new Week();
        system.viewMyShifts(employeeUser, employee, currentWeek);
    }

    @Test
    public void testViewContractDetails() {
        EmployeeSystem system = new EmployeeSystem();
        system.viewContractDetails(employeeUser, employee);
    }

    @Test
    public void testViewContractDetails_Unauthorized() {
        Employee other = new Employee("Other", 222222222, "pass", "bank", 5000, LocalDate.now(), 2, 2, 5, 5);
        User otherUser = new User(other, Level.regularEmp);

        EmployeeSystem system = new EmployeeSystem();
        assertThrows(SecurityException.class, () -> system.viewContractDetails(otherUser, employee));
    }

    @Test
    public void testViewMyConstraints_EmptyInitially() {
        String input = "1\n"; // בוחר לראות אילוצים של השבוע הנוכחי
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        system.viewMyConstraints(employeeUser, employee);
    }

    @Test
    public void testViewMyConstraints_WithConstraints() {
        String input = "1\n"; // בוחר לראות אילוצים של השבוע הנוכחי
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        Constraint morningC = new Constraint("Busy on Monday", WeekDay.MONDAY, ShiftType.MORNING);
        Constraint eveningC = new Constraint("Unavailable Tuesday", WeekDay.TUESDAY, ShiftType.EVENING);
        employee.addNewConstraints(employeeUser, morningC);
        employee.addNewConstraints(employeeUser, eveningC);
        employee.addNewMorningConstraints(employeeUser, morningC);
        employee.addNewEveningConstraints(employeeUser, eveningC);

        system.viewMyConstraints(employeeUser, employee);
    }

    @Test
    public void testUpdateConstraint_ExplanationUpdate() {
        Constraint c = new Constraint("Can't work", WeekDay.MONDAY, ShiftType.MORNING);
        employee.addNewConstraints(employeeUser, c);
        employee.addNewMorningConstraints(employeeUser, c);

        String input = """
        morning
        1
        1
        Updated explanation
        back
        """;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        Week week = new Week();
        system.updateConstraint(employeeUser, employee, week);

        List<Constraint> updated = employee.getMorningConstraints(employeeUser);
        boolean updatedCorrectly = updated.stream().anyMatch(cc -> cc.getExplanation().contains("Updated explanation"));
        assertTrue(updatedCorrectly);
    }

    @Test
    public void testUpdateConstraint_NoConstraints() {
        String input = """
        morning
        back
        """;
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner testScanner = new Scanner(in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        Week currentWeek = new Week();
        system.updateConstraint(employeeUser, employee, currentWeek);
    }

    @Test
    public void testViewAvailableRoles_NoRolesAssigned() {
        EmployeeSystem system = new EmployeeSystem();
        system.viewAvailableRoles(employeeUser, employee);
    }

    @Test
    public void testViewAvailableRoles_WithRoles() {
        EmployeeSystem system = new EmployeeSystem();
        Role role = new Role("Cashier");
        role.addNewEmployee(new User(employee, Level.HRManager), employee);
        employee.addNewRole(new User(employee, Level.HRManager), role);
        system.viewAvailableRoles(employeeUser, employee);
    }

    @Test
    public void testViewMyShifts_WithAssignedShift() {
        Scanner testScanner = new Scanner(System.in);
        EmployeeSystem system = new EmployeeSystem(testScanner);

        Week week = new Week();
        Shift shift = new Shift(WeekDay.MONDAY, ShiftType.MORNING);

        User hrManager = new User(employee, Level.HRManager);

        Role role = new Role("Cashier");
        shift.addNecessaryRoles(hrManager, role);
        shift.addEmployee(hrManager, employee, role);

        week.addShift(hrManager, shift);

        system.viewMyShifts(employeeUser, employee, week);
    }
}
