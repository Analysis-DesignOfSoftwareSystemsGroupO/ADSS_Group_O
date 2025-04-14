package HR_Mudol;


public abstract class AbstractEmployee {

    static int empCounter=0; //Count how many employees

    private final int empNum; //Employee’s number
    private String empName; //Full Name
    private final int empId; //ID
    private String empPassword;
    private String empBankAccount;
    private int empSalary;
    private final String empStartDate;

    //Constructor
    public AbstractEmployee(User caller, String empName, int empId, String empPassword, String empBankAccount, int empSalary, String empStartDate) {

        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }

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

    /*no need
    public String getEmpPassword() {

       return empPassword;
    }
    */

    //everybody can access
    public String getEmpBankAccount() {

        return empBankAccount;
    }

    //only manager or the man himself can access
    public int getEmpSalary(User caller) {
        if (!caller.isSameEmployee(this)||!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        return empSalary;
    }

    //everybody can access
    public String getEmpStartDate() {
        return empStartDate;
    }

    /*no need
    public void setEmpNum(int empNum) {
        this.empNum = empNum;
    }
    */

    //only the man himself
    public void setEmpName(String empName,User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empName = empName;
    }

    /* no need
    public void setEmpId(int empId) {
        this.empId = empId;
    }
     */

    //can use just by the employee
    public void setEmpPassword(String empPassword,User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empPassword = empPassword;
    }

    //can use just by the employee
    public void setEmpBankAccount(String empBankAccount, User caller) {
        if (!caller.isSameEmployee(this)) {
            throw new SecurityException("Access denied");
        }
        this.empBankAccount = empBankAccount;
    }

    //can use just by the HR manager
    public void setEmpSalary(int empSalary, User caller) {

        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        this.empSalary = empSalary;
    }

    /* no need
    public void setEmpStartDate(String empStartDate) {
        this.empStartDate = empStartDate;
    }
    */

}
