/**
 * RoleManagerMock is a simplified mock implementation of the IRoleManager interface.
 *
 * It is used exclusively for unit testing purposes, mainly inside the EmployeeManagerTest class.
 *
 * Purpose:
 * - Simulate basic role management operations (adding/removing employees to roles).
 * - Allow EmployeeManager tests to run independently of the real RoleManager logic.
 * - Provide predictable behavior without involving complex real-world constraints.
 *
 * Key Features:
 * - Stores a simple list of roles in memory.
 * - Automatically initializes with a default role ("Shift Manager") for testing.
 * - Skips security checks and exception throwing where possible to simplify tests.
 * - Empty implementation for non-critical methods that are not relevant to the tests.
 *
 * Main usage:
 * - Injected into EmployeeManager via setRoleManager(RoleManagerMock) during test setup in EmployeeManagerTest.
 *
 * This mock ensures that unit tests for employee operations remain focused, isolated, and deterministic.
 */
package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.IRoleManager;
import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;

public class RoleManagerMock implements IRoleManager {

    private List<Role> roles = new LinkedList<>(); // Stores roles manually for test purposes

    public RoleManagerMock() {
        // Add a default role: "Shift Manager"
        Role defaultRole = new Role("Shift Manager");
        roles.add(defaultRole);
    }

    public void addMockRole(Role role) {
        // Manually add a new role to the mock
        roles.add(role);
    }

    @Override
    public List<Role> getAllRoles(User caller) {
        // Return all roles without security checks (mock behavior)
        return roles;
    }

    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        // Remove an employee from a specific role if exists
        for (Role role : roles) {
            if (role.getRoleNumber() == roleNumber) {
                try {
                    role.removeEmployee(caller, e);
                } catch (Exception ignored) {
                    // Ignore exception for testing simplicity
                }
            }
        }
    }

    // Unused methods — left empty intentionally for unit testing purposes
    @Override public void createRole(User caller) {}
    @Override public void updateRoleDescription(User caller) {}
    @Override public void assignEmployeeToRole(User caller) {}
    @Override public void assignEmployeeToShiftManager(User caller) {}
    @Override public void removeEmployeeFromRole(User caller) {}
    @Override public List<Employee> getRelevantEmployees(User caller) { return new LinkedList<>(); }
    @Override public void printAllRoles(User caller) {}

    @Override
    public Role getRoleByNumber(int roleNumber) {
        // Find role by its number (or return null if not found)
        for (Role r : roles) {
            if (r.getRoleNumber() == roleNumber) return r;
        }
        return null;
    }

    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        // Count how many employees do not have any assigned role
        int count = 0;
        for (Employee e : employeeList) {
            if (e.getRelevantRoles(caller).isEmpty()) count++;
        }
        return count;
    }
}
