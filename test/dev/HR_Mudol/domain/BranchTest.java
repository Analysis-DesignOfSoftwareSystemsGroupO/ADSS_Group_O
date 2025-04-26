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

    /**
     * Test that the branch contains at least one HRManager employee.
     */
    @Test
    public void testBranchHasHRManager() {
        boolean hasHR = branch.getEmployees().stream()
                .anyMatch(emp -> emp instanceof HRManager);
        assertTrue(hasHR, "Branch should contain at least one HRManager");
    }

    /**
     * Test that the branch contains at least one user with shift manager role.
     */
    @Test
    public void testBranchHasShiftManager() {
        boolean hasShiftManager = branch.getUsers().stream()
                .anyMatch(user -> user.isShiftManager());
        assertTrue(hasShiftManager, "Branch should contain at least one shift manager");
    }

    /**
     * Test that the branch contains at least one regular employee user.
     */
    @Test
    public void testBranchHasRegularEmployee() {
        boolean hasRegular = branch.getUsers().stream()
                .anyMatch(user -> user.getLevel() == Level.regularEmp);
        assertTrue(hasRegular, "Branch should contain at least one regular employee");
    }

    /**
     * Test that the branch contains at least 16 regular employees (1 from initial and 15 additional).
     */
    @Test
    public void testBranchHas15AdditionalEmployees() {
        long countRegular = branch.getUsers().stream()
                .filter(user -> user.getLevel() == Level.regularEmp)
                .count();
        assertTrue(countRegular >= 16, "Branch should contain at least 16 regular employees (1+15)");
    }

    /**
     * Test that the branch initializes with exactly one current week.
     */
    @Test
    public void testBranchHasWeekInitialized() {
        List<Week> weeks = branch.getWeeks();
        assertNotNull(weeks);
        assertEquals(1, weeks.size(), "Branch should initialize with 1 current week");
    }

    /**
     * Test that the branch ID is properly set and is non-negative.
     */
    @Test
    public void testBranchIDIsSet() {
        int id = branch.getBranchID();
        assertTrue(id >= 0, "Branch ID should be non-negative");
    }

    /**
     * Test that the list of roles is initialized and empty at branch creation.
     */
    @Test
    public void testRolesListIsEmptyInitially() {
        assertNotNull(branch.getRoles());
        assertEquals(0, branch.getRoles().size(), "Roles list should be empty at initialization");
    }

    /**
     * Test that the list of old employees is initialized and empty at branch creation.
     */
    @Test
    public void testOldEmployeesListIsEmptyInitially() {
        assertNotNull(branch.getOldEmployee());
        assertTrue(branch.getOldEmployee().isEmpty(), "Old employees list should be empty at start");
    }

    /**
     * Test that the number of users matches the number of employees.
     */
    @Test
    public void testUsersListHasSameSizeAsEmployees() {
        assertEquals(branch.getEmployees().size(), branch.getUsers().size(),
                "Number of users should match number of employees");
    }

    /**
     * Test that all employees in the branch have unique employee IDs.
     */
    @Test
    public void testUniqueEmployeeIDs() {
        long uniqueCount = branch.getEmployees().stream()
                .map(Employee::getEmpId)
                .distinct()
                .count();
        assertEquals(branch.getEmployees().size(), uniqueCount, "All employee IDs should be unique");
    }

    /**
     * Test that all User objects in the branch are unique (no duplicates).
     */
    @Test
    public void testUniqueUsers() {
        long uniqueUserCount = branch.getUsers().stream()
                .distinct()
                .count();
        assertEquals(branch.getUsers().size(), uniqueUserCount, "All users should be unique");
    }

    /**
     * Test that HRManager is also present in the users list.
     */
    @Test
    public void testHRManagerIsUser() {
        Employee hrManager = branch.getEmployees().stream()
                .filter(emp -> emp instanceof HRManager)
                .findFirst()
                .orElse(null);
        assertNotNull(hrManager, "HRManager should exist");

        boolean existsInUsers = branch.getUsers().stream()
                .anyMatch(user -> user.getUser().getEmpId() == hrManager.getEmpId());
        assertTrue(existsInUsers, "HRManager should also be present as a User");
    }


    /**
     * Test that creating two branches results in independent employee and user lists.
     */
    @Test
    public void testBranchesAreIndependent() {
        Branch branch1 = new Branch();
        Branch branch2 = new Branch();
        assertNotSame(branch1.getEmployees(), branch2.getEmployees(), "Branches should have independent employee lists");
        assertNotSame(branch1.getUsers(), branch2.getUsers(), "Branches should have independent user lists");
    }
}
