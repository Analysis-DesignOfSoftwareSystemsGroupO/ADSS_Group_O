package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class HRManagerTest {

    private HRManager hrManager;
    private User hrCaller;

    @BeforeEach
    public void setUp() {
        hrManager = new HRManager(
                "Alice HR", 987654321, "securePass", "IL12-3456-7890",
                12000, LocalDate.of(2020, 5, 1),
                1, 1, 20, 10
        );
        hrCaller = new User(hrManager, Level.HRManager);
    }

    @Test
    public void testHRManagerToString() {
        String result = hrManager.toString(hrCaller);
        assertTrue(result.contains("HR Manager"));
        assertTrue(result.contains("Alice HR"));
        assertTrue(result.contains("987654321"));
    }

    @Test
    public void testAccessToConstraints() {
        assertNotNull(hrManager.getWeeklyConstraints(hrCaller));
        assertNotNull(hrManager.getMorningConstraints(hrCaller));
        assertNotNull(hrManager.getEveningConstraints(hrCaller));
        assertNotNull(hrManager.getLockedConstraints(hrCaller));
    }

    @Test
    public void testAddWeeklyConstraint() {
        Constraint c = new Constraint("HR busy", WeekDay.TUESDAY, ShiftType.MORNING);
        hrManager.addNewConstraints(hrCaller, c);
        assertEquals(1, hrManager.getWeeklyConstraints(hrCaller).size());
        assertEquals(c, hrManager.getWeeklyConstraints(hrCaller).get(0));
    }

    @Test
    public void testEmploymentContractAccess() {
        EmploymentContract ec = hrManager.getContract(hrCaller);
        assertEquals(1, ec.getMinDayShift(hrCaller, hrManager));
        assertEquals(1, ec.getMinEveninigShift(hrCaller, hrManager));
        assertEquals(20, ec.getSickDays(hrCaller, hrManager));
        assertEquals(10, ec.getDaysOff(hrCaller, hrManager));
    }

    @Test
    public void testModifyContractValues() {
        hrManager.setSickDays(hrCaller, 15);
        hrManager.setDaysOff(hrCaller, 7);
        assertEquals(15, hrManager.getSickDays(hrCaller));
        assertEquals(7, hrManager.getDaysOff(hrCaller));
    }

    @Test
    public void testChangeMinShifts() {
        hrManager.setMinDayShift(hrCaller, 3);
        hrManager.setMinEveninigShift(hrCaller, 4);
        assertEquals(3, hrManager.getMinDayShift(hrCaller));
        assertEquals(4, hrManager.getMinEveninigShift(hrCaller));
    }

    @Test
    public void testChangeBankAccount() {
        hrManager.setEmpBankAccount(hrCaller, "IL00-9999-0000");
        assertEquals("IL00-9999-0000", hrManager.getEmpBankAccount());
    }

    @Test
    public void testAddAndViewRoles() {
        Role r = new Role("Admin");
        hrManager.addNewRole(hrCaller, r);
        assertTrue(hrManager.getRelevantRoles(hrCaller).contains(r));
    }

    @Test
    public void testLockWeeklyConstraints() {
        Constraint c = new Constraint("Vacation", WeekDay.THURSDAY, ShiftType.EVENING);
        hrManager.addNewConstraints(hrCaller, c);
        hrManager.lockWeeklyConstraints(hrCaller);
        assertEquals(1, hrManager.getLockedConstraints(hrCaller).size());
        assertEquals(0, hrManager.getWeeklyConstraints(hrCaller).size());
    }
}
