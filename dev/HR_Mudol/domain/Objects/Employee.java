package HR_Mudol.domain.Objects;

import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an employee in the company, including relevant roles,
 * weekly constraints, and employment contract details.
 */
public class Employee extends AbstractEmployee {

    // List of roles that the employee is qualified for
    private List<Role> relevantRoles;

    // Employee's employment contract details
    private EmploymentContract Contract;

    // Constraints submitted by the employee for the current week
    private List<Constraint> weeklyConstraints;

    // Constraints specific to morning shifts
    private List<Constraint> morningConstraints;

    // Constraints specific to evening shifts
    private List<Constraint> eveningConstraints;

    // Constraints after submission deadline (locked)
    private List<Constraint> lockedConstraints;

    /**
     * Constructs a new Employee with basic details and initializes all constraint lists.
     */
    public Employee(String empName, long empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate, int minDayShift, int minEveninigShift, int sickDays, int daysOff) {
        super(empName, empId, empPassword, empBankAccount, empSalary, empStartDate);
        this.Contract = new EmploymentContract(minDayShift, minEveninigShift, sickDays, daysOff, this);
        this.weeklyConstraints = new LinkedList<>();
        this.relevantRoles = new LinkedList<>();
        this.morningConstraints = new LinkedList<>();
        this.eveningConstraints = new LinkedList<>();
        this.lockedConstraints = new LinkedList<>();
    }

    /**
     * Adds a new role to the employee.
     * Only a manager is allowed to perform this action.
     * @throws NullPointerException if role is null.
     */
    public void addNewRole(User caller, Role role) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        if (role == null) {
            throw new NullPointerException("Role cannot be null");
        }
        this.relevantRoles.addLast(role);
    }


    /**
     * Removes an assigned role from the employee.
     * Only a manager is allowed to perform this action.
     */
    public void removeRole(User caller, Role role) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.relevantRoles.remove(role);
    }

    /**
     * Returns the list of roles assigned to the employee.
     * Only accessible by the HR manager.
     */
    public List<Role> getRelevantRoles() {

        return this.relevantRoles;
    }

    /**
     * Returns the list of weekly constraints submitted by the employee.
     * Accessible by the employee himself or the HR manager.
     */
    public List<Constraint> getWeeklyConstraints() {

        return this.weeklyConstraints;
    }

    /**
     * Returns the list of morning shift constraints.
     */
    public List<Constraint> getMorningConstraints() {

        return morningConstraints;
    }

    /**
     * Returns the list of evening shift constraints.
     */
    public List<Constraint> getEveningConstraints() {
        return eveningConstraints;
    }

    /**
     * Adds a new morning constraint.
     * @throws NullPointerException if constraint is null.
     */
    public void addNewMorningConstraints( Constraint constraint) {

        if (constraint == null) {
            throw new NullPointerException("Constraint cannot be null");
        }
        morningConstraints.add(constraint);
    }


    /**
     * Adds a new evening constraint.
     * @throws NullPointerException if constraint is null.
     */
    public void addNewEveningConstraints(Constraint constraint) {

        if (constraint == null) {
            throw new NullPointerException("Constraint cannot be null");
        }
        eveningConstraints.add(constraint);
    }


    /**
     * Adds a new general weekly constraint.
     * Only the employee himself or a manager can add a constraint.
     *
     * @param constraint The constraint to add. Must not be null.
     * @throws SecurityException if the caller is unauthorized.
     * @throws NullPointerException if the constraint is null.
     */
    public void addNewConstraints( Constraint constraint) {

        if (constraint == null) {
            throw new NullPointerException("Constraint cannot be null");
        }
        weeklyConstraints.add(constraint);
    }


    /**
     * Searches for a constraint that matches the given day and shift type.
     * If submission is still open, searches in weekly constraints;
     * otherwise, searches in locked constraints.
     */
    public Constraint searchingForRelevantconstraint(User caller, Week currentWeek, WeekDay day, ShiftType type) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }

        List<Constraint> relevantList;
        if (currentWeek.isConstraintSubmissionOpen()) {
            relevantList = this.weeklyConstraints;
        } else {
            relevantList = this.lockedConstraints;
        }

        for (Constraint c : relevantList) {
            if (c.getDay() == day && c.getType() == type) {
                return c;
            }
        }

        return null;
    }

    /**
     * Prints the employee's relevant roles.
     */
    public void printRelevantRoles() {
        System.out.println("\n--- Relevant Roles ---");
        if (relevantRoles.isEmpty()) {
            System.out.println("No roles assigned.");
        } else {
            for (int i = 0; i < relevantRoles.size(); i++) {
                Role role = relevantRoles.get(i);
                System.out.println((i + 1) + ". Role number " + role.getRoleNumber() + " - " + role.getDescription());
            }
        }
    }

    /**
     * Returns the minimum number of day shifts required.
     */
    public int getMinDayShift() {
        return this.Contract.getMinDayShift( this);
    }

    /**
     * Sets the minimum number of day shifts required.
     */
    public void setMinDayShift(int minDayShift) {
        this.Contract.setMinDayShift(minDayShift);
    }

    /**
     * Returns the minimum number of evening shifts required.
     */
    public int getMinEveninigShift() {
        return this.Contract.getMinEveninigShift( this);
    }

    /**
     * Sets the minimum number of evening shifts required.
     */
    public void setMinEveninigShift( int minEveninigShift) {
        this.Contract.setMinEveninigShift( minEveninigShift);
    }

    /**
     * Returns the number of sick days remaining.
     */
    public int getSickDays() {
        return this.Contract.getSickDays(this);
    }

    /**
     * Sets the number of sick days.
     */
    public void setSickDays(int sickDays) {
        this.Contract.setSickDays(sickDays);
    }

    /**
     * Returns the number of vacation days remaining.
     */
    public int getDaysOff() {
        return this.Contract.getDaysOff(this);
    }

    /**
     * Sets the number of vacation days.
     */
    public void setDaysOff(User caller, int daysOff) {
        this.Contract.setDaysOff(daysOff);
    }

    /**
     * Returns the employee's contract details.
     */
    public EmploymentContract getContract() {

        return this.Contract;
    }

    /**
     * Locks the weekly constraints into the locked constraints list,
     * clearing the weekly, morning, and evening constraints afterwards.
     */
    public void lockWeeklyConstraints() {

        this.lockedConstraints = new ArrayList<>(this.getWeeklyConstraints());
        this.getWeeklyConstraints().clear();
        this.getMorningConstraints().clear();
        this.getEveningConstraints().clear();
    }

    /**
     * Returns the locked constraints (after submission deadline).
     * Only a manager is allowed to access locked constraints.
     *
     * @return A list of locked constraints.
     * @throws SecurityException if the caller is not a manager.
     */
    public List<Constraint> getLockedConstraints() {

        return this.lockedConstraints;
    }


    /**
     * Returns a string representation of the employee.
     */
    @Override
    public String toString() {
        return "Employee " + this.getEmpNum() +
                "\n  Full Name: " + this.getEmpName() +
                "\n  ID: " + this.getEmpId() +
                "\n  Bank Account: " + this.getEmpBankAccount() +
                "\n  Salary: " + this.getEmpSalary() +
                "\n  Start Date: " + this.getEmpStartDate();
    }

    public void setRelevantRoles(List<Role> relevantRoles) {
        this.relevantRoles = relevantRoles;
    }

    public void setWeeklyConstraints(List<Constraint> weeklyConstraints) {
        this.weeklyConstraints = weeklyConstraints;
    }

    public void setMorningConstraints(List<Constraint> morningConstraints) {
        this.morningConstraints = morningConstraints;
    }

    public void setEveningConstraints(List<Constraint> eveningConstraints) {
        this.eveningConstraints = eveningConstraints;
    }

    public void setLockedConstraints(List<Constraint> lockedConstraints) {
        this.lockedConstraints = lockedConstraints;
    }

    public void removeConstraint(WeekDay day, ShiftType type) {
        // הסרה מהרשימה הכללית
        weeklyConstraints.removeIf(c -> c.getDay() == day && c.getType() == type);

        // הסרה מהרשימה הספציפית לסוג המשמרת
        switch (type) {
            case MORNING:
                morningConstraints.removeIf(c -> c.getDay() == day);
                break;
            case EVENING:
                eveningConstraints.removeIf(c -> c.getDay() == day);
                break;
        }
    }

}
