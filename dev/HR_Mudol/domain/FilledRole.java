package HR_Mudol.domain;

/**
 * Represents a role that is filled by a specific employee.
 */
public class FilledRole {

    // The employee assigned to the role
    private Employee employee;

    // The role assigned to the employee
    private Role role;

    /**
     * Constructs a FilledRole with the specified employee and role.
     */
    public FilledRole(Employee employee, Role role) {
        this.employee = employee;
        this.role = role;
    }

    /**
     * Returns the employee assigned to this role.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Updates the employee assigned to this role.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Returns the role assigned to the employee.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Updates the role assigned to the employee.
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
