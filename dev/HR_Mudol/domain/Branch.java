package HR_Mudol.domain;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Branch in the company, managing employees, users, weeks, and roles.
 */
public class Branch {

    // Static counter for tracking number of created branches
    static int counter = 0;

    // Branch ID assigned at creation
    private int branchID;

    // List of weeks for scheduling work
    private List<Week> weeks = new LinkedList<>();

    // List of active employees
    private List<Employee> employees;

    // List of roles within the branch
    private List<Role> roles = new LinkedList<>();

    // List of users (employees with system access)
    private List<User> users;

    // List of former employees who left the company
    private List<Employee> oldEmployee = new LinkedList<>();

    /**
     * Constructs a new Branch with initial setup:
     * - Creates HR manager, shift manager, and regular employees
     * - Initializes one current week
     * - Initializes lists for employees, users, and roles
     */
    public Branch() {
        this.branchID = counter++;
        Week curWeek = new Week();
        this.weeks.add(curWeek);

        // Initialize lists
        this.employees = new LinkedList<>();
        this.users = new LinkedList<>();

        // --- HR Manager ---
        HRManager hrManager = new HRManager("Rami Levi", 111111111, "admin", "122345", 300000, LocalDate.now(), 2, 2, 5, 10);
        User hrUser = new User(hrManager, Level.HRManager);
        this.employees.add(hrManager);
        this.users.add(hrUser);

        // --- Shift Manager ---
        Employee shiftManager = new Employee("Yossi Cohen", 222222222, "shiftadmin", "999999", 7000, LocalDate.now(), 2, 2, 5, 10);
        User shiftUser = new User(shiftManager, Level.shiftManager);
        this.employees.add(shiftManager);
        this.users.add(shiftUser);

        // --- Create predefined Roles ---
        Role driverRole = new Role("Driver");
        Role cashierRole = new Role("Cashier");
        Role warehouseRole = new Role("Warehouse Worker");
        Role shiftSupervisorRole = new Role("Shift Supervisor");
        Role courierRole = new Role("Courier");
        Role stockOrganizerRole = new Role("Stock Organizer");
        Role cookRole = new Role("Cook");
        Role securityGuardRole = new Role("Security Guard");

        // Add roles to branch
        roles.add(driverRole);
        roles.add(cashierRole);
        roles.add(warehouseRole);
        roles.add(shiftSupervisorRole);
        roles.add(courierRole);
        roles.add(stockOrganizerRole);
        roles.add(cookRole);
        roles.add(securityGuardRole);

        // --- Add Employees for Each Role ---

        addEmployeeWithRole("Avi Driver", 333333333, "driverpass", "BANK1", 4500, driverRole, hrUser);
        addEmployeeWithRole("Tamar Cashier", 444444444, "cashierpass", "BANK2", 4300, cashierRole, hrUser);
        addEmployeeWithRole("David Warehouse", 555555555, "warehousepass", "BANK3", 4200, warehouseRole, hrUser);
        addEmployeeWithRole("Shiran Supervisor", 666666666, "supervisorpass", "BANK4", 5200, shiftSupervisorRole, hrUser);
        addEmployeeWithRole("Oren Courier", 777777777, "courierpass", "BANK5", 4100, courierRole, hrUser);
        addEmployeeWithRole("Noa Organizer", 888888888, "organizerpass", "BANK6", 4000, stockOrganizerRole, hrUser);
        addEmployeeWithRole("Ron Cook", 999999999, "cookpass", "BANK7", 4300, cookRole, hrUser);
        addEmployeeWithRole("Eyal Security", 123123123, "securitypass", "BANK8", 4400, securityGuardRole, hrUser);

        // --- Add 8 Additional Regular Employees ---
        for (int i = 0; i < 8; i++) {
            int empId = 600000000 + i;
            String name = "Employee" + (i + 1);
            String password = "pass" + (i + 1);
            String bankAccount = "BANK" + (i + 1);
            int salary = 4000 + (i * 100);

            Employee emp = new Employee(name, empId, password, bankAccount, salary, LocalDate.now(), 2, 2, 5, 10);
            User user = new User(emp, Level.regularEmp);

            this.employees.add(emp);
            this.users.add(user);
        }
    }

    /**
     * Helper method to add an employee with a role.
     */
    private void addEmployeeWithRole(String name, int id, String password, String bankAccount, int salary, Role role, User hrUser) {
        Employee emp = new Employee(name, id, password, bankAccount, salary, LocalDate.now(), 2, 2, 5, 10);
        User user = new User(emp, Level.regularEmp);

        this.employees.add(emp);
        this.users.add(user);
        role.addNewEmployee(hrUser, emp);
    }


    /**
     * Returns the list of users (employees with system accounts).
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Returns the list of former employees who have left the branch.
     */
    public List<Employee> getOldEmployee() {
        return oldEmployee;
    }

    /**
     * Returns the list of roles available in the branch.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Returns the list of currently active employees in the branch.
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * Returns the list of weeks associated with the branch.
     */
    public List<Week> getWeeks() {
        return weeks;
    }

    /**
     * Returns the branch ID.
     */
    public int getBranchID() {
        return branchID;
    }
}
