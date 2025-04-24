package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Branch;
import HR_Mudol.domain.Employee;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

import java.util.List;

public interface IEmployeeManager {

    //Add new employee to the system
    void addEmployee(User caller);

    //Fire employee
    void removeEmployee(User caller);

    //Update an employee's details:

    void updateBankAccount(User caller);

    void updateSalary(User caller);

    void updateMinDayShift(User caller);

    void updateMinEveningShift(User caller);

    void setInitialsickDays(User caller);

    void setInitialdaysOff(User caller);

    //Searching method:
    Employee getEmployeeById(User caller, int ID);

    public Branch getBranch();

    public IRoleManager getRoleManager();


    //Printing method:
    void printEmployees(User caller);

    void printAllEmployees(User caller);

    List<User> getAllUsers(User caller);

}
