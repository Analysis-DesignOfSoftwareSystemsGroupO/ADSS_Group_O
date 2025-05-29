package HR_Mudol.domain.Controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.*;


/**
 * EmployeeManager class handles all operations related to employees within a branch,
 * such as adding, removing, updating employee details, and managing their roles.
 */
public class EmployeeController implements IEmployeeController {

    private Scanner scanner = new Scanner(System.in);
    private IRoleController roleManager;
    private Branch curBranch;
    private DTOToDomainMapper mapper;

    public EmployeeController(Branch curBranch) {
        this.curBranch = curBranch;
        this.mapper=new DTOToDomainMapper(curBranch.getUserRepo(),curBranch.getEmployeeRepo(),curBranch.getRoleRepo());
    }

    public void setRoleManager(IRoleController roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public Branch getBranch() {
        return this.curBranch;
    }

    @Override
    public void addEmployee(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

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
    public void removeEmployee(UserDTO caller) throws SQLException {
        if (!DTOToDomainMapper.fromDTO(caller,curBranch.getUserRepo()).isManager()) throw new SecurityException("Access denied");

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
    public void updateBankAccount(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        String newBankAccount = getValidatedBankAccountInput("Enter new bank account (digits only): ");
        curBranch.getEmployeeRepo().updateBankAccount(UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()), empId, newBankAccount);

        System.out.println("Bank account updated successfully.");
    }

    @Override
    public void updateSalary(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int newSalary = getIntInput("Enter new salary: ");
        curBranch.getEmployeeRepo().updateSalary(UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()), empId, newSalary);

        System.out.println("Salary updated successfully.");
    }

    @Override
    public void updateMinDayShift(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int newNumber = getIntInput("Enter new minimum day shifts: ");
        curBranch.getEmployeeRepo().updateMinDayShift(UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()), empId, newNumber);

        System.out.println("Minimum day shifts updated successfully.");
    }

    @Override
    public void updateMinEveningShift(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int newNumber = getIntInput("Enter new minimum evening shifts: ");
        curBranch.getEmployeeRepo().updateMinEveningShift(UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()), empId, newNumber);

        System.out.println("Minimum evening shifts updated successfully.");
    }


    @Override
    public void setInitialsickDays(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        int number = getIntInput("Enter number of sick days: ");
        curBranch.getEmployeeRepo().updateSickDays(UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()), empId, number);

        System.out.println("Sick days updated successfully.");
    }

    @Override
    public void setInitialdaysOff(UserDTO caller) throws SQLException {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID: ");
        Employee e = curBranch.getEmployeeRepo().getById(empId);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }

        int number = getIntInput("Enter number of vacation days: ");
        e.setDaysOff(UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()), number);

        curBranch.getEmployeeRepo().updateDaysOff(empId, number);

        System.out.println("Vacation days updated successfully.");
    }


    @Override
    public Employee getEmployeeById(UserDTO caller, int empId) {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

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
    public void printEmployees(UserDTO caller) {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

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
    public void printAllEmployees(UserDTO caller) {
        if (!UserMapper.fromDTO(caller,curBranch.getEmployeeRepo()).isManager()) throw new SecurityException("Access denied");

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



