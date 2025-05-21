package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.IEmployeeManager;
import HR_Mudol.domain.IRoleManager;
import HR_Mudol.domain.*;

import java.util.List;

/**
 * EmployeeManagerMock is a special lightweight version of an employee manager.
 * It is used only for unit testing to provide controlled responses without requiring real system setup.
 */
public class EmployeeManagerMock implements IEmployeeManager {

    private Branch branch;

    /**
     * Constructs a mock employee manager based on a given branch.
     * @param branch the branch to operate on (usually a TestBranch instance).
     */
    public EmployeeManagerMock(Branch branch) {
        this.branch = branch;
    }

    @Override
    public void addEmployee(User caller) {

    }

    @Override
    public void removeEmployee(User caller) {

    }

    @Override
    public void updateBankAccount(User caller) {

    }

    @Override
    public void updateSalary(User caller) {

    }

    @Override
    public void updateMinDayShift(User caller) {

    }

    @Override
    public void updateMinEveningShift(User caller) {

    }

    @Override
    public void setInitialsickDays(User caller) {

    }

    @Override
    public void setInitialdaysOff(User caller) {

    }

    @Override
    public Employee getEmployeeById(User caller, int empId) {
        for (Employee emp : branch.getEmployees()) {
            if (emp.getEmpId() == empId) {
                return emp;
            }
        }
        throw new IllegalArgumentException("Employee with ID " + empId + " not found.");
    }

    @Override
    public Branch getBranch() {
        return null;
    }

    @Override
    public IRoleManager getRoleManager() {
        return null;
    }

    @Override
    public void printEmployees(User caller) {

    }

    @Override
    public void printAllEmployees(User caller) {

    }

    @Override
    public List<User> getAllUsers(User caller) {
        return branch.getUsers();
    }
}
