package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BranchTest {

    private Branch branch;

    @BeforeEach
    public void setUp() {
        branch = new Branch();
    }

    @Test
    public void testBranchHasHRManager() {
        boolean hasHR = branch.getEmployees().stream()
                .anyMatch(emp -> emp instanceof HRManager);
        assertTrue(hasHR, "Branch should contain at least one HRManager");
    }

    @Test
    public void testBranchHasShiftManager() {
        boolean hasShiftManager = branch.getUsers().stream()
                .anyMatch(user -> user.isShiftManager());
        assertTrue(hasShiftManager, "Branch should contain at least one shift manager");
    }

    @Test
    public void testBranchHasRegularEmployee() {
        boolean hasRegular = branch.getUsers().stream()
                .anyMatch(user -> user.getLevel() == Level.regularEmp);
        assertTrue(hasRegular, "Branch should contain at least one regular employee");
    }

    @Test
    public void testBranchHas15AdditionalEmployees() {
        long countRegular = branch.getUsers().stream()
                .filter(user -> user.getLevel() == Level.regularEmp)
                .count();
        assertTrue(countRegular >= 16, "Branch should contain at least 16 regular employees (1+15)");
    }

    @Test
    public void testBranchHasWeekInitialized() {
        List<Week> weeks = branch.getWeeks();
        assertNotNull(weeks);
        assertEquals(1, weeks.size(), "Branch should initialize with 1 current week");
    }

    @Test
    public void testBranchIDIsSet() {
        int id = branch.getBranchID();
        assertTrue(id >= 0, "Branch ID should be non-negative");
    }

    @Test
    public void testRolesListIsEmptyInitially() {
        assertNotNull(branch.getRoles());
        assertEquals(0, branch.getRoles().size(), "Roles list should be empty at initialization");
    }

    @Test
    public void testOldEmployeesListIsEmptyInitially() {
        assertNotNull(branch.getOldEmployee());
        assertTrue(branch.getOldEmployee().isEmpty(), "Old employees list should be empty at start");
    }

    @Test
    public void testUsersListHasSameSizeAsEmployees() {
        assertEquals(branch.getEmployees().size(), branch.getUsers().size(),
                "Number of users should match number of employees");
    }

    @Test
    public void testUniqueEmployeeIDs() {
        long uniqueCount = branch.getEmployees().stream()
                .map(Employee::getEmpId)
                .distinct()
                .count();
        assertEquals(branch.getEmployees().size(), uniqueCount, "All employee IDs should be unique");
    }
}
