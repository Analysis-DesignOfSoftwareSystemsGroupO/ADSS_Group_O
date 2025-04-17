package HR_Mudol;

public class HR_Manger extends AbstractEmployee{

    public HR_Manger(User caller, String empName, int empId, String empPassword, String empBankAccount, int empSalary, String empStartDate) {
        super(caller, empName, empId, empPassword, empBankAccount, empSalary, empStartDate);
    }

    public String toString(User caller) {
        return "HR Manager:"+
                "\n  Full Name: '" + this.getEmpName() + '\'' +
                "\n  ID: " + this.getEmpId() +
                "\n  Bank Account: '" + this.getEmpBankAccount() + '\'' +
                "\n  Salary: " + this.getEmpSalary(caller) + '\'' +
                "\n  Start Date: " + this.getEmpBankAccount();
    }

}
