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
     * Constructs an empty Branch with initialized empty lists.
     */
    public Branch() {
        this.branchID = counter++;
        this.weeks = new LinkedList<>();
        this.employees = new LinkedList<>();
        this.roles = new LinkedList<>();
        this.users = new LinkedList<>();
        this.oldEmployee = new LinkedList<>();

        // Initialize with one current week
        this.weeks.add(new Week());
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
