package HR_Mudol.domain;

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
    private RoleRepository roleRepo;
    private Scanner scanner;
    private IEmployeeController employeeManager;

    public RoleController(Branch curBranch) {
        this.curBranch = curBranch;
        this.roleRepo = curBranch.getRoleRepo();
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

        for (Role role : roleRepo.getAll()) {
            if (role.getDescription().equalsIgnoreCase(description)) {
                System.out.println("Role already exists.");
                return;
            }
        }

        roleRepo.add(new Role(description));
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
        try {
            role.SetDescription(caller, newDesc);
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

        try {
            role.addNewEmployee(caller, employee);
            System.out.println("Employee assigned to role.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
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

        try {
            Role shiftManager = roleRepo.getAll().get(0); // assuming first is always Shift Manager
            shiftManager.addNewEmployee(caller, employee);
            System.out.println("Employee assigned as Shift Manager.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEmployeeFromRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getIntInput("Enter employee ID: ");
        Employee employee = employeeManager.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        for (Role role : roleRepo.getAll()) {
            try {
                role.removeEmployee(caller, employee);
            } catch (SecurityException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Employee removed from all roles.");
    }

    @Override
    public void removeEmployeeFromRole(User caller, int roleId, Employee employee) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        for (Role role : roleRepo.getAll()) {
            if (role.getRoleNumber() == roleId) {
                try {
                    role.removeEmployee(caller, employee);
                } catch (SecurityException e) {
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        System.out.println("Role not found.");
    }

    @Override
    public List<Employee> getRelevantEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        List<Employee> relevant = new ArrayList<>();
        for (Role role : roleRepo.getAll()) {
            try {
                relevant.addAll(role.getRelevantEmployees(caller));
            } catch (SecurityException e) {
                System.out.println(e.getMessage());
            }
        }
        return relevant;
    }

    @Override
    public List<Role> getAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");
        return roleRepo.getAll();
    }

    @Override
    public void printAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        for (Role role : roleRepo.getAll()) {
            System.out.println(role);
        }
    }

    @Override
    public Role getRoleByNumber(int roleNumber) {
        for (Role role : roleRepo.getAll()) {
            if (role.getRoleNumber() == roleNumber)
                return role;
        }
        return null;
    }

    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int count = 0;
        for (Employee emp : employeeList) {
            boolean found = false;
            for (Role role : roleRepo.getAll()) {
                try {
                    if (role.getRelevantEmployees(caller).contains(emp)) {
                        found = true;
                        break;
                    }
                } catch (SecurityException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (!found) count++;
        }
        return count;
    }

    public void printEmployeesInRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        for (Role role : roleRepo.getAll()) {
            System.out.println("Role: " + role.getDescription());
            try {
                for (Employee emp : role.getRelevantEmployees(caller)) {
                    System.out.println(" - " + emp);
                }
            } catch (SecurityException e) {
                System.out.println(e.getMessage());
            }
        }
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
