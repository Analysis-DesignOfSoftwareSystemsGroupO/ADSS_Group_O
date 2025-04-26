package HR_Mudol.domain;

import java.time.LocalDate;

/**
 * Represents an HR Manager in the system.
 */
public class HRManager extends Employee {

    /**
     * Constructs an HR Manager with all necessary employee details.
     */
    public HRManager(String empName, int empId, String empPassword, String empBankAccount,
                     int empSalary, LocalDate empStartDate,
                     int minDayShift, int minEveningShift, int sickDays, int daysOff) {
        super(empName, empId, empPassword, empBankAccount, empSalary, empStartDate,
                minDayShift, minEveningShift, sickDays, daysOff);
    }

    /**
     * Returns a string representation of the HR Manager's details.
     */
    public String toString(User caller) {
        return "HR Manager:" +
                "\n  Full Name: " + this.getEmpName() +
                "\n  ID: " + this.getEmpId() +
                "\n  Bank Account: " + this.getEmpBankAccount() +
                "\n  Salary: " + this.getEmpSalary() +
                "\n  Start Date: " + this.getEmpStartDate();
    }
}
