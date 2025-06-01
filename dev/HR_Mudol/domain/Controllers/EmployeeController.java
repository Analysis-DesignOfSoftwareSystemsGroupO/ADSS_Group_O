package HR_Mudol.domain.Controllers;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;


/**
 * EmployeeManager class handles all operations related to employees within a branch,
 * such as adding, removing, updating employee details, and managing their roles.
 */
public class EmployeeController implements IEmployeeController {

    private Scanner scanner = new Scanner(System.in);
    private IRoleController roleManager;
    private Branch curBranch;
    private DTOToDomainMapper mapper;

    public EmployeeController(BranchDTO Branch) throws SQLException {

        this.curBranch = DTOToDomainMapper.fromDTO(Branch);
        DTOToDomainMapper.initialize(
                curBranch.getUserRepo(),
                curBranch.getEmployeeRepo(),
                curBranch.getRoleRepo(),
                curBranch.getWeekRepo()
        );
        //this.mapper = new DTOToDomainMapper(curBranch.getUserRepo(), curBranch.getEmployeeRepo(), curBranch.getRoleRepo(), curBranch.getWeekRepo());
    }
    @Override
    public void close() {
        try {
            curBranch.close();
        } catch (Exception e) {
            System.out.println("❌ Failed to close branch resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setRoleManager(IRoleController roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public Branch getBranch() {
        return this.curBranch;
    }

    @Override
    public void addEmployee(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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

        Employee emp = new Employee(empName, empID, empPassword, empBankAccount,
                empSalary, empStartDate, minDay, minEvening, sicks, daysOff);

        //add him
        curBranch.getEmployeeRepo().addFromDTO(emp);

        //create his User with employee reference
        curBranch.getUserRepo().add(new User(emp, Level.regularEmp));

        System.out.println("Employee and user created successfully!");
    }

    @Override
    public void removeEmployee(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied");

        int empId = getIntInput("Enter employee ID to remove: ");
        if (!curBranch.getEmployeeRepo().exists(empId)) {
            System.out.println("Employee not found.");
            return;
        }

        Employee toRemove = curBranch.getEmployeeRepo().getById(empId);

        for (Role role : roleManager.getAllRoles(theCaller)) {
            roleManager.removeEmployeeFromRole(theCaller, role.getRoleNumber(), mapper.toDTO(toRemove));
        }

        User user = curBranch.getUserRepo().getByEmployeeId(empId);
        if (user != null) {
            curBranch.getUserRepo().remove(user); // remove from DB
        }

        curBranch.getEmployeeRepo().archive(empId);

        System.out.println("Employee removed successfully from system.");
    }

    @Override
    public void updateBankAccount(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public void updateSalary(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public void updateMinDayShift(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public void updateMinEveningShift(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public void setInitialsickDays(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public void setInitialdaysOff(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public EmployeeDTO getEmployeeById(UserDTO theCaller, int empId) throws SQLException {
        User caller = mapper.fromDTO(theCaller);

        if (String.valueOf(empId).length() != 9) {
            System.out.println("Employee ID must be exactly 9 digits.");
            return null;
        }

        Employee e = curBranch.getEmployeeRepo().getById(empId);
        if (e == null) {
            System.out.println("Employee not found.");
        }
        return DTOToDomainMapper.toDTO(e);
    }

    @Override
    public void printEmployees(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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
    public void printAllEmployees(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
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

    @Override
    public boolean verifyPassword(UserDTO caller, int empId, String password) {
        Employee employee = curBranch.getEmployeeRepo().getById(empId);

        // אם העובד לא קיים – החזרה של שגיאה/false
        if (employee == null) {
            System.out.println("Employee not found.");
            return false;
        }

        // בדיקה של הרשאות – רק העובד עצמו יכול לאמת את הסיסמה שלו
        if (caller.getUserId() != empId) {
            System.out.println("Access denied.");
            return false;
        }

        // בדיקת סיסמה – נניח שאין hash
        return employee.getEmpPassword().equals(password);
    }

    @Override
    public void updatePassword(UserDTO callerDTO, int empId, String newPassword) throws SQLException {
        // שליפת האובייקט Employee
        Employee employee = curBranch.getEmployeeRepo().getById(empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // שליפת אובייקט User מה־DTO
        User caller = curBranch.getUserRepo().getByEmployeeId(callerDTO.getUserId());
        if (caller == null || !caller.getUser().equals(employee)) {
            throw new SecurityException("Access denied: You can only update your own password.");
        }

        // בדיקה שהסיסמה החדשה אינה ריקה
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        curBranch.getEmployeeRepo().updatePassword(empId, newPassword);

    }

    @Override
    public List<ConstraintDTO> getConstraintsByEmployeeId(int employeeId) {
        Employee emp = curBranch.getEmployeeRepo().getById(employeeId);
        if (emp == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        List<ConstraintDTO> result = new ArrayList<>();
        List<Constraint> rawConstraints = emp.getWeeklyConstraints();

        for (Constraint c : rawConstraints) {
            ConstraintDTO dto = DTOToDomainMapper.toDTO(c, employeeId);
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<RoleDTO> getRolesForEmployee(int employeeId) {
        Employee emp = curBranch.getEmployeeRepo().getById(employeeId);
        if (emp == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (Role role : emp.getRelevantRoles()) {
            roleDTOs.add(DTOToDomainMapper.toDTO(role));
        }

        return roleDTOs;
    }

    @Override
    public void lockWeeklyConstraints(int empId) {
        Employee emp = curBranch.getEmployeeRepo().getById(empId);
        if (emp == null) throw new IllegalArgumentException("Employee not found");
        emp.lockWeeklyConstraints();
    }

    @Override
    public int getMinDayShifts(int empId) {
        Employee emp = curBranch.getEmployeeRepo().getById(empId);
        if (emp == null) throw new IllegalArgumentException("Employee not found");
        return emp.getContract().getMinDayShift(emp);
    }

    @Override
    public int getMinEveningShifts(int empId) {
        Employee emp = curBranch.getEmployeeRepo().getById(empId);
        if (emp == null) throw new IllegalArgumentException("Employee not found");
        return emp.getContract().getMinEveninigShift(emp);
    }

    @Override
    public void submitConstraint(int empId, ConstraintDTO constraintDTO) throws SQLException {
        Employee employee = curBranch.getEmployeeRepo().getById(empId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + empId);
        }

        // המרת DTO לאובייקט דומיין
        Constraint constraint = DTOToDomainMapper.fromDTO(constraintDTO);

        // שמירה ברמת העובד
        employee.addNewConstraints(constraint);
        if (constraint.getType() == ShiftType.MORNING) {
            employee.addNewMorningConstraints(constraint);
        } else {
            employee.addNewEveningConstraints(constraint);
        }

        // שמירה ברמת הריפוזיטורי הכללי
        curBranch.getConstraintRepo().save(empId, constraint);
    }

    @Override
    public EmploymentContractDTO getContractDetails(UserDTO caller, int empId) {
        Employee employee = curBranch.getEmployeeRepo().getById(empId);
        if (employee == null) return null;

        // בדיקת הרשאות – רק HR או העובד עצמו
        if (!caller.getLevel().equals("HR_MANAGER") && caller.getUserId() != empId) {
            throw new SecurityException("Access denied: Only HR or the employee may view the contract.");
        }

        EmploymentContract contract = employee.getContract();
        return DTOToDomainMapper.toDTO(contract,employee);
    }

    @Override
    public List<ConstraintDTO> getConstraintsByType(int empId, ShiftType type) {
        List<Constraint> all = curBranch.getEmployeeRepo()
                .getById(empId)
                .getWeeklyConstraints();

        List<ConstraintDTO> result = new ArrayList<>();
        for (Constraint c : all) {
            if (c.getType() == type) {
                ConstraintDTO dto = DTOToDomainMapper.toDTO(c, empId);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public void updateConstraintExplanation(EmployeeDTO emp, ConstraintDTO constraintDTO, String newExplanation) {
        Constraint constraint = curBranch.getConstraintRepo()
                .getConstraint(emp.getEmployeeId(), WeekDay.valueOf(constraintDTO.getDay().toUpperCase()), ShiftType.valueOf(constraintDTO.getType().toUpperCase()));

        if (constraint != null) {
            constraint.setExplanation(DTOToDomainMapper.fromDTO(emp), newExplanation);

            curBranch.getConstraintRepo().update(emp.getEmployeeId(), constraint);
        }
    }

    @Override
    public void removeConstraint(int empId, ConstraintDTO constraintDTO) throws SQLException {
        WeekDay day = WeekDay.valueOf(constraintDTO.getDay().toUpperCase());
        ShiftType type = ShiftType.valueOf(constraintDTO.getType().toUpperCase());

        curBranch.getConstraintRepo().delete(empId, day, type);
    }

    @Override
    public int getEmployeeCount() {
        return curBranch.getEmployeeRepo().size();
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() throws SQLException {
        List<EmployeeDTO> dtos = new ArrayList<>();
        for (Employee e : curBranch.getEmployeeRepo().getAll()) {
            dtos.add(DTOToDomainMapper.toDTO(e));
        }
        return dtos;
    }

    @Override
    public String getUserLevel(int employeeId) throws SQLException {
        return curBranch.getUserRepo().getLevelById(employeeId).name();
    }

    @Override
    public boolean isEmployeeInBranch(int empId, int branchId) throws SQLException{
        return curBranch.getEmployeeRepo().isEmployeeInBranch(empId,branchId);
    }

    @Override
    public UserDTO getUserById(int empId) throws SQLException {
        User user= curBranch.getUserRepo().getByEmployeeId(empId);
        return DTOToDomainMapper.toDTO(user);
    }


}





