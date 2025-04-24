package HR_Mudol.domain;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class Employee extends AbstractEmployee {

    private List<Role> relevantRoles;
    private EmploymentContract Contract;
    private List<Constraint> weeklyConstraints;
    private List<Constraint> morningConstraints;
    private List<Constraint> eveningConstraints;
    private List<Constraint> lockedConstraints;


    public Employee(String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate, int minDayShift, int minEveninigShift, int sickDays, int daysOff) {

        super(empName, empId, empPassword, empBankAccount, empSalary, empStartDate);
        this.Contract = new EmploymentContract(minDayShift, minEveninigShift, sickDays, daysOff,this);
        this.weeklyConstraints = new LinkedList<>();
        this.relevantRoles = new LinkedList<>();
        this.morningConstraints = new LinkedList<>();
        this.eveningConstraints = new LinkedList<>();
        this.lockedConstraints = new LinkedList<>();
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
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return this.weeklyConstraints;
    }

    public List<Constraint> getMorningConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return morningConstraints;
    }

    public List<Constraint> getEveningConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return eveningConstraints;
    }


    public void addNewMorningConstraints(User caller,Constraint constraint){
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        morningConstraints.add(constraint);
    }
    public void addNewEveningConstraints(User caller,Constraint constraint){
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        eveningConstraints.add(constraint);
    }
    //only by the emp himself
    public void addNewConstraints(User caller,Constraint constraint){
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        weeklyConstraints.add(constraint);
    }


    public Constraint searchingForRelevantconstraint(User caller, Week currentWeek, WeekDay day, ShiftType type) {
        // בדיקת הרשאה
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }

        // אם עדיין מותר להגיש – נחפש ברשימה השבועית
        List<Constraint> relevantList;
        if (currentWeek.isConstraintSubmissionOpen()) {
            relevantList = this.weeklyConstraints;
        } else {
            // אם הדדליין עבר – נחפש ברשימה הסגורה
            relevantList = this.lockedConstraints;
        }

        // חיפוש אילוץ לפי יום וסוג משמרת
        for (Constraint c : relevantList) {
            if (c.getDay() == day && c.getType() == type) {
                return c;
            }
        }

        return null; // אם לא נמצא כלום
    }


    // This method assumes that access control has already been verified externally
    public void printRelevantRoles() {
        System.out.println("\n--- Relevant Roles ---");
        if (relevantRoles.isEmpty()) {
            System.out.println("No roles assigned.");
        } else {
            for (int i = 0; i < relevantRoles.size(); i++) {
                System.out.println((i + 1) + ". " + relevantRoles.get(i));
            }
        }
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
    public EmploymentContract getContract(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return this.Contract;
    }

    public void lockWeeklyConstraints(User caller) {
        if (!caller.isManager()&& !caller.isSameEmployee(this)) {
            throw new SecurityException("Only manager can lock constraints.");
        }

        this.lockedConstraints = new ArrayList<>(this.getWeeklyConstraints(caller)); // שומר את האילוצים
        this.getWeeklyConstraints(caller).clear();
        this.getMorningConstraints(caller).clear();
        this.getEveningConstraints(caller).clear();
    }
    public List<Constraint> getLockedConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied.");
        }
        return this.lockedConstraints;
    }





    public void setDaysOff(User caller, int daysOff) {

        this.Contract.setDaysOff(caller,daysOff);
    }

    public String toString() {
        return "Employee " + this.getEmpNum() +
                "\n  Full Name: " + this.getEmpName() +
                "\n  ID: " + this.getEmpId() +
                "\n  Bank Account: " + this.getEmpBankAccount() +
                "\n  Salary: " + this.getEmpSalary() +
                "\n  Start Date: " + this.getEmpStartDate();

    }

}

