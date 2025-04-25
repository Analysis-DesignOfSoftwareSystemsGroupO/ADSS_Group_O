package HR_Mudol.domain;

public class FilledRole {

    private Employee employee;
    private Role role;

    public FilledRole(Employee employee, Role role) {
        this.employee = employee;
        this.role = role;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
