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
        this.branchID = counter;
        Week curWeek = new Week();
        this.weeks.add(curWeek);

        // Initialize employee and user lists
        this.employees = new LinkedList<>();
        this.users = new LinkedList<>();

        // --- Add HR Manager ---
        HRManager hrManager = new HRManager("Rami Levi", 111111111, "admin", "122345", 300000, LocalDate.now(), 2, 2, 5, 10);
        User hrUser = new User(hrManager, Level.HRManager);
        this.employees.add(hrManager);
        this.users.add(hrUser);

        // --- Add Regular Employee ---
        Employee regularEmp = new Employee("Dana", 123456789, "pass", "123456", 5000, LocalDate.now(), 2, 2, 5, 10);
        User regularUser = new User(regularEmp, Level.regularEmp);
        this.employees.add(regularEmp);
        this.users.add(regularUser);

        // --- Add Shift Manager ---
        Employee shiftManager = new Employee("Yossi Cohen", 222222222, "shiftadmin", "999999", 7000, LocalDate.now(), 2, 2, 5, 10);
        User shiftUser = new User(shiftManager, Level.shiftManager);
        this.employees.add(shiftManager);
        this.users.add(shiftUser);

        // --- Add 15 Additional Regular Employees ---
        for (int i = 0; i < 15; i++) {
            int empId = 200000000 + i;
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
