package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RoleManager implements IRoleManager {

    private Branch curBranch; // The branch where roles are managed
    private Scanner scanner;
    private IEmployeeManager employeeManager;

    public RoleManager(Branch curBranch) {
        this.curBranch = curBranch;
        // Initialize with a default "Shift Manager" role
        Role Shift_Manager = new Role("Shift Manager");
        this.curBranch.getRoles().add(Shift_Manager);
        this.scanner = new Scanner(System.in);
    }

    public void setEmployeeManager(IEmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    @Override
    // Create a new role based on user input
    public void createRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter role description: ");
        String description = scanner.nextLine();

        // Check if description is empty
        if (description.trim().isEmpty()) {
            System.out.println("Role description cannot be empty.");
            return;
        }

        // Check for duplicate role description
        for (Role r : curBranch.getRoles()) {
            if (r.getDescription().equalsIgnoreCase(description)) {
                System.out.println("This role already exists.");
                return;
            }
        }

        // Create and add the new role
        Role role = new Role(description);
        curBranch.getRoles().add(role);

        System.out.println("Role created successfully.");
    }

    @Override
    // Update the description of an existing role
    public void updateRoleDescription(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        printAllRoles(caller);
        System.out.print("Enter role number to update: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        System.out.print("Enter new description: ");
        String newDesc = scanner.nextLine();

        Role role = getRoleByNumber(roleNumber);
        role.SetDescription(caller, newDesc);

        System.out.println("Role description updated successfully.");
    }

    @Override
    // Assign an employee to a specific role (or Shift Manager if role 1 is selected)
    public void assignEmployeeToRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        printAllRoles(caller);

        System.out.print("Enter role number: ");
        int roleNumber = scanner.nextInt();

        if (roleNumber == 1) {
            assignEmployeeToShiftManager(caller); // Special handling for Shift Manager
        } else {
            System.out.print("Enter employee ID: ");
            int empId = scanner.nextInt();
            Employee employee;

            // Loop until a valid employee is found
            while (true) {
                try {
                    employee = employeeManager.getEmployeeById(caller, empId);
                    break;
                } catch (Exception e) {
                    System.out.print("Employee not found, please insert ID again: ");
                    empId = scanner.nextInt();
                }
            }

            Role role = getRoleByNumber(roleNumber);

            role.addNewEmployee(caller, employee);
            employee.addNewRole(caller, role);

            System.out.println(employee.getEmpName() + " assigned to " + role.getDescription() + ".");
        }
    }

    @Override
    // Assign an employee to the "Shift Manager" role
    public void assignEmployeeToShiftManager(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId;

        // Check if input is valid integer
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException("Invalid input. No valid employee ID entered.");
        }
        empId = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        Employee employee;
        while (true) {
            try {
                employee = employeeManager.getEmployeeById(caller, empId);
                break;
            } catch (Exception e) {
                System.out.print("Employee wasn't found, Enter employee ID again: ");
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Invalid input. No valid employee ID entered.");
                }
                empId = scanner.nextInt();
                scanner.nextLine(); // clear buffer
            }
        }

        // Find "Shift Manager" role
        Role shiftManagerRole = null;
        for (Role role : curBranch.getRoles()) {
            if (role.getDescription().equalsIgnoreCase("Shift Manager")) {
                shiftManagerRole = role;
                break;
            }
        }

        if (!shiftManagerRole.getRelevantEmployees(caller).contains(employee)) {
            shiftManagerRole.addNewEmployee(caller, employee);
            employee.addNewRole(caller, shiftManagerRole);
            System.out.println(employee.getEmpName() + " assigned to 'Shift Manager' role.");
        } else {
            System.out.println("Employee is already assigned to 'Shift Manager' role.");
        }

        // Update user's level
        User targetUser = null;
        for (User u : employeeManager.getAllUsers(caller)) {
            if (u.getUser().equals(employee)) {
                targetUser = u;
                break;
            }
        }
        if (targetUser != null) {
            targetUser.setLevel(caller, Level.shiftManager);
            System.out.println("User level updated to 'Shift Manager'.");
        } else {
            System.out.println("Warning: Could not update user level. User not found.");
        }
    }


    @Override
    // Remove an employee from a role based on user input
    public void removeEmployeeFromRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        printAllRoles(caller);
        System.out.print("Enter role number: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        try {
            Employee e = employeeManager.getEmployeeById(caller, empId);
            Role role = getRoleByNumber(roleNumber);
            role.removeEmployee(caller, e);

            System.out.println("Employee removed from role.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    // Remove an employee from a role (direct call without Scanner)
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        Role role = getRoleByNumber(roleNumber);
        role.removeEmployee(caller, e);
    }

    @Override
    // Retrieve employees assigned to a specific role
    public List<Employee> getRelevantEmployees(User caller) {
        if (!caller.isManager() || !caller.isShiftManager())
            throw new SecurityException("Access denied");

        printAllRoles(caller);
        System.out.print("Enter role number: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine();

        Role role = getRoleByNumber(roleNumber);
        return new LinkedList<>(role.getRelevantEmployees(caller));
    }

    @Override
    // Return all roles in the current branch
    public List<Role> getAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        return curBranch.getRoles();
    }

    @Override
    // Print all roles in the branch
    public void printAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        for (Role role : curBranch.getRoles()) {
            System.out.println(role);
        }
    }

    @Override
    // Retrieve a specific role by its number
    public Role getRoleByNumber(int roleNumber) {
        for (Role r : curBranch.getRoles()) {
            if (r.getRoleNumber() == roleNumber)
                return r;
        }
        return null;
    }

    @Override
    // Count how many employees have no roles assigned
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        int count = 0;
        for (Employee e : employeeList) {
            if (e.getRelevantRoles(caller).isEmpty())
                count++;
        }
        return count;
    }

    // Setter for injecting custom Scanner (used for testing)
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
