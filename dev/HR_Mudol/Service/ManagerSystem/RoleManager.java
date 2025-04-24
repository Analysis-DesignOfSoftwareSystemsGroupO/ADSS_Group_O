package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RoleManager implements IRoleManager {
    private Branch curBranch; //The role at the branch
    private final Scanner scanner;
    private IEmployeeManager employeeManager;

    public RoleManager(Branch curBranch) {
        this.curBranch=curBranch;
        Role Shift_Manager=new Role("Shift Manager");
        this.curBranch.getRoles().add(Shift_Manager);
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

        // בדיקה אם התיאור ריק
        if (description.trim().isEmpty()) {
            System.out.println("Role description cannot be empty.");
            return;
        }


        for (Role r: curBranch.getRoles()){
            if (r.getDescription().equalsIgnoreCase(description)){
                System.out.println("This role already exist.");
                return;
            }
        }
        Role role = new Role(description);
        curBranch.getRoles().add(role);

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

        if (roleNumber==1){
            assignEmployeeToShiftManager(caller);
        }
        else {
            System.out.print("Enter employee ID: ");
            int empId = scanner.nextInt();
            Employee employee;

            while (true) {
                try {
                    employee = employeeManager.getEmployeeById(caller, empId);
                    break;

                } catch (Exception e) {
                    System.out.print("Employee not found, please insert ID again :");
                    System.out.print("Enter employee ID: ");
                    empId = scanner.nextInt();
                }
            }
            Role role = getRoleByNumber(roleNumber);

            role.addNewEmployee(caller, employee);
            employee.addNewRole(caller,role);

            System.out.println(employee.getEmpName()+" assigned to "+role.getDescription()+".");
        }
    }


    @Override
    public void assignEmployeeToShiftManager(User caller) {

        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();
        Employee employee;
        while (true) {

            try {
                employee = employeeManager.getEmployeeById(caller, empId);
                break;
            } catch (Exception e) {

                System.out.print("Employee wasn't found, Enter employee ID again: ");
            }
            empId = scanner.nextInt();
            scanner.nextLine();
        }

        //if already exist "Shift Manager" role
        Role shiftManagerRole = null;
        for (Role role : curBranch.getRoles()) {
            if (role.getDescription().equalsIgnoreCase("Shift Manager")) {
                shiftManagerRole = role;
                break;
            }
        }

        // checks if the emp already shift manager
        if (!shiftManagerRole.getRelevantEmployees(caller).contains(employee)) {
            shiftManagerRole.addNewEmployee(caller, employee);
            employee.addNewRole(caller,shiftManagerRole);
            System.out.println(employee.getEmpName() + " assigned to 'Shift Manager' role.");
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

        return curBranch.getRoles();
    }

    @Override
    public void printAllRoles(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");
        for (Role role : curBranch.getRoles()) {
            System.out.println(role);
        }
    }

    @Override
    public Role getRoleByNumber(int roleNumber) {
        for (Role r : curBranch.getRoles()) {
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