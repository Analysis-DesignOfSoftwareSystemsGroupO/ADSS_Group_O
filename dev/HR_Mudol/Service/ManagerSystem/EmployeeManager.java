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

    private Scanner scanner = new Scanner(System.in); // Scanner for input operations
    private IRoleManager roleManager; // Role manager interface
    private Branch curBranch; // Current branch being managed

    /**
     * Constructor to initialize EmployeeManager with a specific branch.
     * @param curBranch the branch this manager operates on
     */
    public EmployeeManager(Branch curBranch) {
        this.curBranch = curBranch;
    }

    /**
     * Sets the role manager dependency.
     * @param roleManager the role manager to set
     */
    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

   /**
           * Returns all users in the current branch.
     * @param caller the user requesting the operation
     * @return list of all users
     */
    public List<User> getAllUsers(User caller) {
        return this.curBranch.getUsers();
    }

    /**
     * Adds a new employee to the branch and creates an associated user account.
     */
    @Override
    public void addEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        // Input and validation for employee name
        String empName;
        do {
            System.out.print("Enter employee full name: ");
            empName = scanner.nextLine().trim();
            if (empName.isEmpty()) System.out.println("Name can't be empty.");
        } while (empName.isEmpty());

        // Input and validation for employee ID
        int empID;
        String idString;
        do {
            empID = getIntInput("Enter employee ID: ");
            idString = String.valueOf(empID);
            if (idString.length() != 9) System.out.println("Invalid ID."); // Validate ID length
            if (checkIfAlreadyExist(empID)) System.out.println("This ID is already exist in the system."); // Check if ID already exists
        } while (idString.length() != 9 || checkIfAlreadyExist(empID));

        // Input and validation for password
        String empPassword;
        do {
            System.out.print("Enter initial password for employee: ");
            empPassword = scanner.nextLine().trim();
            if (empPassword.isEmpty()) System.out.println("Password can't be empty.");
        } while (empPassword.isEmpty());

        // Input and validation for bank account
        String empBankAccount;
        do {
            System.out.print("Enter bank account: ");
            empBankAccount = scanner.nextLine().trim();
            if (empBankAccount.isEmpty()) System.out.println("Bank account can't be empty.");
        } while (empBankAccount.isEmpty());

        int empSalary = getIntInput("Enter salary: ");
        LocalDate empStartDate = LocalDate.now();
        int minDay = getIntInput("Enter max day shifts in contract: ");
        int minEvening = getIntInput("Enter max evening shifts in contract: ");
        int sicks = getIntInput("Enter number of sick days: ");
        int daysOff = getIntInput("Enter number of vacation days: ");

        // Create and add new employee
        Employee employee = new Employee(
                empName, empID, empPassword, empBankAccount, empSalary,
                empStartDate, minDay, minEvening, sicks, daysOff
        );
        curBranch.getEmployees().add(employee);
        curBranch.getUsers().add(new User(employee, Level.regularEmp));

        System.out.println("Employee and user created successfully!");
    }


    /**
     * Helper method to check if an employee ID already exists in the branch.
     * @param ID the employee ID to check
     * @return true if the ID exists, false otherwise
     */
    private boolean checkIfAlreadyExist(int ID) {
        for (Employee emp : this.curBranch.getEmployees()) {
            if (emp.getEmpId() == ID) return true;
        }
        return false;
    }

    /** Removes an employee and the corresponding user from the branch.
    */
    @Override
    public void removeEmployee(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID to remove: ");
        int empId = scanner.nextInt();
        scanner.nextLine(); // Clean input buffer

        Employee toRemove = getEmployeeById(caller, empId);
        if (toRemove == null) {
            System.out.println("Employee not found.");
            return;
        }

        // Remove employee from all roles
        for (Role role : roleManager.getAllRoles(caller)) {
            roleManager.removeEmployeeFromRole(caller, role.getRoleNumber(), toRemove);
        }

        curBranch.getOldEmployee().add(toRemove); // Move to old employees

        // Remove associated user
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

    /**  Updates the bank account information of an employee.
     */
    @Override
    public void updateBankAccount(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        if (e == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + empId);
        }

        System.out.print("Enter new bank account: ");
        String newBankAccount = scanner.nextLine();

        e.setEmpBankAccount(caller, newBankAccount);
        System.out.println("Changed successfully.");
    }

    /** Updates the salary of an employee.
     */
    @Override
    public void updateSalary(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new salary: ");
        int newSalary = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        e.setEmpSalary(caller, newSalary);

        System.out.println("Changed successfully.");
    }

    /** Updates the minimum required day shifts of an employee.
     */
    @Override
    public void updateMinDayShift(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new minimum day shifts: ");
        int newNumber = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        e.setMinDayShift(caller, newNumber);
        System.out.println("Changed successfully.");
    }

    /** Updates the minimum required evening shifts of an employee.
     */
    @Override
    public void updateMinEveningShift(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new minimum evening shifts: ");
        int newNumber = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        e.setMinEveninigShift(caller, newNumber);
        System.out.println("Changed successfully.");
    }

    /** Sets the initial number of sick days for an employee.
     */
    @Override
    public void setInitialsickDays(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter number of sick days: ");
        int number = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        e.setSickDays(caller, number);
        System.out.println("Changed successfully.");
    }

    /** Sets the initial number of vacation days for an employee.
     */
    @Override
    public void setInitialdaysOff(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter number of vacation days: ");
        int number = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        e.setDaysOff(caller, number);
        System.out.println("Changed successfully.");
    }

    /** Retrieves an employee by their ID.
     */
    @Override
    public Employee getEmployeeById(User caller, int empId) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        if (String.valueOf(empId).length() != 9) {
            throw new IllegalArgumentException("Employee ID must be exactly 9 digits.");
        }

        for (Employee e : curBranch.getEmployees()) {
            if (e.getEmpId() == empId) return e;
        }

        return null; // If employee not found
    }

    /** Prints the details of a specific employee.
     */
    @Override
    public void printEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        System.out.print("Enter employee ID: ");
        int empId = scanner.nextInt();
        scanner.nextLine();

        Employee e = getEmployeeById(caller, empId);
        System.out.println(e.toString());
    }

    /**Prints the details of all employees in the branch.
     */
    @Override
    public void printAllEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        // Print all employees in the branch
        for (Employee e : curBranch.getEmployees()) {
            System.out.println(e.toString());
        }
    }


    /** Getter for the current branch
     */
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
     * @param prompt the prompt message to display
     * @return the integer value input by the user
     */
    // Helper method to safely read positive integers
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

    /**
     * Allows setting a custom Scanner instance (useful for testing).
     * @param newScanner the scanner to set
     */
    public void setScanner(Scanner newScanner) {
        this.scanner = newScanner;
    }
}
