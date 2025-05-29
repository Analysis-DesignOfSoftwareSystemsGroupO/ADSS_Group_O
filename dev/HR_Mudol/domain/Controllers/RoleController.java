package HR_Mudol.domain.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.repository.RoleRepository;

/**
 * Manages roles within a branch: creation, assignment, removal, and description updates.
 * Ensures security by validating manager privileges.
 */
public class RoleController implements IRoleController {

    private Branch curBranch;
    private Scanner scanner;
    private IEmployeeController employeeManager;

    public RoleController(Branch curBranch) {
        this.curBranch = curBranch;
        this.scanner = new Scanner(System.in);
    }

    public void setEmployeeManager(IEmployeeController employeeManager) {
        this.employeeManager = employeeManager;
    }

    @Override
    public void createRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        System.out.print("Enter role description: ");
        String description = scanner.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Role description cannot be empty.");
            return;
        }

        for (Role role : curBranch.getRoleRepo().getAll()) {
            if (role.getDescription().equalsIgnoreCase(description)) {
                System.out.println("Role already exists.");
                return;
            }
        }

        //Create domain object - RAM
        Role newRole = new Role(description);

        //Add to DB
        curBranch.getRoleRepo().add(newRole); // internally converts to DTO and calls DAO

        System.out.println("Role created successfully.");
    }

    @Override
    public void updateRoleDescription(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int roleNumber = getIntInput("Enter role number to update: ");
        Role role = getRoleByNumber(roleNumber);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }

        System.out.print("Enter new description: ");
        String newDesc = scanner.nextLine().trim();

        if (newDesc.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        try {
            curBranch.getRoleRepo().updateDescription(role, newDesc);
            System.out.println("Role updated.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void assignEmployeeToRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getIntInput("Enter employee ID: ");
        Employee employee = employeeManager.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        int roleNumber = getIntInput("Enter role number to assign: ");
        Role role = getRoleByNumber(roleNumber);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }

        curBranch.getRoleRepo().assignEmployeeToRole(employee, role); //updates RAM and DB
        System.out.println("Employee assigned to role.");
    }

    @Override
    public void assignEmployeeToShiftManager(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getIntInput("Enter employee ID to promote to Shift Manager: ");
        Employee employee = employeeManager.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // Assuming role number 1 is Shift Manager
        Role shiftManager = curBranch.getRoleRepo().getRoleByNumber(1);
        if (shiftManager == null) {
            System.out.println("Shift Manager role not found.");
            return;
        }
        curBranch.getRoleRepo().assignEmployeeToRole(employee, shiftManager); //updates RAM and DB
        System.out.println("Employee assigned as Shift Manager.");
    }

    @Override
    public void removeEmployeeFromALLRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getIntInput("Enter employee ID: ");
        Employee employee = employeeManager.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // Remove the employee from all roles using the repository (memory + DB)
        curBranch.getRoleRepo().removeEmployeeFromAllRoles(employee);

        System.out.println("Employee removed from all roles.");
    }

    @Override
    public void removeEmployeeFromRole(User caller, int roleId, Employee employee) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        Role role = getRoleByNumber(roleId);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }
        try {
            role.removeEmployee(employee); //update RAM
            curBranch.getRoleRepo().removeEmployeeFromRole(employee, role); //update DB

            System.out.println("Employee removed from role: " + role.getDescription());
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getRelevantEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");
        return curBranch.getRoleRepo().getAllRelevantEmployees();
    }

    @Override
    public List<Role> getAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");
        return curBranch.getRoleRepo().getAllRoles();
    }

    @Override
    public void printAllRoles(User caller) {

        List<Role> roles = getAllRoles(caller);

        if (roles.isEmpty()) {
            System.out.println("No roles found.");
            return;
        }

        System.out.println("Available Roles:");
        for (Role role : roles) {
            System.out.println(role);
        }
    }


    @Override
    public Role getRoleByNumber(int roleNumber) {
        return curBranch.getRoleRepo().getRoleByNumber(roleNumber);
    }

    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        List<Integer> empIDsWithRoles = curBranch.getRoleRepo().getAllEmployeeIDsWithRoles();

        int count = 0;
        for (Employee emp : employeeList) {
            if (!empIDsWithRoles.contains(emp.getEmpId())) {
                count++;
            }
        }
        return count;
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return getIntInput(prompt);
        }
    }
}
