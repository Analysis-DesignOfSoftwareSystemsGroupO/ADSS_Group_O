package HRModule.domain;

import HR_Mudol.domain.*;
import HR_Mudol.domain.Objects.*;
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

    /**
     * Test that the HR manager's toString method includes key identity fields.
     */
    @Test
    public void testHRManagerToString() {
        String result = hrManager.toString(hrCaller);
        assertTrue(result.contains("HR Manager"));
        assertTrue(result.contains("Alice HR"));
        assertTrue(result.contains("987654321"));
    }

    /**
     * Test that the HR manager has access to all constraint lists.
     */
    @Test
    public void testAccessToConstraints() {
        assertNotNull(hrManager.getWeeklyConstraints());
        assertNotNull(hrManager.getMorningConstraints());
        assertNotNull(hrManager.getEveningConstraints());
        assertNotNull(hrManager.getLockedConstraints());
    }

    /**
     * Test adding a new weekly constraint to the HR manager.
     */
    @Test
    public void testAddWeeklyConstraint() {
        Constraint c = new Constraint("HR busy", WeekDay.TUESDAY, ShiftType.MORNING);
        hrManager.addNewConstraints( c);
        assertEquals(1, hrManager.getWeeklyConstraints().size());
        assertEquals(c, hrManager.getWeeklyConstraints().get(0));
    }

    /**
     * Test reading the employment contract values of the HR manager.
     */
    @Test
    public void testEmploymentContractAccess() {
        EmploymentContract ec = hrManager.getContract();
        assertEquals(1, ec.getMinDayShift(hrManager));
        assertEquals(1, ec.getMinEveninigShift( hrManager));
        assertEquals(20, ec.getSickDays( hrManager));
        assertEquals(10, ec.getDaysOff( hrManager));
    }

    /**
     * Test modifying sick days and vacation days through the contract.
     */
    @Test
    public void testModifyContractValues() {
        hrManager.setSickDays(15);
        hrManager.setDaysOff(hrCaller, 7);
        assertEquals(15, hrManager.getSickDays());
        assertEquals(7, hrManager.getDaysOff());
    }

    /**
     * Test modifying the required minimum shift counts.
     */
    @Test
    public void testChangeMinShifts() {
        hrManager.setMinDayShift( 3);
        hrManager.setMinEveninigShift( 4);
        assertEquals(3, hrManager.getMinDayShift());
        assertEquals(4, hrManager.getMinEveninigShift());
    }

    /**
     * Test updating the HR manager's bank account number.
     */
    @Test
    public void testChangeBankAccount() {
        hrManager.setEmpBankAccount(hrCaller, "IL00-9999-0000");
        assertEquals("IL00-9999-0000", hrManager.getEmpBankAccount());
    }

    /**
     * Test adding and viewing a relevant role for the HR manager.
     */
    @Test
    public void testAddAndViewRoles() {
        Role r = new Role("Admin");
        hrManager.addNewRole(hrCaller, r);
        assertTrue(hrManager.getRelevantRoles().contains(r));
    }

    /**
     * Test locking weekly constraints as an HR manager.
     */
    @Test
    public void testLockWeeklyConstraints() {
        Constraint c = new Constraint("Vacation", WeekDay.THURSDAY, ShiftType.EVENING);
        hrManager.addNewConstraints(c);
        hrManager.lockWeeklyConstraints();
        assertEquals(1, hrManager.getLockedConstraints().size());
        assertEquals(0, hrManager.getWeeklyConstraints().size());
    }

    // -------------------------
    // Additional Edge Case Tests
    // -------------------------

    /**
     * Test adding the same role twice results in duplication (if allowed).
     */
    @Test
    public void testAddDuplicateRole() {
        Role r = new Role("Admin");
        hrManager.addNewRole(hrCaller, r);
        hrManager.addNewRole(hrCaller, r);
        assertEquals(2, hrManager.getRelevantRoles().size()); // or 1 if duplicates are blocked
    }

    /**
     * Test that adding a null constraint throws NullPointerException.
     */
    @Test
    public void testAddNullConstraint() {
        assertThrows(NullPointerException.class, () -> {
            hrManager.addNewConstraints( null);
        });
    }


    /**
     * Test that all constraint lists are cleared after locking.
     */
    @Test
    public void testClearAllConstraintsAfterLock() {
        hrManager.addNewConstraints( new Constraint("Vacation", WeekDay.MONDAY, ShiftType.MORNING));
        hrManager.addNewMorningConstraints( new Constraint("No Mornings", WeekDay.TUESDAY, ShiftType.MORNING));
        hrManager.addNewEveningConstraints( new Constraint("No Evenings", WeekDay.THURSDAY, ShiftType.EVENING));

        hrManager.lockWeeklyConstraints();

        assertTrue(hrManager.getWeeklyConstraints().isEmpty());
        assertTrue(hrManager.getMorningConstraints().isEmpty());
        assertTrue(hrManager.getEveningConstraints().isEmpty());
    }
}
