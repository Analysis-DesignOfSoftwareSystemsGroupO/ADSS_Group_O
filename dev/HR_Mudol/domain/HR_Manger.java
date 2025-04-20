package HR_Mudol.domain;

import java.time.LocalDate;

public class HR_Manger extends AbstractEmployee{

    public HR_Manger(String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate) {
        super(empName, empId, empPassword, empBankAccount, empSalary, empStartDate);
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
