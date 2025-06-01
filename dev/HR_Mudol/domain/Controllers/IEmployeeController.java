package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.ShiftType;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing employees within a branch.
 * Provides methods to add, remove, update, search, and display employees.
 */
public interface IEmployeeController {

    /**
     * Adds a new employee to the system.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void addEmployee(UserDTO theCaller) throws SQLException;

    /**
     * Removes an employee from the system.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void removeEmployee(UserDTO theCaller) throws SQLException;

    // --- Update employee details ---

    /**
     * Updates the bank account information of an employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void updateBankAccount(UserDTO theCaller) throws SQLException;

    /**
     * Updates the salary of an employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void updateSalary(UserDTO theCaller) throws SQLException;

    /**
     * Updates the minimum required number of day shifts for an employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void updateMinDayShift(UserDTO theCaller) throws SQLException;

    /**
     * Updates the minimum required number of evening shifts for an employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void updateMinEveningShift(UserDTO theCaller) throws SQLException;

    /**
     * Sets the initial number of sick days for an employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void setInitialsickDays(UserDTO theCaller) throws SQLException;

    /**
     * Sets the initial number of vacation days for an employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void setInitialdaysOff(UserDTO theCaller) throws SQLException;

    // --- Search and retrieval ---

    /**
     * Retrieves an employee by their ID.
     * @param theCaller the user performing the operation (must be a manager)
     * @param ID the employee ID
     * @return the employee object if found, or null otherwise
     */
    EmployeeDTO getEmployeeById(UserDTO theCaller, int ID) throws SQLException;

    void close();

    /**
     * Returns the branch associated with this employee manager.
     * @return the current branch
     */
    Branch getBranch();

    /**
     * Prints the details of a specific employee.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void printEmployees(UserDTO theCaller) throws SQLException;

    /**
     * Prints the details of all employees in the branch.
     * @param theCaller the user performing the operation (must be a manager)
     */
    void printAllEmployees(UserDTO theCaller) throws SQLException;

    boolean verifyPassword(UserDTO caller, int empId, String password);

    void updatePassword(UserDTO callerDTO, int empId, String newPassword) throws SQLException;

    List<ConstraintDTO> getConstraintsByEmployeeId(int employeeId);

    List<RoleDTO> getRolesForEmployee(int employeeId);

    public void lockWeeklyConstraints(int empId);

    public int getMinDayShifts(int empId);

    public int getMinEveningShifts(int empId);

    void submitConstraint(int empId, ConstraintDTO constraintDTO) throws SQLException;

    EmploymentContractDTO getContractDetails(UserDTO caller, int empId);

    List<ConstraintDTO> getConstraintsByType(int empId, ShiftType type);

    void updateConstraintExplanation(EmployeeDTO emp, ConstraintDTO constraint, String newExplanation);

    void removeConstraint(int empId, ConstraintDTO constraint) throws SQLException;

    /**
     * Returns the total number of employees in the branch.
     */
    int getEmployeeCount();

    /**
     * Returns all employees in the branch as DTOs.
     */
    List<EmployeeDTO> getAllEmployees() throws SQLException;


    /**
     * Returns the access level of the user (e.g., HRManager, shiftManager, regularEmp)
     * @param employeeId the employee ID
     * @return String representing the level
     */
    String getUserLevel(int employeeId) throws SQLException;


}
