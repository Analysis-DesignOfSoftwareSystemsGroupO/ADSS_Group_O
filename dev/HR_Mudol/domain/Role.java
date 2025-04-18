package HR_Mudol.domain;
import java.util.LinkedList;
import java.util.List;

public class Role {
    static int RoleCounter = 0;
    private final int roleNumber;
    private String description;
    private List<Employee> relevantEmployees;

    //create new role only by HR manager
    public Role(User caller, String description) {

        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }

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
        return "Role" + this.getRoleNumber() + " " + this.getDescription()+
                "\n  RelevantEmployees: " + this.relevantEmployees.toString();

    }
}
