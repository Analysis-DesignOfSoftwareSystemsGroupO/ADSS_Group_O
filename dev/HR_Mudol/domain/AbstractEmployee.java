package HR_Mudol.domain;


import java.time.LocalDate;

public abstract class AbstractEmployee {

    static int empCounter=0; //Count how many employees

    private final int empNum; //Employee’s number
    private String empName; //Full Name
    private final int empId; //ID
    private String empPassword;
    private String empBankAccount;
    private int empSalary;
    private final LocalDate empStartDate;

    //Constructor
    public AbstractEmployee(String empName, int empId, String empPassword, String empBankAccount, int empSalary, LocalDate empStartDate) {


        //Checking the ID
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

    //everybody can access
    public int getEmpNum() {
        return empNum;
    }

    //everybody can access
    public String getEmpName() {
        return empName;
    }

    //everybody can access
    public int getEmpId() {
        return empId;
    }


    //everybody can access
    public String getEmpBankAccount() {

        return empBankAccount;
    }

    //only manager or the man himself can access
    public int getEmpSalary() {

        return empSalary;
    }

    //everybody can access
    public LocalDate getEmpStartDate() {
        return empStartDate;
    }



    //only the man himself
    public void setEmpName(String empName,User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empName = empName;
    }


    //can use just by the employee
    public void setEmpPassword(String empPassword,User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empPassword = empPassword;
    }


    public void setEmpBankAccount(User caller, String empBankAccount) {
        if (!caller.isSameEmployee(this)&&!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.empBankAccount = empBankAccount;
    }

    //can use just by the HR manager
    public void setEmpSalary( User caller, int empSalary) {

        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.empSalary = empSalary;
    }

}
