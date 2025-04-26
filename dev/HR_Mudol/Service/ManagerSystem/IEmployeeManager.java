package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Branch;
import HR_Mudol.domain.Employee;
import HR_Mudol.domain.User;

import java.util.List;

/**
 * Interface for managing employees within a branch.
 * Provides methods to add, remove, update, search, and display employees.
 */
public interface IEmployeeManager {

    /**
     * Adds a new employee to the system.
     * @param caller the user performing the operation (must be a manager)
     */
    void addEmployee(User caller);

    /**
     * Removes an employee from the system.
     * @param caller the user performing the operation (must be a manager)
     */
    void removeEmployee(User caller);

    // --- Update employee details ---

    /**
     * Updates the bank account information of an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateBankAccount(User caller);

    /**
     * Updates the salary of an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateSalary(User caller);

    /**
     * Updates the minimum required number of day shifts for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateMinDayShift(User caller);

    /**
     * Updates the minimum required number of evening shifts for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateMinEveningShift(User caller);

    /**
     * Sets the initial number of sick days for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void setInitialsickDays(User caller);

    /**
     * Sets the initial number of vacation days for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void setInitialdaysOff(User caller);

    // --- Search and retrieval ---

    /**
     * Retrieves an employee by their ID.
     * @param caller the user performing the operation (must be a manager)
     * @param ID the employee ID
     * @return the employee object if found, or null otherwise
     */
    Employee getEmployeeById(User caller, int ID);

    /**
     * Returns the branch associated with this employee manager.
     * @return the current branch
     */
    Branch getBranch();

    /**
     * Returns the role manager associated with this employee manager.
     * @return the role manager
     */
    IRoleManager getRoleManager();

    // --- Display ---

    /**
     * Prints the details of a specific employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void printEmployees(User caller);

    /**
     * Prints the details of all employees in the branch.
     * @param caller the user performing the operation (must be a manager)
     */
    void printAllEmployees(User caller);

    /**
     * Returns a list of all users in the branch.
     * @param caller the user performing the operation (must be a manager)
     * @return list of users
     */
    List<User> getAllUsers(User caller);
}
