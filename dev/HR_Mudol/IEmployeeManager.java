package HR_Mudol;

public interface IEmployeeManager {

    //Add new employee to the system
    void addEmployee(User caller, Employee employee);

    //Fire employee
    void removeEmployee(User caller, int empId);

    //Update an employee's details:

    void updateBankAccount(User caller, int empId, String newBankAccount);

    void updateSalary(User caller, int empId, int newSalary);

    void updateMinDayShift(User caller, int empId, int newNumber);

    void updateMinEveningShift(User caller, int empId, int newNumber);

    void setInitialsickDays(User caller, int empId, int Number);

    void setInitialdaysOff(User caller, int empId, int Number);

    //Searching method:
    Employee getEmployeeById(User caller, int empId);

    //Printing method:
    void printEmployees(User caller,int empId);

    void printAllEmployees(User caller);
}
