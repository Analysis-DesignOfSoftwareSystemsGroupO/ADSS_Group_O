package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;

import java.sql.SQLException;
import java.util.List;

/**
 * The IRoleManager interface defines the operations related to managing roles within the system.
 * These operations include creating, updating, assigning, and removing roles, as well as querying and counting employees' role assignments.
 */
public interface IRoleController {

    /**
     * Creates a new role in the system.
     * This operation is typically performed by an authorized user (e.g., HR or admin).
     *
     * @param theCaller The user who is creating the role (typically an HR manager).
     */
    void createRole(UserDTO theCaller) throws SQLException;

    /**
     * Updates the description of an existing role.
     * This allows for modifying role responsibilities or other details.
     *
     * @param theCaller The user who is updating the role description (typically an HR manager).
     */
    void updateRoleDescription(UserDTO theCaller) throws SQLException;

    /**
     * Assigns an employee to a specific role.
     * This operation links an employee to the responsibilities defined in a role.
     *
     * @param theCaller The user who is assigning the employee to the role (typically an HR manager).
     */
    void assignEmployeeToRole(UserDTO theCaller) throws SQLException;

    /**
     * Assigns an employee to be a shift manager.
     * This grants the employee the authority to manage shifts for a specified period.
     *
     * @param theCaller The user who is assigning the shift manager role (typically an HR manager).
     */
    void assignEmployeeToShiftManager(UserDTO theCaller) throws SQLException;

    /**
     * Removes an employee from a role.
     * This operation unassigns an employee from their current role.
     *
     * @param theCaller The user who is removing the employee from the role (typically an HR manager).
     */
    void removeEmployeeFromALLRoles(UserDTO theCaller) throws SQLException;

    /**
     * Removes an employee from a specific role.
     * This operation unassigns an employee from a specific role based on role number.
     *
     * @param theCaller The user who is removing the employee from the role (typically an HR manager).
     * @param roleNumber The identifier for the role.
     * @param e The employee to be removed from the role.
     */
    void removeEmployeeFromRole(UserDTO theCaller, int roleNumber, EmployeeDTO e) throws SQLException;

    /**
     * Finds all employees who have been assigned to any role.
     * This is used to query employees who are currently in one or more roles.
     *
     * @param theCaller The user who is querying the employees (typically an HR manager).
     * @return A list of employees who are assigned to roles.
     */
    List<Employee> getRelevantEmployees(UserDTO theCaller) throws SQLException;

    /**
     * Retrieves a list of all roles in the system.
     * This provides an overview of all the roles available in the system.
     *
     * @param theCaller The user who is querying the roles (typically an HR manager).
     * @return A list of all roles in the system.
     */
    List<Role> getAllRoles(UserDTO theCaller) throws SQLException;

    /**
     * Prints all roles in the system.
     * This operation prints details of each role to the console or log.
     *
     * @param theCaller The user who is requesting the printout of all roles.
     */
    void printAllRoles(UserDTO theCaller) throws SQLException;

    /**
     * Retrieves a role by its unique number.
     * This is used to fetch specific roles by their identifier.
     *
     * @param roleNumber The unique identifier for the role.
     * @return The role with the specified role number.
     */
    Role getRoleByNumber(int roleNumber);

    /**
     * Counts the number of employees who do not have any assigned roles.
     * This operation helps to track employees who have not been assigned to any role.
     *
     * @param theCaller The user who is counting employees without roles (typically an HR manager).
     * @param employeeList The list of all employees in the system.
     * @return The number of employees who do not have any roles.
     */
    int countEmployeesWithoutRoles(UserDTO theCaller, List<EmployeeDTO> employeeList) throws SQLException;
        }
