package HR_Mudol.presentation;

import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RoleManager implements IRoleManager {
    private List<Role> allRoles; //The role at the branch
    private final Scanner scanner;
    private IEmployeeManager employeeManager;

    public RoleManager() {
        this.allRoles = new LinkedList<>();
        this.scanner = new Scanner(System.in);
    }
    public void setEmployeeManager(IEmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }


    @Override
    public void createRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter role description: ");
        String description = scanner.nextLine();

        Role role = new Role(caller, description);
        allRoles.add(role);

        System.out.println("Role created successfully");
    }

    @Override
    public void updateRoleDescription(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        printAllRoles(caller);
        System.out.print("Enter role number to update: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new description: ");
        String newDesc = scanner.nextLine();

        Role role = getRoleByNumber(roleNumber);
        role.SetDescription(caller, newDesc);

        System.out.println("Role description updated successfully.");
    }

    @Override
    public void assignEmployeeToRole(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        printAllRoles(caller);
        System.out.print("Enter role number: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        Employee employee = employeeManager.getEmployeeById(caller, empId);

        Role role = getRoleByNumber(roleNumber);
        role.addNewEmployee(caller,employee);

        System.out.println("Employee assigned to role.");
    }


    @Override
    public void assignEmployeeToShiftManager(User caller) {

        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        Employee employee = employeeManager.getEmployeeById(caller, empId);

        //if already exist "Shift Manager" role
        Role shiftManagerRole = null;
        for (Role role : allRoles) {
            if (role.getDescription().equalsIgnoreCase("Shift Manager")) {
                shiftManagerRole = role;
                break;
            }
        }
        //else
        if (shiftManagerRole == null) {
            shiftManagerRole = new Role(caller, "Shift Manager");
            allRoles.add(shiftManagerRole);
            System.out.println("Created 'Shift Manager' role.");
        }

        // checks if the emp already shift manager
        if (!shiftManagerRole.getRelevantEmployees(caller).contains(employee)) {
            shiftManagerRole.addNewEmployee(caller, employee);
            System.out.println("Employee assigned to 'Shift Manager' role.");
        } else {
            System.out.println("Employee is already assigned to 'Shift Manager' role.");
        }

        //update his user level:

        User targetUser = null;
        for (User u : employeeManager.getAllUsers(caller)) {
            if (u.getUser().equals(employee)) {
                targetUser = u;
                break;
            }
        }
        if (targetUser != null) {
            targetUser.setLevel(caller,Level.shiftManager);
            System.out.println("User level updated to 'Shift Manager'.");
        } else {
            System.out.println("Warning: Could not update user level. User not found.");
        }
    }

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

        Employee e = employeeManager.getEmployeeById(caller, empId);
        Role role = getRoleByNumber(roleNumber);
        role.removeEmployee(caller,e);

        System.out.println("Employee removed from role.");
    }

    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e){
        if (!caller.isManager()) throw new SecurityException("Access denied");
        Role role = getRoleByNumber(roleNumber);
        role.removeEmployee(caller,e);
    }

    @Override
    public List<Employee> getRelevantEmployees(User caller) {

        if (!caller.isManager()||!caller.isShiftManager()) throw new SecurityException("Access denied");

        printAllRoles(caller);
        System.out.print("Enter role number: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine();

        Role role = getRoleByNumber(roleNumber);
        return new LinkedList<>(role.getRelevantEmployees(caller));
    }

    @Override
    public List<Role> getAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        return new LinkedList<>(allRoles);
    }

    @Override
    public void printAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");
        for (Role role : allRoles) {
            System.out.println(role);
        }
    }

    private Role getRoleByNumber(int roleNumber) {
        for (Role r : allRoles) {
            if (r.getRoleNumber() == roleNumber) return r;
        }
        throw new IllegalArgumentException("Role not found");
    }
}