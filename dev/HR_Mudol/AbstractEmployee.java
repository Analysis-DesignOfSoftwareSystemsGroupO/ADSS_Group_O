package HR_Mudol;

public abstract class AbstractEmployee {
    private int empNum;
    private String empName;
    private int empId;
    private String empPassword;
    private String empBankAccount;
    private int empSalary;
    private String empStartDate;

    public AbstractEmployee(int empNum, String empName, int empId, String empPassword, String empBankAccount, int empSalary, String empStartDate) {
        this.empNum = empNum;
        this.empName = empName;
        this.empId = empId;
        this.empPassword = empPassword;
        this.empBankAccount = empBankAccount;
        this.empSalary = empSalary;
        this.empStartDate = empStartDate;
    }

    public int getEmpNum() {
        return empNum;
    }

    public String getEmpName() {
        return empName;
    }

    public int getEmpId() {
        return empId;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public String getEmpBankAccount() {
        return empBankAccount;
    }

    public int getEmpSalary() {
        return empSalary;
    }

    public String getEmpStartDate() {
        return empStartDate;
    }

    public void setEmpNum(int empNum) {
        this.empNum = empNum;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public void setEmpBankAccount(String empBankAccount) {
        this.empBankAccount = empBankAccount;
    }

    public void setEmpSalary(int empSalary) {
        this.empSalary = empSalary;
    }

    public void setEmpStartDate(String empStartDate) {
        this.empStartDate = empStartDate;
    }
}
