package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.IRoleManager;
import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;

public class RoleManagerMock implements IRoleManager {

    private List<Role> roles = new LinkedList<>();

    public RoleManagerMock() {
        // מוסיף תפקיד ברירת מחדל — "Shift Manager"
        Role defaultRole = new Role("Shift Manager");
        roles.add(defaultRole);
    }

    public void addMockRole(Role role) {
        roles.add(role);
    }

    @Override
    public List<Role> getAllRoles(User caller) {
        return roles;
    }

    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        for (Role role : roles) {
            if (role.getRoleNumber() == roleNumber) {
                try {
                    role.removeEmployee(caller, e);
                } catch (Exception ignored) {}
            }
        }
    }

    // מתודות מיותרות ליוניט טסטים — נשאיר ריקות
    @Override public void createRole(User caller) {}
    @Override public void updateRoleDescription(User caller) {}
    @Override public void assignEmployeeToRole(User caller) {}
    @Override public void assignEmployeeToShiftManager(User caller) {}
    @Override public void removeEmployeeFromRole(User caller) {}
    @Override public List<Employee> getRelevantEmployees(User caller) { return new LinkedList<>(); }
    @Override public void printAllRoles(User caller) {}

    @Override
    public Role getRoleByNumber(int roleNumber) {
        for (Role r : roles) {
            if (r.getRoleNumber() == roleNumber) return r;
        }
        return null;
    }

    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        int count = 0;
        for (Employee e : employeeList) {
            if (e.getRelevantRoles(caller).isEmpty()) count++;
        }
        return count;
    }
}
