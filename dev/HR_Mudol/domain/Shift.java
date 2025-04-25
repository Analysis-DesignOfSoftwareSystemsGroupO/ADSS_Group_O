package HR_Mudol.domain;

import java.util.LinkedList;
import java.util.List;

public class Shift {

    static int counter = 0; // Counter

    private int shiftID;
    private WeekDay day;
    private ShiftType type;
    private Status status;
    private List<Employee> employees; // The employees who work at that shift
    private List<Role> necessaryRoles;
    private Employee shiftManager;
    private List<FilledRole> filledRoles; //all the roles that already were assigned with emp

    public Shift(WeekDay day, ShiftType type) {
        counter++;
        this.shiftID = counter;
        this.day = day;
        this.type = type;
        this.employees = new LinkedList<>();
        this.status = Status.Empty;
        this.necessaryRoles = new LinkedList<>();
        this.filledRoles=new LinkedList<>();
    }

    // Getters
    public int getShiftID() {
        return shiftID;
    }

    public WeekDay getDay() {
        return day;
    }

    public ShiftType getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void updateStatus(User caller, Status status) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.status = status;
    }

    public List<Role> getNecessaryRoles() {
        return necessaryRoles;
    }

    public void addNecessaryRoles(User caller, Role r) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.necessaryRoles.add(r);
    }

    public void removeRole(User caller, Role r) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.necessaryRoles.remove(r);

        for (FilledRole fr:this.filledRoles){
            if (fr.getRole()==r){
                this.employees.remove(fr.getEmployee()); //remove the emp from shift
                this.filledRoles.remove(fr);
                break;
            }
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    // Only by HR manager methods:

    // Add/Remove employee helpers (updated to allow shift managers)
    public void addEmployee(User caller, Employee employee, Role role) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied");
        }
        this.filledRoles.addLast(new FilledRole(employee,role));
        this.employees.addLast(employee);
    }

    public void removeEmployee(User caller, Employee employee) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied");
        }
        for (FilledRole fr:this.filledRoles){
            if (fr.getEmployee()==employee){
                this.filledRoles.remove(fr); //the role isn't filled anymore
                updateStatus(caller,Status.Problem);
                break;
            }
        }
        this.employees.remove(employee);
    }

    @Override
    public String toString() {
        StringBuilder rolesStr = new StringBuilder();
        for (Role role : necessaryRoles) {
            rolesStr.append(role.getDescription()).append(", ");
        }
        if (rolesStr.length() > 0) rolesStr.setLength(rolesStr.length() - 2); // הסרת פסיק אחרון

        StringBuilder employeesStr = new StringBuilder();
        for (Employee emp : employees) {
            employeesStr.append(emp.getEmpName()).append(", ");
        }
        if (employeesStr.length() > 0) employeesStr.setLength(employeesStr.length() - 2); // הסרת פסיק אחרון

        return "Shift " + type + " number: " + shiftID + " at " + day + " — status: " + status
                + "\nRoles: " + rolesStr
                + "\nWorkers: " + employeesStr;
    }

    public Employee getShiftManager() {
        return shiftManager;
    }

    public void setShiftManager(User caller, Employee shiftManager) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.shiftManager = shiftManager;
    }
}
