package HR_Mudol.domain;

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
    public Employee(String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate, int minDayShift, int minEveninigShift, int sickDays, int daysOff) {
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
    public List<Role> getRelevantRoles(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return this.relevantRoles;
    }

    /**
     * Returns the list of weekly constraints submitted by the employee.
     * Accessible by the employee himself or the HR manager.
     */
    public List<Constraint> getWeeklyConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return this.weeklyConstraints;
    }

    /**
     * Returns the list of morning shift constraints.
     */
    public List<Constraint> getMorningConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return morningConstraints;
    }

    /**
     * Returns the list of evening shift constraints.
     */
    public List<Constraint> getEveningConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return eveningConstraints;
    }

    /**
     * Adds a new morning constraint.
     * @throws NullPointerException if constraint is null.
     */
    public void addNewMorningConstraints(User caller, Constraint constraint) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        if (constraint == null) {
            throw new NullPointerException("Constraint cannot be null");
        }
        morningConstraints.add(constraint);
    }


    /**
     * Adds a new evening constraint.
     * @throws NullPointerException if constraint is null.
     */
    public void addNewEveningConstraints(User caller, Constraint constraint) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        if (constraint == null) {
            throw new NullPointerException("Constraint cannot be null");
        }
        eveningConstraints.add(constraint);
    }


    /**
     * Adds a new general weekly constraint.
     * Only the employee himself or a manager can add a constraint.
     *
     * @param caller The user attempting to add the constraint.
     * @param constraint The constraint to add. Must not be null.
     * @throws SecurityException if the caller is unauthorized.
     * @throws NullPointerException if the constraint is null.
     */
    public void addNewConstraints(User caller, Constraint constraint) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
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
    public int getMinDayShift(User caller) {
        return this.Contract.getMinDayShift(caller, this);
    }

    /**
     * Sets the minimum number of day shifts required.
     */
    public void setMinDayShift(User caller, int minDayShift) {
        this.Contract.setMinDayShift(caller, minDayShift);
    }

    /**
     * Returns the minimum number of evening shifts required.
     */
    public int getMinEveninigShift(User caller) {
        return this.Contract.getMinEveninigShift(caller, this);
    }

    /**
     * Sets the minimum number of evening shifts required.
     */
    public void setMinEveninigShift(User caller, int minEveninigShift) {
        this.Contract.setMinEveninigShift(caller, minEveninigShift);
    }

    /**
     * Returns the number of sick days remaining.
     */
    public int getSickDays(User caller) {
        return this.Contract.getSickDays(caller, this);
    }

    /**
     * Sets the number of sick days.
     */
    public void setSickDays(User caller, int sickDays) {
        this.Contract.setSickDays(caller, sickDays);
    }

    /**
     * Returns the number of vacation days remaining.
     */
    public int getDaysOff(User caller) {
        return this.Contract.getDaysOff(caller, this);
    }

    /**
     * Sets the number of vacation days.
     */
    public void setDaysOff(User caller, int daysOff) {
        this.Contract.setDaysOff(caller, daysOff);
    }

    /**
     * Returns the employee's contract details.
     */
    public EmploymentContract getContract(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        return this.Contract;
    }

    /**
     * Locks the weekly constraints into the locked constraints list,
     * clearing the weekly, morning, and evening constraints afterwards.
     */
    public void lockWeeklyConstraints(User caller) {
        if (!caller.isManager() && !caller.isSameEmployee(this)) {
            throw new SecurityException("Only manager can lock constraints.");
        }

        this.lockedConstraints = new ArrayList<>(this.getWeeklyConstraints(caller));
        this.getWeeklyConstraints(caller).clear();
        this.getMorningConstraints(caller).clear();
        this.getEveningConstraints(caller).clear();
    }

    /**
     * Returns the locked constraints (after submission deadline).
     * Only a manager is allowed to access locked constraints.
     *
     * @param caller The user attempting to access the locked constraints.
     * @return A list of locked constraints.
     * @throws SecurityException if the caller is not a manager.
     */
    public List<Constraint> getLockedConstraints(User caller) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
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
}
