package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.User;

import java.sql.SQLException;

/**
 * Interface for managing employees within a branch.
 * Provides methods to add, remove, update, search, and display employees.
 */
public interface IEmployeeController {

    /**
     * Adds a new employee to the system.
     * @param caller the user performing the operation (must be a manager)
     */
    void addEmployee(UserDTO caller) throws SQLException;

    /**
     * Removes an employee from the system.
     * @param caller the user performing the operation (must be a manager)
     */
    void removeEmployee(UserDTO caller) throws SQLException;

    // --- Update employee details ---

    /**
     * Updates the bank account information of an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateBankAccount(UserDTO caller) throws SQLException;

    /**
     * Updates the salary of an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateSalary(UserDTO caller) throws SQLException;

    /**
     * Updates the minimum required number of day shifts for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateMinDayShift(UserDTO caller) throws SQLException;

    /**
     * Updates the minimum required number of evening shifts for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void updateMinEveningShift(UserDTO caller) throws SQLException;

    /**
     * Sets the initial number of sick days for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void setInitialsickDays(UserDTO caller) throws SQLException;

    /**
     * Sets the initial number of vacation days for an employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void setInitialdaysOff(UserDTO caller) throws SQLException;

    // --- Search and retrieval ---

    /**
     * Retrieves an employee by their ID.
     * @param caller the user performing the operation (must be a manager)
     * @param ID the employee ID
     * @return the employee object if found, or null otherwise
     */
    Employee getEmployeeById(UserDTO caller, int ID);

    /**
     * Returns the branch associated with this employee manager.
     * @return the current branch
     */
    Branch getBranch();

    /**
     * Prints the details of a specific employee.
     * @param caller the user performing the operation (must be a manager)
     */
    void printEmployees(UserDTO caller);

    /**
     * Prints the details of all employees in the branch.
     * @param caller the user performing the operation (must be a manager)
     */
    void printAllEmployees(UserDTO caller);

}
