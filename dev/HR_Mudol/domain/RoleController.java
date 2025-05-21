package HR_Mudol.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages roles within a branch: creation, assignment, removal, and description updates.
 * Ensures security by validating manager privileges.
 */
public class RoleManager implements IRoleManager {

    private Branch curBranch;
    private Scanner scanner;
    private IEmployeeManager employeeManager;


    /**
     * Constructs a RoleManager for the given branch.
     *
     * @param curBranch the current branch managed
     */
    public RoleManager(Branch curBranch) {
        this.curBranch = curBranch;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Sets the employee manager.
     *
     * @param employeeManager the employee manager to set
     */
    public void setEmployeeManager(IEmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }


    /**
     * Creates a new role in the branch after validating manager privileges.
     *
     * @param caller the user initiating the action
     */
    @Override
    public void createRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        System.out.print("Enter role description: ");
        String description = scanner.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Role description cannot be empty.");
            return;
        }

        for (Role role : curBranch.getRoles()) {
            if (role.getDescription().equalsIgnoreCase(description)) {
                System.out.println("This role already exists.");
                return;
            }
        }

        Role newRole = new Role(description);
        curBranch.getRoles().add(newRole);
        System.out.println("Role created successfully.");
    }


    /**
     * Updates the description of an existing role.
     *
     * @param caller the user initiating the action
     */
    @Override
    public void updateRoleDescription(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        printAllRoles(caller);

        int roleNumber = getValidIntInput("Enter role number to update: ");
        Role role = getRoleByNumber(roleNumber);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }

        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine().trim();

        if (newDescription.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        role.SetDescription(caller, newDescription);
        System.out.println("Role description updated successfully.");
    }

    /**
     * Assigns an employee to a specific role.
     *
     * @param caller the user initiating the action
     */
    @Override
    public void assignEmployeeToRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        printAllRoles(caller);
        int roleNumber = getValidIntInput("Enter role number: ");

        if (roleNumber == 1) {
            assignEmployeeToShiftManager(caller);
            return;
        }

        int empId = getValidIntInput("Enter employee ID: ");
        Employee employee = findValidEmployee(caller, empId);

        if (employee == null) {
            System.out.println("Employee not found. Cannot assign role.");
            return;
        }

        Role role = getRoleByNumber(roleNumber);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }

        role.addNewEmployee(caller, employee);
        employee.addNewRole(caller, role);

        System.out.println(employee.getEmpName() + " assigned to " + role.getDescription() + ".");
    }

    /**
     * Specifically assigns an employee to the "Shift Manager" role and updates the user's level.
     *
     * @param caller the user initiating the action
     */
    @Override
    public void assignEmployeeToShiftManager(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getValidIntInput("Enter employee ID: ");
        Employee employee = findValidEmployee(caller, empId);
        if (employee == null) {
            System.out.println("Employee not found. Cannot assign shift manager role.");
            return;
        }

        Role shiftManagerRole = null;
        for (Role role : curBranch.getRoles()) {
            if (role.getDescription().equalsIgnoreCase("Shift Manager")) {
                shiftManagerRole = role;
                break;
            }
        }

        if (shiftManagerRole == null) {
            System.out.println("'Shift Manager' role does not exist.");
            return;
        }

        if (!shiftManagerRole.getRelevantEmployees(caller).contains(employee)) {
            shiftManagerRole.addNewEmployee(caller, employee);
            employee.addNewRole(caller, shiftManagerRole);
            System.out.println(employee.getEmpName() + " assigned to 'Shift Manager' role.");
        } else {
            System.out.println("Employee is already assigned to 'Shift Manager' role.");
        }

        User targetUser = findUserByEmployee(caller, employee);
        if (targetUser != null) {
            targetUser.setLevel(caller, Level.shiftManager);
            System.out.println("User level updated to 'Shift Manager'.");
        } else {
            System.out.println("Warning: User not found for employee.");
        }
    }

    /**
     * Removes an employee from a specific role.
     *
     * @param caller the user initiating the action
     */
    @Override
    public void removeEmployeeFromRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        printAllRoles(caller);
        int roleNumber = getValidIntInput("Enter role number: ");
        int empId = getValidIntInput("Enter employee ID: ");

        try {
            Employee employee = employeeManager.getEmployeeById(caller, empId);
            Role role = getRoleByNumber(roleNumber);

            if (role == null) {
                System.out.println("Role not found.");
                return;
            }

            role.removeEmployee(caller, employee);
            System.out.println("Employee removed from role.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Removes an employee from a specific role by role number and employee object.
     *
     * @param caller the user initiating the action
     * @param roleNumber the number of the role
     * @param employee the employee to remove
     */
    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee employee) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        Role role = getRoleByNumber(roleNumber);
        if (role != null) {
            role.removeEmployee(caller, employee);
        } else {
            System.out.println("Role not found.");
        }
    }
    /**
     * Retrieves a list of employees assigned to a specific role.
     *
     * @param caller the user initiating the action
     * @return a list of relevant employees
     */
    @Override
    public List<Employee> getRelevantEmployees(User caller) {
        if (!caller.isManager() && !caller.isShiftManager())
            throw new SecurityException("Access denied.");

        printAllRoles(caller);
        int roleNumber = getValidIntInput("Enter role number: ");

        Role role = getRoleByNumber(roleNumber);
        if (role == null) {
            System.out.println("Role not found.");
            return new LinkedList<>();
        }

        return new LinkedList<>(role.getRelevantEmployees(caller));
    }
    /**
     * Retrieves all roles within the branch.
     *
     * @param caller the user initiating the action
     * @return a list of all roles
     */
    @Override
    public List<Role> getAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");
        return curBranch.getRoles();
    }
    /**
     * Prints all roles in the branch.
     *
     * @param caller the user initiating the action
     */
    @Override
    public void printAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        for (Role role : curBranch.getRoles()) {
            System.out.println(role);
        }
    }
    /**
     * Retrieves a role by its role number.
     *
     * @param roleNumber the number of the role
     * @return the Role object, or null if not found
     */
    @Override
    public Role getRoleByNumber(int roleNumber) {
        for (Role role : curBranch.getRoles()) {
            if (role.getRoleNumber() == roleNumber) {
                return role;
            }
        }
        return null;
    }
    /**
     * Counts the number of employees who are not assigned to any role.
     *
     * @param caller the user initiating the action
     * @param employeeList the list of employees to check
     * @return the count of employees without roles
     */
    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        int count = 0;
        for (Employee employee : employeeList) {
            if (employee.getRelevantRoles(caller).isEmpty()) {
                count++;
            }
        }
        return count;
    }
    /**
     * Sets a custom scanner for input, mainly for testing purposes.
     *
     * @param scanner the Scanner to set
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Helper method to retrieve a valid integer input from the user.
     *
     * @param prompt the prompt to display to the user
     * @return a valid integer input
     */
    private int getValidIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                return value;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    /**
     * Helper method to find a valid employee by ID.
     *
     * @param caller the user initiating the action
     * @param empId the employee ID to find
     * @return the Employee object if found
     */
    private Employee findValidEmployee(User caller, int empId) {
        while (true) {
            try {
                return employeeManager.getEmployeeById(caller, empId);
            } catch (Exception e) {
                System.out.print("Employee not found, enter ID again: ");
                empId = getValidIntInput("");
            }
        }
    }
    /**
        * Helper method to find a user associated with a specific employee.
     *
             * @param caller the user initiating the action
     * @param employee the employee to find the user for
            * @return the corresponding User object, or null if not found
     */
    private User findUserByEmployee(User caller, Employee employee) {
        for (User user : employeeManager.getAllUsers(caller)) {
            if (user.getUser().equals(employee)) {
                return user;
            }
        }
        return null;
    }
}
