package HR_Mudol.domain;

import java.time.LocalDate;

public class HRManager extends Employee {

    public HRManager(String empName, int empId, String empPassword, String empBankAccount,
                     int empSalary, LocalDate empStartDate,
                     int minDayShift, int minEveningShift, int sickDays, int daysOff) {
        super(empName, empId, empPassword, empBankAccount, empSalary, empStartDate,
                minDayShift, minEveningShift, sickDays, daysOff);
    }



    public String toString(User caller) {
        return "HR Manager:"+
                "\n  Full Name: '" + this.getEmpName() + '\'' +
                "\n  ID: " + this.getEmpId() +
                "\n  Bank Account: '" + this.getEmpBankAccount() + '\'' +
                "\n  Salary: " + this.getEmpSalary() + '\'' +
                "\n  Start Date: " + this.getEmpBankAccount();
    }

}
