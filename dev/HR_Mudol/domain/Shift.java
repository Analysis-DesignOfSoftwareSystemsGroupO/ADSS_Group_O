package HR_Mudol.domain;

import java.util.LinkedList;
import java.util.List;

public class Shift {

    static int counter=0; //Counter

    private int shiftID;
    private DayOfWeek day;
    private ShiftType type;
    private Status status;
    private List<Employee> employees; //The employees who work at that shift
    private List<Role> necessaryRoles;

    public Shift(DayOfWeek day,ShiftType type){
        counter++;
        this.shiftID=counter;
        this.day=day;
        this.type=type;
        this.employees=new LinkedList<>();
        this.status=Status.Empty;
    }

    // Getters
    public int getShiftID() {
        return shiftID;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public ShiftType getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public void updateStatus() {
        //for each
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    //Only by HR manager methods:

    public void setStatus(User caller, Status status) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.status = status;
    }

    public void setEmployees(User caller,List<Employee> employees) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.employees = employees;
    }

    // Add/Remove employee helpers
    public void addEmployee(User caller,Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.employees.addLast(employee);
    }

    public void removeEmployee(User caller,Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.employees.remove(employee);
    }

    @Override
    public String toString(){
        return "Shift "+ type + " number: "+ shiftID + " at "+ day + " — " + status
                +"\n Workers: "+ employees;
        }

}
