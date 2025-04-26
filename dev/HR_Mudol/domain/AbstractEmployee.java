package HR_Mudol.domain;

import java.time.LocalDate;

/**
 * Abstract base class representing a company employee.
 * Stores basic employee details and provides access and update methods
 * with appropriate access control.
 */
public abstract class AbstractEmployee {

    // Counts the total number of employees created
    static int empCounter = 0;

    // Employee’s unique number within the system
    private final int empNum;

    // Employee’s full name
    private String empName;

    // Employee’s ID number (must be exactly 9 digits)
    private final int empId;

    // Employee’s account password
    private String empPassword;

    // Employee’s bank account number
    private String empBankAccount;

    // Employee’s salary
    private int empSalary;

    // Employee’s start date at the company
    private final LocalDate empStartDate;

    /**
     * Constructs a new AbstractEmployee with the given details.
     * Validates the ID format and increments the employee counter.
     *
     * @param empName Employee's full name
     * @param empId Employee's ID number (must be positive and 9 digits)
     * @param empPassword Employee's password
     * @param empBankAccount Employee's bank account number
     * @param empSalary Employee's salary
     * @param empStartDate Employee's start date
     * @throws IllegalArgumentException if ID is invalid
     */
    public AbstractEmployee(String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate) {

        if (empId <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number.");
        }

        String idString = String.valueOf(empId);
        if (idString.length() != 9) {
            throw new IllegalArgumentException("Employee ID must be exactly 9 digits.");
        }

        empCounter++;
        this.empNum = empCounter;
        this.empName = empName;
        this.empId = empId;
        this.empPassword = empPassword;
        this.empBankAccount = empBankAccount;
        this.empSalary = empSalary;
        this.empStartDate = empStartDate;
    }

    /**
     * Returns the employee’s unique number.
     */
    public int getEmpNum() {
        return empNum;
    }

    /**
     * Returns the employee’s full name.
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * Returns the employee’s ID number.
     */
    public int getEmpId() {
        return empId;
    }

    /**
     * Returns the employee’s bank account number.
     */
    public String getEmpBankAccount() {
        return empBankAccount;
    }

    /**
     * Returns the employee’s salary.
     * Only accessible by the employee himself or a manager.
     */
    public int getEmpSalary() {
        return empSalary;
    }

    /**
     * Returns the employee’s start date at the company.
     */
    public LocalDate getEmpStartDate() {
        return empStartDate;
    }

    /**
     * Updates the employee's full name.
     * Only the employee himself can perform this action.
     *
     * @param empName New full name
     * @param caller The user attempting to make the change
     * @throws SecurityException if the caller is unauthorized
     */
    public void setEmpName(String empName, User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empName = empName;
    }

    /**
     * Updates the employee’s password.
     * Only the employee himself can perform this action.
     *
     * @param empPassword New password
     * @param caller The user attempting to make the change
     * @throws SecurityException if the caller is unauthorized
     */
    public void setEmpPassword(String empPassword, User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empPassword = empPassword;
    }

    /**
     * Updates the employee’s bank account number.
     * Only the employee himself or a manager can perform this action.
     *
     * @param empBankAccount New bank account number
     * @param caller The user attempting to make the change
     * @throws SecurityException if the caller is unauthorized
     */
    public void setEmpBankAccount(User caller, String empBankAccount) {
        if (!caller.isSameEmployee(this) && !caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.empBankAccount = empBankAccount;
    }

    /**
     * Updates the employee’s salary.
     * Only a manager can perform this action.
     *
     * @param caller The user attempting to make the change
     * @param empSalary New salary
     * @throws SecurityException if the caller is not a manager
     */
    public void setEmpSalary(User caller, int empSalary) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.empSalary = empSalary;
    }

    /**
     * Returns the employee’s password.
     * Intended for internal use only.
     */
    public String getEmpPassword() {
        return this.empPassword;
    }

    /**
     * Verifies if the given password matches the employee's password.
     * Only accessible by the employee himself or a manager.
     *
     * @param inputPassword The password to verify
     * @param caller The user attempting the verification
     * @return true if the password matches, false otherwise
     * @throws SecurityException if the caller is unauthorized
     */
    public boolean verifyPassword(String inputPassword, User caller) {
        if (!caller.isSameEmployee(this) && !caller.isManager()) {
            throw new SecurityException("Access denied: Only the employee or a manager can verify the password.");
        }
        return this.empPassword.equals(inputPassword);
    }

    /**
     * Compares two employees based on their ID number.
     *
     * @param obj The object to compare
     * @return true if the ID numbers are the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee other = (Employee) obj;
        return this.empId == (other.getEmpId());
    }
}
