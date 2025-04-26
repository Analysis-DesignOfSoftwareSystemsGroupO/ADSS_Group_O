package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * This class manages roles within a branch. It allows the creation, assignment, and removal
 * of roles, as well as the management of employees associated with those roles.
 * A "Role" is a job description or position that employees can be assigned to.
 * The manager can also update role descriptions, assign employees to roles (including the "Shift Manager" role),
 * and remove employees from roles.
 *
 * The `RoleManager` ensures that role assignments and modifications are performed securely,
 * verifying that the user requesting changes is a manager. It also provides utilities
 * for managing and printing role-related information.
 */
public class RoleManager implements IRoleManager {

    private Branch curBranch; // The branch where roles are managed
    private Scanner scanner;
    private IEmployeeManager employeeManager;

    /**
     * Constructor to initialize the RoleManager with the current branch.
     * @param curBranch The branch where roles are managed.
     */
    public RoleManager(Branch curBranch) {
        this.curBranch = curBranch;
        // Initialize with a default "Shift Manager" role
        Role Shift_Manager = new Role("Shift Manager");
        this.curBranch.getRoles().add(Shift_Manager);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Setter for the Employee Manager.
     * @param employeeManager The employee manager to be injected.
     */
    public void setEmployeeManager(IEmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    /**
     * Creates a new role based on user input.
     * @param caller The user requesting the role creation.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
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

    /**
     * Updates the description of an existing role.
     * @param caller The user requesting the update.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
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

    /**
     * Assigns an employee to a specific role. If role 1 (Shift Manager) is selected, special handling is applied.
     * @param caller The user requesting the assignment.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
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

    /**
     * Assigns an employee to the "Shift Manager" role.
     * @param caller The user requesting the assignment.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
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

    /**
     * Removes an employee from a role based on user input.
     * @param caller The user requesting the removal.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
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

    /**
     * Removes an employee from a role (direct call without using Scanner).
     * @param caller The user requesting the removal.
     * @param roleNumber The role number to remove the employee from.
     * @param e The employee to be removed.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        Role role = getRoleByNumber(roleNumber);
        role.removeEmployee(caller, e);
    }


    /**
     * Retrieves employees assigned to a specific role.
     * @param caller The user requesting the list of employees.
     * @return A list of employees assigned to the specified role.
     * @throws SecurityException If the caller does not have manager or shift manager permissions.
     */
    @Override
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

    /**
     * Returns all roles in the current branch.
     * @param caller The user requesting the list of roles.
     * @return A list of all roles in the branch.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
    public List<Role> getAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        return curBranch.getRoles();
    }

    /**
     * Prints all roles in the current branch.
     * @param caller The user requesting to print the roles.
     * @throws SecurityException If the caller does not have manager permissions.
     */
    @Override
    public void printAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        for (Role role : curBranch.getRoles()) {
            System.out.println(role);
        }
    }

    /**
     * Retrieves a specific role by its number.
     * @param roleNumber The number of the role to retrieve.
     * @return The role with the specified number, or null if not found.
     */
    @Override
    public Role getRoleByNumber(int roleNumber) {
        for (Role r : curBranch.getRoles()) {
            if (r.getRoleNumber() == roleNumber)
                return r;
        }
        return null;
    }

    /**
     * Counts how many employees have no roles assigned.
     * @param caller The user requesting the count.
     * @param employeeList The list of employees to check.
     * @return The count of employees without roles.
     */
    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        int count = 0;
        for (Employee e : employeeList) {
            if (e.getRelevantRoles(caller).isEmpty())
                count++;
        }
        return count;
    }

    /**
     * Setter for injecting a custom Scanner (used for testing purposes).
     * @param scanner The scanner to be used for receiving input.
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
