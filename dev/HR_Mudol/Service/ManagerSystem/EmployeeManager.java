package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManager implements IEmployeeManager {

    Scanner scanner = new Scanner(System.in);
    private IRoleManager roleManager; // Reference to RoleManager to access all roles
    private Branch curBranch;

    public EmployeeManager(Branch curBranch) {
        this.curBranch = curBranch;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public List<User> getAllUsers(User caller) {
        return this.curBranch.getUsers();
    }

    @Override
    public void addEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        String empName;
        do {
            System.out.print("Enter employee full name: ");
            empName = scanner.nextLine().trim();
            if (empName.isEmpty()) System.out.println("Name can't be empty.");
        } while (empName.isEmpty());

        int empID;
        String idString;
        do {
            empID = getIntInput("Enter employee ID: ");
            idString = String.valueOf(empID);
            if (idString.length()!=9) System.out.println("Invalid ID.");
            if (checkIfAlreadyExist(empID)) System.out.println("This ID is already exist in the system.");
        } while (idString.length()!=9||checkIfAlreadyExist(empID));

        String empPassword;
        do {
            System.out.print("Enter initial password for employee: ");
            empPassword = scanner.nextLine().trim();
            if (empPassword.isEmpty()) System.out.println("Password can't be empty.");
        } while (empPassword.isEmpty());

        String empBankAccount;
        do {
            System.out.print("Enter bank account: ");
            empBankAccount = scanner.nextLine().trim();
            if (empBankAccount.isEmpty()) System.out.println("Bank account can't be empty.");
        } while (empBankAccount.isEmpty());

        int empSalary = getIntInput("Enter salary: ");
        LocalDate empStartDate = LocalDate.now();
        int minDay = getIntInput("Enter minimum day shifts in contract: ");
        int minEvening = getIntInput("Enter minimum evening shifts in contract: ");
        int sicks = getIntInput("Enter number of sick days: ");
        int daysOff = getIntInput("Enter number of vacation days: ");

        Employee employee = new Employee(
                empName, empID, empPassword, empBankAccount, empSalary,
                empStartDate, minDay, minEvening, sicks, daysOff
        );
        curBranch.getEmployees().add(employee);

        User newUser = new User(employee, Level.regularEmp);
        curBranch.getUsers().add(newUser);

        System.out.println("Employee and user created successfully!");
    }

    private boolean checkIfAlreadyExist(int ID){
        for (Employee emp: this.curBranch.getEmployees()){
            if (emp.getEmpId()==ID){
                return true;
            }
        }
        return false;
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

        curBranch.getUsers().remove(toRemove);
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

        for (Employee e : curBranch.getEmployees()) {
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
        System.out.println(e.toString());
    }

    @Override
    public void printAllEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        for (Employee e : curBranch.getEmployees()) {
            System.out.println(e.toString());
        }
    }

    private int getIntInput(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);

            String input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Please enter a positive number.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return value;
    }
}
