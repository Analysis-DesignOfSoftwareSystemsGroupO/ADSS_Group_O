package HR_Mudol.presentation;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Role;
import HR_Mudol.domain.User;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManager implements IEmployeeManager {

    Scanner scanner = new Scanner(System.in);
    private List<User> users; //All the employees of the branch
    private List<Employee> employees; //All the employees of the branch
    private IRoleManager roleManager; // Reference to RoleManager to access all roles

    public EmployeeManager() {
        this.employees = new LinkedList<>();
        this.users= new LinkedList<>();
        //this.roleManager = roleManager;
    }
    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public List<User> getAllUsers(User caller){
        return this.users;
    }

    @Override
    public void addEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee full name: ");
        String empName = scanner.nextLine();

        System.out.print("Enter employee ID: ");
        int empID = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter initial password for employee: ");
        String empPassword = scanner.nextLine();

        System.out.print("Enter bank account: ");
        String empBankAccount = scanner.nextLine();

        System.out.print("Enter salary: ");
        int empSalary = scanner.nextInt();
        scanner.nextLine();

        LocalDate empStartDate = LocalDate.now();

        System.out.print("Enter minimum day shifts in contract: ");
        int minDay = scanner.nextInt();

        System.out.print("Enter minimum evening shifts in contract: ");
        int minEvening = scanner.nextInt();

        System.out.print("Enter number of sick days: ");
        int sicks = scanner.nextInt();

        System.out.print("Enter number of vacation days: ");
        int daysOff = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Create the employee
        Employee employee = new Employee(
                empName, empID, empPassword, empBankAccount, empSalary,
                empStartDate, minDay, minEvening, sicks, daysOff
        );

        employees.add(employee);

        // Create the user and add to list
        User newUser = new User(employee, Level.regularEmp);
        users.add(newUser);

        System.out.println("Employee and his user created successfully!");
    }


    @Override
    public void removeEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID to remove: ");
        int empId = scanner.nextInt();

        Employee toRemove = getEmployeeById(caller, empId);

        if (toRemove == null) {
            System.out.println("Employee not found.");
            return;
        }

        for (Role role : roleManager.getAllRoles(caller)) {
            roleManager.removeEmployeeFromRole(caller, role.getRoleNumber(), toRemove);
        }

        employees.remove(toRemove);
        System.out.println("Employee removed successfully from system.");
    }

    @Override
    public void updateBankAccount(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new bank account: ");
        String newBankAccount = scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        e.setEmpBankAccount(caller, newBankAccount);
        System.out.println("Changed successfully.");
    }

    @Override
    public void updateSalary(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        System.out.print("Enter new salary: ");
        int newSalary = scanner.nextInt();

        Employee e = getEmployeeById(caller, empId);
        e.setEmpSalary(caller, newSalary);

        System.out.println("Changed successfully.");
    }

    @Override
    public void updateMinDayShift(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        System.out.print("Enter new minimum day shifts: ");
        int newNumber = scanner.nextInt();

        Employee e = getEmployeeById(caller, empId);
        e.setMinDayShift(caller, newNumber);
        System.out.println("Changed successfully.");
    }

    @Override
    public void updateMinEveningShift(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        System.out.print("Enter new minimum evening shifts: ");
        int newNumber = scanner.nextInt();

        Employee e = getEmployeeById(caller, empId);
        e.setMinEveninigShift(caller, newNumber);
        System.out.println("Changed successfully.");
    }

    @Override
    public void setInitialsickDays(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        System.out.print("Enter number of sick days: ");
        int number = scanner.nextInt();

        Employee e = getEmployeeById(caller, empId);
        e.setSickDays(caller, number);
        System.out.println("Changed successfully.");
    }

    @Override
    public void setInitialdaysOff(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        System.out.print("Enter number of vacation days: ");
        int number = scanner.nextInt();

        Employee e = getEmployeeById(caller, empId);
        e.setDaysOff(caller, number);
        System.out.println("Changed successfully.");
    }

    @Override
    public Employee getEmployeeById(User caller, int empId) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        for (Employee e : employees) {
            if (e.getEmpId() == empId) return e;
        }

        throw new IllegalArgumentException("Employee not found");
    }

    @Override
    public void printEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();

        Employee e = getEmployeeById(caller, empId);
        System.out.println(e.toString(caller));
    }

    @Override
    public void printAllEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        for (Employee e : employees) {
            System.out.println(e.toString(caller));
        }
    }
}
