package HR_Mudol.domain;
import java.util.LinkedList;
import java.util.List;

public class Role {
    static int RoleCounter = 0;
    private final int roleNumber;
    private String description;
    private List<Employee> relevantEmployees;

    //create new role only by HR manager
    public Role(String description) {

        RoleCounter++;
        this.roleNumber = RoleCounter;
        this.description = description;
        this.relevantEmployees = new LinkedList<>();
    }

    public int getRoleNumber() {
        return roleNumber;
    }

    public String getDescription() {
        return description;
    }

    public void SetDescription(User caller, String newDesc) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        description = newDesc;
    }

    //for managers only
    public List<Employee> getRelevantEmployees(User caller) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        return relevantEmployees;
    }

    public void addNewEmployee(User caller, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantEmployees.addLast(employee);
    }

    public void removeEmployee(User caller, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantEmployees.remove(employee);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Role number " + this.getRoleNumber() + " - " + this.getDescription());

        if (relevantEmployees.isEmpty()) {
            string.append("\n  No employees assigned to this role.");
        } else {
            string.append("\n  Relevant employees:");
            for (Employee e : this.relevantEmployees) {
                string.append("\n    - ").append(e.getEmpName() + " - "+ e.getEmpId());
            }
        }

        return string.toString();

    }
}
