package HR_Mudol.domain.Controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.User;

/**
 * EmployeeManager class handles all operations related to employees within a branch,
 * such as adding, removing, updating employee details, and managing their roles.
 */
public class EmployeeController implements IEmployeeController {

    private Scanner scanner = new Scanner(System.in);
    private IRoleController roleManager;
    private Branch curBranch;

    public EmployeeController(Branch curBranch) {
        this.curBranch = curBranch;
    }

    public void setRoleManager(IRoleController roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public Branch getBranch() {
        return this.curBranch;
    }

    @Override
    public void addEmployee(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        String empName = getNonEmptyStringInput("Enter employee full name: ");

        int empID;
        do {
            empID = getIntInput("Enter employee ID (9 digits): ");
            if (String.valueOf(empID).length() != 9) {
                System.out.println("Invalid ID. Must be exactly 9 digits.");
            } else if (curBranch.getEmployeeRepo().exists(empID)) {
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

        EmployeeDTO dto = new EmployeeDTO(empID, empName, empPassword, empBankAccount,
                empSalary, empStartDate, minDay, minEvening, sicks, daysOff);

        //add him
        Employee created = curBranch.getEmployeeRepo().addFromDTO(dto);

        //create his User with employee reference
        curBranch.getUserRepo().add(new User(created, Level.regularEmp));

        System.out.println("Employee and user created successfully!");
    }

    @Override
    public void removeEmployee(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID to remove: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        Employee toRemove = curBranch.getEmployeeRepo().getById(empId);

        for (Role role : roleManager.getAllRoles(caller)) {
            roleManager.removeEmployeeFromRole(caller, role.getRoleNumber(), toRemove);
        }

        User user = curBranch.getUserRepo().getByEmployeeId(empId);
        if (user != null) {
            curBranch.getUserRepo().remove(user); // remove from DB
        }

        curBranch.getEmployeeRepo().archive(empId);

        System.out.println("Employee removed successfully from system.");
    }

    @Override
    public void updateBankAccount(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        String newBankAccount = getValidatedBankAccountInput("Enter new bank account (digits only): ");
        curBranch.getEmployeeRepo().updateBankAccount(caller, empId, newBankAccount);

        System.out.println("Bank account updated successfully.");
    }

    @Override
    public void updateSalary(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int newSalary = getIntInput("Enter new salary: ");
        curBranch.getEmployeeRepo().updateSalary(caller, empId, newSalary);

        System.out.println("Salary updated successfully.");
    }

    @Override
    public void updateMinDayShift(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int newNumber = getIntInput("Enter new minimum day shifts: ");
        curBranch.getEmployeeRepo().updateMinDayShift(caller, empId, newNumber);

        System.out.println("Minimum day shifts updated successfully.");
    }

    @Override
    public void updateMinEveningShift(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int newNumber = getIntInput("Enter new minimum evening shifts: ");
        curBranch.getEmployeeRepo().updateMinEveningShift(caller, empId, newNumber);

        System.out.println("Minimum evening shifts updated successfully.");
    }


    @Override
    public void setInitialsickDays(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int number = getIntInput("Enter number of sick days: ");
        curBranch.getEmployeeRepo().updateSickDays(caller, empId, number);

        System.out.println("Sick days updated successfully.");
    }

    @Override
    public void setInitialdaysOff(User caller) throws SQLException {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = curBranch.getEmployeeRepo().getById(empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int number = getIntInput("Enter number of vacation days: ");
        e.setDaysOff(caller, number);

        curBranch.getEmployeeRepo().updateDaysOff(empId, number);

        System.out.println("Vacation days updated successfully.");
    }


    @Override
    public Employee getEmployeeById(User caller, int empId) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        if (String.valueOf(empId).length() != 9) {
            System.out.println("Employee ID must be exactly 9 digits.");
            return null;
        }

        Employee e = curBranch.getEmployeeRepo().getById(empId);
        if (e == null) {
            System.out.println("Employee not found.");
        }
        return e;
    }

    @Override
    public void printEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (String.valueOf(empId).length() != 9) {
            System.out.println("Employee ID must be exactly 9 digits.");
            return;
        }

        Employee e = curBranch.getEmployeeRepo().getById(empId);
        if (e != null) {
            System.out.println(e);
        } else {
            System.out.println("Employee not found.");
        }
    }

    @Override
    public void printAllEmployees(User caller) {
        if (!caller.isManager()) throw new SecurityException("Access denied");

        List<Employee> allEmployees = curBranch.getEmployeeRepo().getAll();
        if (allEmployees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        for (Employee e : allEmployees) {
            System.out.println(e);
        }
    }

    //helpers
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










}



