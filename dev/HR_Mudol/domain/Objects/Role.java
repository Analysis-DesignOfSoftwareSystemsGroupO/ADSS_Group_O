package HR_Mudol.domain.Objects;

import java.util.LinkedList;
import java.util.List;

/**
 * The Role class represents a specific job role within the organization.
 * Each role has a unique identifier, a description, and a list of employees assigned to it.
 * Only managers are allowed to modify the role's description or manage the list of assigned employees.
 */
public class Role {


    // Unique identifier for the role
    private  int roleNumber;

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
        this.description = description;
        this.relevantEmployees = new LinkedList<>();
    }

    public Role(int roleNumber, String description) {
        this.roleNumber = roleNumber;
        this.description = description;
        this.relevantEmployees = new LinkedList<>();
    }

    public Role(int roleNumber, String description,List<Employee> relevantEmployees) {
        this.roleNumber = roleNumber;
        this.description = description;
        this.relevantEmployees = relevantEmployees;
    }



    /**
     * Returns the unique role number.
     * @return role number
     */
    public int getRoleNumber() {
        return  this.roleNumber;
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
     * @param newDesc The new description for the role.
     */
    public void setDescription(String newDesc) {
        description = newDesc;
    }

    /**
     * Returns the list of employees assigned to this role.
     * Only managers are allowed to view this information.
     * @return list of relevant employees
     */
    public List<Employee> getRelevantEmployees() {

        return relevantEmployees;
    }

    /**
     * Adds a new employee to the list of employees assigned to this role.
     * Only managers are allowed to perform this action.
     * @param employee The employee to add.

     */
    public void addNewEmployee(Employee employee) {

        this.relevantEmployees.addLast(employee);
    }

    /**
     * Removes an employee from the list of employees assigned to this role.
     * Only managers are allowed to perform this action.
     * @param employee The employee to remove.

     */
    public void removeEmployee(Employee employee) {

        this.relevantEmployees.remove(employee);
    }

    /**
     * Returns a string representation of the role,
     * including its description and the list of relevant employees (if any).
     * @return string representation of the role
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Role" + " - " +this.getDescription());

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
