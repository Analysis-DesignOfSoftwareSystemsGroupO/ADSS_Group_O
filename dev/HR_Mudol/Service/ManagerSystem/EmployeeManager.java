package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * EmployeeManager class handles all operations related to employees within a branch,
 * such as adding, removing, updating employee details, and managing their roles.
 */
public class EmployeeManager implements IEmployeeManager {

    private Scanner scanner = new Scanner(System.in);
    private IRoleManager roleManager;
    private Branch curBranch;

    public EmployeeManager(Branch curBranch) {
        this.curBranch = curBranch;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public List<User> getAllUsers(User caller) {
        return this.curBranch.getUsers();
    }

    @Override
    public void addEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        String empName = getNonEmptyStringInput("Enter employee full name: ");

        int empID;
        do {
            empID = getIntInput("Enter employee ID (9 digits): ");
            if (String.valueOf(empID).length() != 9) {
                System.out.println("Invalid ID. Must be exactly 9 digits.");
            } else if (checkIfAlreadyExist(empID)) {
                System.out.println("This ID already exists in the system.");
            } else {
                break;
            }
        } while (true);

        String empPassword = getNonEmptyStringInput("Enter initial password for employee: ");
        String empBankAccount = getValidatedBankAccountInput("Enter bank account (digits only): ");
        int empSalary = getIntInput("Enter salary: ");
        LocalDate empStartDate = LocalDate.now();
        int minDay = getIntInput("Enter max day shifts in contract: ");
        int minEvening = getIntInput("Enter max evening shifts in contract: ");
        int sicks = getIntInput("Enter number of sick days: ");
        int daysOff = getIntInput("Enter number of vacation days: ");

        Employee employee = new Employee(empName, empID, empPassword, empBankAccount, empSalary,
                empStartDate, minDay, minEvening, sicks, daysOff);

        curBranch.getEmployees().add(employee);
        curBranch.getUsers().add(new User(employee, Level.regularEmp));

        System.out.println("Employee and user created successfully!");
    }

    private boolean checkIfAlreadyExist(int ID) {
        for (Employee emp : this.curBranch.getEmployees()) {
            if (emp.getEmpId() == ID) return true;
        }
        return false;
    }

    @Override
    public void removeEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID to remove: ");
        Employee toRemove = getEmployeeById(caller, empId);
        if (toRemove == null) {
            System.out.println("Employee not found.");
            return;
        }

        for (Role role : roleManager.getAllRoles(caller)) {
            roleManager.removeEmployeeFromRole(caller, role.getRoleNumber(), toRemove);
        }

        curBranch.getOldEmployee().add(toRemove);

        User userToRemove = null;
        for (User u : curBranch.getUsers()) {
            if (u.getUser().equals(toRemove)) {
                userToRemove = u;
                break;
            }
        }

        if (userToRemove != null) {
            curBranch.getUsers().remove(userToRemove);
        }

        curBranch.getEmployees().remove(toRemove);
        System.out.println("Employee removed successfully from system.");
    }

    @Override
    public void updateBankAccount(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        String newBankAccount = getValidatedBankAccountInput("Enter new bank account (digits only): ");
        e.setEmpBankAccount(caller, newBankAccount);
        System.out.println("Bank account updated successfully.");
    }

    @Override
    public void updateSalary(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int newSalary = getIntInput("Enter new salary: ");
        e.setEmpSalary(caller, newSalary);
        System.out.println("Salary updated successfully.");
    }

    @Override
    public void updateMinDayShift(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int newNumber = getIntInput("Enter new minimum day shifts: ");
        e.setMinDayShift(caller, newNumber);
        System.out.println("Minimum day shifts updated successfully.");
    }

    @Override
    public void updateMinEveningShift(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int newNumber = getIntInput("Enter new minimum evening shifts: ");
        e.setMinEveninigShift(caller, newNumber);
        System.out.println("Minimum evening shifts updated successfully.");
    }

    @Override
    public void setInitialsickDays(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int number = getIntInput("Enter number of sick days: ");
        e.setSickDays(caller, number);
        System.out.println("Sick days updated successfully.");
    }

    @Override
    public void setInitialdaysOff(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int number = getIntInput("Enter number of vacation days: ");
        e.setDaysOff(caller, number);
        System.out.println("Vacation days updated successfully.");
    }

    @Override
    public Employee getEmployeeById(User caller, int empId) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        if (String.valueOf(empId).length() != 9) {
            System.out.println("Employee ID must be exactly 9 digits.");
            return null;
        }

        for (Employee e : curBranch.getEmployees()) {
            if (e.getEmpId() == empId) return e;
        }

        return null;
    }

    @Override
    public void printEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = getEmployeeById(caller, empId);
        if (e != null) {
            System.out.println(e.toString());
        } else {
            System.out.println("Employee not found.");
        }
    }

    @Override
    public void printAllEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        for (Employee e : curBranch.getEmployees()) {
            System.out.println(e.toString());
        }
    }

    @Override
    public Branch getBranch() {
        return this.curBranch;
    }

    @Override
    public IRoleManager getRoleManager() {
        return this.roleManager;
    }

    /**
     * Helper method to safely read a positive integer input from the user.
     */
    private int getIntInput(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Please enter a positive number.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Helper method to safely read non-empty string input.
     */
    private String getNonEmptyStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input can't be empty.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Helper method to read and validate bank account input.
     */
    private String getValidatedBankAccountInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Bank account can't be empty.");
            }
        } while (input.isEmpty());
        return input;
    }

    public void setScanner(Scanner newScanner) {
        this.scanner = newScanner;
    }
}
