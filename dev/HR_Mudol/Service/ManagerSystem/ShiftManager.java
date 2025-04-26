package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.List;
import java.util.Scanner;

/**
 * ShiftManager class manages shift operations:
 * Assign employees to shifts, remove employees, add/remove roles, and print shifts.
 */
public class ShiftManager implements IShiftManager {

    private IRoleManager dependency; // Dependency for accessing role management


    /**
     * Constructor for ShiftManager.
     * @param dependency The role manager dependency used for role-related operations.
     */
    public ShiftManager(IRoleManager dependency) {
        this.dependency = dependency;
    }
    /**
     * Assigns an employee to a shift.
     * This operation checks if the caller is a manager or shift manager before proceeding.
     * @param caller The user (manager or shift manager) who is assigning the employee.
     * @param shift The shift to which the employee is being assigned.
     * @param employee The employee being assigned to the shift.
     * @param role The role the employee will have in the shift.
     * @throws SecurityException if the caller does not have manager or shift manager privileges.
     */
    @Override
    public void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {
        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        // Assign employee to the shift
        shift.addEmployee(caller, employee, role);
        System.out.println(employee.getEmpName() +
                " assigned to shift " + shift.getDay() + " - " + shift.getType() + ".");
    }

    /**
     * Removes an employee from a shift.
     * The user must be a manager or shift manager.
     * @param caller The user (manager or shift manager) who is removing the employee.
     * @param shift The shift from which the employee is being removed.
     * @throws SecurityException if the caller does not have manager or shift manager privileges.
     */
    @Override
    public void removeEmployeeFromShift(User caller, Shift shift) {
        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        // Check if shift exists
        if (shift == null) {
            System.out.println("Shift doesn't exist.");
            return;
        }

        List<Employee> employees = shift.getEmployees();
        // Check if no employees assigned
        if (employees.isEmpty()) {
            System.out.println("No employees assigned to this shift.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an employee to remove by their index:");

        // Print list of employees with indexes
        int index = 1;
        for (Employee e : employees) {
            System.out.println(index + ". " + e.getEmpName());
            index++;
        }

        String input = scanner.nextLine().trim();
        int chosenIndex;

        try {
            chosenIndex = Integer.parseInt(input);
            if (chosenIndex < 1 || chosenIndex > employees.size()) {
                System.out.println("Invalid index. Please enter a number between 1 and " + employees.size());
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        // Remove selected employee
        Employee employeeToRemove = employees.get(chosenIndex - 1);
        shift.removeEmployee(caller, employeeToRemove);

        System.out.println(employeeToRemove.getEmpName() +
                " was removed from shift " + shift.getDay() + " - " + shift.getType() + ".");
    }


    /**
     * Removes a role from a shift.
     * The user must be a manager or shift manager.
     * @param caller The user (manager or shift manager) who is removing the role.
     * @param shift The shift from which the role is being removed.
     */
    @Override
    public void removeRoleFromShift(User caller, Shift shift) {
        Scanner scanner = new Scanner(System.in);
        List<Role> relevantRoles = shift.getNecessaryRoles();

        // Check if there are no roles assigned
        if (relevantRoles.isEmpty()) {
            System.out.println("There are no roles assigned to this shift.");
            return;
        }

        Role role = null;

        // Loop until a valid role is selected
        while (role == null) {
            System.out.println("\nChoose a role to remove from shift " + shift.getDay() + " - " + shift.getType());
            printRolesListForShift(caller, relevantRoles);
            System.out.print("Enter role ID (or type 'exit' to cancel): ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Operation cancelled.");
                return;
            }

            try {
                int roleId = Integer.parseInt(input);
                role = findRoleById(relevantRoles, roleId);

                if (role == null) {
                    System.out.println("This role is not assigned to the shift. Please try again.");
                    role = null; // Stay in the loop
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid role ID.");
            }
        }

        // Remove the selected role
        shift.removeRole(caller, role);
        System.out.println("Role \"" + role.getDescription() + "\" was removed from the shift.");
    }


    /**
     * Prints a list of roles assigned to a shift.
     * @param caller The user (manager or shift manager) who is printing the roles.
     * @param roles The list of roles assigned to the shift.
     */
    private void printRolesListForShift(User caller, List<Role> roles) {
        // Print roles assigned to the shift
        System.out.println("Roles assigned to this shift:");
        for (Role role : roles) {
            System.out.println("Role ID: " + role.getRoleNumber() + " - " + role.getDescription());
        }
    }

    /**
     * Finds a role by its ID from a list of roles.
     * @param roles The list of roles to search in.
     * @param roleId The ID of the role to find.
     * @return The role with the matching ID, or null if not found.
     */
    private Role findRoleById(List<Role> roles, int roleId) {
        // Find a role by its ID
        for (Role role : roles) {
            if (role.getRoleNumber() == roleId) {
                return role;
            }
        }
        return null;
    }

    /**
     * Helper method to check if a shift already has a role by ID.
     * @param shift The shift to check.
     * @param roleId The ID of the role to check.
     * @return true if the shift already has the role, false otherwise.
     */
    private boolean shiftAlreadyHasRole(Shift shift, int roleId) {
        for (Role role : shift.getNecessaryRoles()) {
            if (role.getRoleNumber() == roleId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Chooses relevant roles for a shift.
     * The user must be a manager or shift manager.
     * @param caller The user (manager or shift manager) who is adding roles.
     * @param shift The shift to which roles are being added.
     */
    @Override
    public void chooseRelevantRoleForShift(User caller, Shift shift) {
        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        // Add Shift Manager only if not already added (by role number check)
        Role shiftManagerRole = dependency.getRoleByNumber(1);
        if (shiftManagerRole != null && !shiftAlreadyHasRole(shift, shiftManagerRole.getRoleNumber())) {
            shift.addNecessaryRoles(caller, shiftManagerRole);
        }

        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            printRolesList(caller, dependency.getAllRoles(caller));

            int roleNumber = -1;
            while (true) {
                System.out.print("Enter relevant role number to add to the shift " +
                        shift.getDay() + " - " + shift.getType() + ": ");
                String input = scanner.nextLine();

                try {
                    roleNumber = Integer.parseInt(input);
                    Role role = dependency.getRoleByNumber(roleNumber);

                    if (role == null) {
                        System.out.println("Role number does not exist. Please try again.");
                    } else if (role.getRoleNumber() == 1) {
                        System.out.println("Shift Manager already added. Please choose another role.");
                    } else if (shiftAlreadyHasRole(shift, role.getRoleNumber())) {
                        System.out.println("Role already assigned to the shift. Please choose another role.");
                    } else {
                        shift.addNecessaryRoles(caller, role);
                        System.out.println(role.getDescription() + " was added to the shift.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            while (true) {
                System.out.print("If you're done write D, else write N: ");
                String isDone = scanner.nextLine().trim();

                if (isDone.equalsIgnoreCase("D")) {
                    done = true;
                    break;
                } else if (isDone.equalsIgnoreCase("N")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'D' or 'N'.");
                }
            }
        }
    }


    /**
     * Prints the details of a shift.
     * @param caller The user (manager or shift manager) who is printing the shift details.
     * @param shift The shift to print.
     */
    @Override
    public void printShift(User caller, Shift shift) {
        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        System.out.println(shift.toString());
    }


    /**
     * Adds an employee to a shift.
     * This method ensures the caller has the correct privileges to perform the action.
     * @param caller The user (manager or shift manager) who is adding the employee.
     * @param shift The shift to which the employee is being added.
     * @param employee The employee being added to the shift.
     * @param role The role the employee will take in the shift.
     */
    @Override
    public void addEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {
        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }
        shift.addEmployee(caller, employee, role);
    }

    /**
     * Helper -Prints the list of roles available for the caller to choose from.
     * @param caller The user (manager or shift manager) who is viewing the roles.
     * @param roles The list of available roles.
     */
    private void printRolesList(User caller, List<Role> roles) {
        for (Role r : roles) {
            System.out.println(r.getRoleNumber() + " - " + r.getDescription());

        }
    }
}
