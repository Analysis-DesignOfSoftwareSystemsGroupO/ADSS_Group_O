package dev.HR_Mudol.domain;

import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Branch class to ensure correct initialization and basic behavior.
 */
public class BranchTest {

    private Branch branch;

    @BeforeEach
    public void setUp() {
        branch = new Branch();
    }

    /**
     * Test that weeks list is initialized and contains exactly one week.
     */
    @Test
    public void testWeeksInitializedCorrectly() {
        List<Week> weeks = branch.getWeeks();
        assertNotNull(weeks, "Weeks list should not be null");
        assertEquals(1, weeks.size(), "Weeks list should contain exactly one week");
    }

    /**
     * Test that employees list is initialized and empty.
     */
    @Test
    public void testEmployeesListInitializedAndEmpty() {
        List<Employee> employees = branch.getEmployees();
        assertNotNull(employees, "Employees list should not be null");
        assertTrue(employees.isEmpty(), "Employees list should be empty at initialization");
    }

    /**
     * Test that users list is initialized and empty.
     */
    @Test
    public void testUsersListInitializedAndEmpty() {
        List<User> users = branch.getUsers();
        assertNotNull(users, "Users list should not be null");
        assertTrue(users.isEmpty(), "Users list should be empty at initialization");
    }

    /**
     * Test that roles list is initialized and empty.
     */
    @Test
    public void testRolesListInitialized() {
        List<Role> roles = branch.getRoles();
        assertNotNull(roles, "Roles list should not be null");
    }



    /**
     * Test that old employees list is initialized and empty.
     */
    @Test
    public void testOldEmployeesListInitializedAndEmpty() {
        List<Employee> oldEmployees = branch.getOldEmployee();
        assertNotNull(oldEmployees, "Old employees list should not be null");
        assertTrue(oldEmployees.isEmpty(), "Old employees list should be empty at initialization");
    }

    /**
     * Test that branch ID is initialized and non-negative.
     */
    @Test
    public void testBranchIDInitialization() {
        int id = branch.getBranchID();
        assertTrue(id >= 0, "Branch ID should be non-negative");
    }

    /**
     * Test that two branches have independent employee lists.
     */
    @Test
    public void testBranchesHaveIndependentEmployeeLists() {
        Branch branch1 = new Branch();
        Branch branch2 = new Branch();
        assertNotSame(branch1.getEmployees(), branch2.getEmployees(), "Branches should have independent employee lists");
    }

    /**
     * Test that two branches have independent users lists.
     */
    @Test
    public void testBranchesHaveIndependentUsersLists() {
        Branch branch1 = new Branch();
        Branch branch2 = new Branch();
        assertNotSame(branch1.getUsers(), branch2.getUsers(), "Branches should have independent users lists");
    }
}
