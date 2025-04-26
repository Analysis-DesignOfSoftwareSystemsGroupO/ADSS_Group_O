/**
 * RoleManagerMock is a simplified mock implementation of the IRoleManager interface.
 *
 * It is used exclusively for unit testing purposes, mainly inside the EmployeeManagerTest and ShiftManagerTest classes.
 *
 * Purpose:
 * - Simulate basic role management operations (adding/removing employees to roles).
 * - Allow tests to run independently of the real RoleManager logic.
 * - Provide predictable behavior without involving complex security checks or validation.
 *
 * Key Features:
 * - Stores a simple list of roles in memory.
 * - Automatically initializes with a default role ("Shift Manager") for testing.
 * - Skips security and validation logic to simplify test environments.
 * - Empty implementations for non-relevant methods.
 *
 * Main usage:
 * - Injected into EmployeeManager or ShiftManager during test setup.
 * - Ensures that unit tests remain focused, isolated, and deterministic.
 */
package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.IRoleManager;
import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;

public class RoleManagerMock implements IRoleManager {

    private List<Role> roles = new LinkedList<>(); // Stores roles manually for test purposes

    /**
     * Constructor initializes the mock with a default "Shift Manager" role.
     */
    public RoleManagerMock() {
        Role defaultRole = new Role("Shift Manager");
        roles.add(defaultRole);
    }

    /**
     * Adds a manually created role to the mock.
     *
     * @param role The role to add.
     */
    public void addMockRole(Role role) {
        roles.add(role);
    }

    /**
     * Returns all roles without permission checks (mock behavior).
     *
     * @param caller The user requesting the roles (ignored in mock).
     * @return List of all roles.
     */
    @Override
    public List<Role> getAllRoles(User caller) {
        return roles;
    }

    /**
     * Removes an employee from a role by role number, ignoring exceptions.
     *
     * @param caller The user requesting the operation.
     * @param roleNumber The role number.
     * @param e The employee to remove.
     */
    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        for (Role role : roles) {
            if (role.getRoleNumber() == roleNumber) {
                try {
                    role.removeEmployee(caller, e);
                } catch (Exception ignored) {
                    // Ignore exceptions for simplicity
                }
            }
        }
    }

    /**
     * Finds a role by its role number.
     *
     * @param roleNumber The number of the role to find.
     * @return The matching role, or null if not found.
     */
    @Override
    public Role getRoleByNumber(int roleNumber) {
        for (Role r : roles) {
            if (r.getRoleNumber() == roleNumber) {
                return r;
            }
        }
        return null;
    }

    /**
     * Counts how many employees from a list do not have any assigned role.
     *
     * @param caller The user requesting the count (ignored in mock).
     * @param employeeList List of employees to check.
     * @return Number of employees without assigned roles.
     */
    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        int count = 0;
        for (Employee e : employeeList) {
            if (e.getRelevantRoles(caller).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    // ---------- Unused methods in this mock (left intentionally empty) ----------

    @Override public void createRole(User caller) {}
    @Override public void updateRoleDescription(User caller) {}
    @Override public void assignEmployeeToRole(User caller) {}
    @Override public void assignEmployeeToShiftManager(User caller) {}
    @Override public void removeEmployeeFromRole(User caller) {}
    @Override public List<Employee> getRelevantEmployees(User caller) { return new LinkedList<>(); }
    @Override public void printAllRoles(User caller) {}
}
