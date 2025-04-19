package HR_Mudol.domain;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Employee extends AbstractEmployee {

    private List<Role> relevantRoles;
    private EmploymentContract Contract;
    private List<Constraint> weeklyConstraints;
    public Employee(String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate, int minDayShift, int minEveninigShift, int sickDays, int daysOff) {

        super(empName, empId, empPassword, empBankAccount, empSalary, empStartDate);
        this.Contract = new EmploymentContract(minDayShift, minEveninigShift, sickDays, daysOff);
        this.weeklyConstraints = new LinkedList<>();
        this.relevantRoles = new LinkedList<>();
    }

    public void addNewRole(User caller,Role role){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantRoles.addLast(role);
    }
    public void removeRole(User caller,Role role){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantRoles.remove(role);
    }

    //only of the HR manager
    public List<Role> getRelevantRoles(User caller){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        return this.relevantRoles;
    }

    //only of the HR manager OR the emp himself
    public List<Constraint> getWeeklyConstraints(User caller){
        if (!caller.isManager()||!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return this.weeklyConstraints;
    }

    //only by the emp himself
    public void addNewConstraints(User caller,Constraint constraint){
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        weeklyConstraints.add(constraint);
    }

    //searching for constraints
    public Constraint searchingForRelevantconstraint(User caller, DayOfWeek day, ShiftType type){
        if (!caller.isManager()||!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        for (Constraint c : this.weeklyConstraints){

            if (c.getDay()==day&&c.getType()==type){
                return c;
            }
        }
        return null;
    }

    //for everybody
    public void printRrelevantRoles(){
        //need to add
    }

    public int getMinDayShift(User caller) {

        return this.Contract.getMinDayShift(caller, this);
    }

    public void setMinDayShift(User caller, int minDayShift) {

        this.Contract.setMinDayShift(caller,minDayShift);
    }

    public int getMinEveninigShift(User caller) {

        return this.Contract.getMinEveninigShift(caller, this);
    }

    public void setMinEveninigShift(User caller, int minEveninigShift) {

        this.Contract.setMinEveninigShift(caller,minEveninigShift);
    }

    public int getSickDays(User caller) {

        return this.Contract.getSickDays(caller, this);
    }

    public void setSickDays(User caller, int sickDays) {

        this.Contract.setSickDays(caller,sickDays);
    }
    public int getDaysOff(User caller) {

        return this.Contract.getDaysOff(caller, this);
    }

    public void setDaysOff(User caller, int daysOff) {

        this.Contract.setDaysOff(caller,daysOff);
    }

    public String toString(User caller) {
            return "Employee" + this.getEmpNum() +
                    "\n   Full Name: '" + this.getEmpName() + '\'' +
                    "\n  ID: " + this.getEmpId() +
                    "\n  Bank Account: '" + this.getEmpBankAccount() + '\'' +
                    "\n  Salary: " + this.getEmpSalary(caller) + '\'' +
                    "\n  Start Date: " + this.getEmpBankAccount() + '\'' +
                    "\n" + this.Contract.toString();
        }
    }

