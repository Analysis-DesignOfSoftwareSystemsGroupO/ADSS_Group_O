package HR_Mudol.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a work shift, including its roles, employees, and status.
 */
public class Shift {

    static int counter = 0; // Static counter for unique shift IDs

    private int shiftID;
    private WeekDay day;
    private ShiftType type;
    private Status status;
    private List<Employee> employees;
    private List<Role> necessaryRoles;
    private Employee shiftManager;
    private List<FilledRole> filledRoles;

    /**
     * Constructs a new shift for a given day and shift type.
     */
    public Shift(WeekDay day, ShiftType type) {
        counter++;
        this.shiftID = counter;
        this.day = day;
        this.type = type;
        this.employees = new LinkedList<>();
        this.status = Status.Empty;
        this.necessaryRoles = new LinkedList<>();
        this.filledRoles = new LinkedList<>();
    }

    // -------------------- Getters --------------------

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

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Role> getNecessaryRoles() {
        return necessaryRoles;
    }

    public List<FilledRole> getFilledRoles() {
        return filledRoles;
    }

    public Employee getShiftManager() {
        return shiftManager;
    }

    // -------------------- Permissions-based actions --------------------

    /**
     * Updates the shift status.
     */
    public void updateStatus(User caller, Status status) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied");
        }
        this.status = status;
    }

    /**
     * Adds a necessary role to this shift.
     */
    public void addNecessaryRoles(User caller, Role r) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        if (r == null || necessaryRoles.contains(r)) {
            throw new IllegalArgumentException("Role already exists or is null.");
        }
        this.necessaryRoles.add(r);
    }

    /**
     * Removes a necessary role and the employees assigned to it.
     */
    public void removeRole(User caller, Role r) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        if (!necessaryRoles.contains(r)) {
            throw new IllegalArgumentException("Role is not assigned to this shift.");
        }

        this.necessaryRoles.remove(r);

        for (FilledRole fr : new LinkedList<>(this.filledRoles)) {
            if (fr.getRole().equals(r)) {
                this.employees.remove(fr.getEmployee());
                this.filledRoles.remove(fr);
            }
        }
    }

    /**
     * Adds an employee to this shift with a specific role.
     */
    public void addEmployee(User caller, Employee employee, Role role) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied");
        }
        if (!necessaryRoles.contains(role)) {
            throw new IllegalArgumentException("Role is not necessary for this shift.");
        }
        if (employees.contains(employee)) {
            throw new IllegalArgumentException("Employee already assigned to this shift.");
        }
        this.filledRoles.addLast(new FilledRole(employee, role));
        this.employees.addLast(employee);
    }

    /**
     * Removes an employee from the shift.
     */
    public void removeEmployee(User caller, Employee employee) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied");
        }
        if (!employees.contains(employee)) {
            throw new IllegalArgumentException("Employee is not assigned to this shift.");
        }

        for (FilledRole fr : new LinkedList<>(this.filledRoles)) {
            if (fr.getEmployee().equals(employee)) {
                this.filledRoles.remove(fr);
                updateStatus(caller, Status.Problem);
                break;
            }
        }
        this.employees.remove(employee);
    }

    /**
     * Sets the manager responsible for the shift.
     */
    public void setShiftManager(User caller, Employee shiftManager) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.shiftManager = shiftManager;
    }

    /**
     * Returns a list of necessary roles that are not yet filled.
     */
    public List<Role> getNotOccupiedRoles() {
        List<Role> notOccupied = new LinkedList<>();
        for (Role role : necessaryRoles) {
            boolean isFilled = false;
            for (FilledRole fr : filledRoles) {
                if (fr.getRole().equals(role)) {
                    isFilled = true;
                    break;
                }
            }
            if (!isFilled) {
                notOccupied.add(role);
            }
        }
        return notOccupied;
    }

    /**
     * Returns a string representation of the shift, including roles and assigned employees.
     */
    @Override
    public String toString() {
        StringBuilder rolesStr = new StringBuilder();
        for (Role role : necessaryRoles) {
            rolesStr.append(role.getDescription()).append(", ");
        }
        if (rolesStr.length() > 0) rolesStr.setLength(rolesStr.length() - 2);

        StringBuilder employeesStr = new StringBuilder();
        for (Employee emp : employees) {
            employeesStr.append(emp.getEmpName()).append(", ");
        }
        if (employeesStr.length() > 0) employeesStr.setLength(employeesStr.length() - 2);

        return "Shift " + type + " number: " + shiftID + " at " + day + " — status: " + status
                + "\nRoles: " + rolesStr
                + "\nWorkers: " + employeesStr;
    }
}
