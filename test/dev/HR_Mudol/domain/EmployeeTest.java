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

    @Test
    public void testGetName() {
        assertEquals("John Doe", employee.getEmpName());
    }

    @Test
    public void testGetID() {
        assertEquals(123456789, employee.getEmpId());
    }

    @Test
    public void testSetAndGetPassword() {
        employee.setEmpPassword("newPass456", hrManagerCaller);
        assertEquals("newPass456", employee.getEmpPassword());
    }

    @Test
    public void testInitialBankAccount() {
        assertEquals("IL58-1234-5678", employee.getEmpBankAccount());
    }

    @Test
    public void testAddRelevantRole() {
        Role cashier = new Role("Cashier");
        employee.addNewRole(hrManagerCaller, cashier);
        assertTrue(employee.getRelevantRoles(hrManagerCaller).contains(cashier));
    }

    @Test
    public void testConstraintsInitialization() {
        assertNotNull(employee.getWeeklyConstraints(hrManagerCaller));
        assertNotNull(employee.getMorningConstraints(hrManagerCaller));
        assertNotNull(employee.getEveningConstraints(hrManagerCaller));
        assertNotNull(employee.getLockedConstraints(hrManagerCaller));
    }

    @Test
    public void testAddWeeklyConstraint() {
        Constraint c = new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING);

        employee.addNewConstraints(hrManagerCaller, c);
        assertEquals(1, employee.getWeeklyConstraints(hrManagerCaller).size());
        assertEquals(c, employee.getWeeklyConstraints(hrManagerCaller).get(0));
    }

    @Test
    public void testEmploymentContractValues() {
        EmploymentContract ec = employee.getContract(hrManagerCaller);
        assertEquals(2, ec.getMinDayShift(hrManagerCaller, employee));
        assertEquals(2, ec.getMinEveninigShift(hrManagerCaller, employee));
        assertEquals(10, ec.getSickDays(hrManagerCaller, employee));
        assertEquals(5, ec.getDaysOff(hrManagerCaller, employee));
    }

    @Test
    public void testStartDate() {
        assertEquals(LocalDate.of(2023, 1, 1), employee.getEmpStartDate());
    }

    @Test
    public void testChangeBankAccount() {
        employee.setEmpBankAccount(hrManagerCaller, "IL00-9999-0000");
        assertEquals("IL00-9999-0000", employee.getEmpBankAccount());
    }
}
