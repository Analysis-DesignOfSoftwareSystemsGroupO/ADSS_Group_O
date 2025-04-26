package HR_Mudol.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * The Role class represents a specific job role within the organization.
 * Each role has a unique identifier, a description, and a list of employees assigned to it.
 * Only managers are allowed to modify the role's description or manage the list of assigned employees.
 */
public class Role {

    // Static counter to generate unique role numbers
    static int RoleCounter = 0;

    // Unique identifier for the role
    private final int roleNumber;

    // Description of the role
    private String description;

    // List of employees assigned to this role
    private List<Employee> relevantEmployees;

    /**
     * Constructor for creating a new Role instance.
     * Should be used only by an HR Manager.
     * @param description The description of the role.
     */
    public Role(String description) {
        RoleCounter++;
        this.roleNumber = RoleCounter;
        this.description = description;
        this.relevantEmployees = new LinkedList<>();
    }

    /**
     * Returns the unique role number.
     * @return role number
     */
    public int getRoleNumber() {
        return roleNumber;
    }

    /**
     * Returns the description of the role.
     * @return role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of the role.
     * Only managers are allowed to perform this action.
     * @param caller The user attempting the update.
     * @param newDesc The new description for the role.
     * @throws SecurityException if the caller is not a manager.
     */
    public void SetDescription(User caller, String newDesc) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        description = newDesc;
    }

    /**
     * Returns the list of employees assigned to this role.
     * Only managers are allowed to view this information.
     * @param caller The user requesting the list.
     * @return list of relevant employees
     * @throws SecurityException if the caller is not a manager.
     */
    public List<Employee> getRelevantEmployees(User caller) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        return relevantEmployees;
    }

    /**
     * Adds a new employee to the list of employees assigned to this role.
     * Only managers are allowed to perform this action.
     * @param caller The user attempting to add an employee.
     * @param employee The employee to add.
     * @throws SecurityException if the caller is not a manager.
     */
    public void addNewEmployee(User caller, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantEmployees.addLast(employee);
    }

    /**
     * Removes an employee from the list of employees assigned to this role.
     * Only managers are allowed to perform this action.
     * @param caller The user attempting to remove an employee.
     * @param employee The employee to remove.
     * @throws SecurityException if the caller is not a manager.
     */
    public void removeEmployee(User caller, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantEmployees.remove(employee);
    }

    /**
     * Returns a string representation of the role,
     * including its description and the list of relevant employees (if any).
     * @return string representation of the role
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Role number " + this.getRoleNumber() + " - " + this.getDescription());

        if (relevantEmployees.isEmpty()) {
            string.append("\n  No employees assigned to this role.");
        } else {
            string.append("\n  Relevant employees:");
            for (Employee e : this.relevantEmployees) {
                string.append("\n    - ").append(e.getEmpName() + " - " + e.getEmpId());
            }
        }

        return string.toString();
    }

    /**
     * Compares this Role to another object for equality.
     * Two roles are considered equal if they have the same description.
     *
     * @param obj the object to compare with
     * @return true if the given object is a Role with the same description, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Comparing to itself
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class
        Role other = (Role) obj;
        return description.equals(other.description); // Compare descriptions
    }

}
