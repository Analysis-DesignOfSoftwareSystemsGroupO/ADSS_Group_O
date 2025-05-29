package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.Shift;
import HR_Mudol.domain.Objects.User;

import java.sql.SQLException;

/**
 * This interface defines the contract for managing employee shifts within the system.
 * It includes operations such as assigning and removing employees from shifts,
 * choosing relevant roles for shifts, and printing shift details.
 * The primary user of this interface is a manager or a user with the required permissions
 * who can manage shift assignments and roles for employees.
 */
public interface IShiftController {

    /**
     * Assigns an employee to a specific shift with a particular role.
     * @param theCaller The user (e.g., manager) requesting the assignment.
     * @param theShift The shift the employee is being assigned to.
     * @param theEmployee The employee being assigned to the shift.
     * @param theRole The role the employee will take on during the shift.
     */
    void assignEmployeeToShift(UserDTO theCaller, ShiftDTO theShift, EmployeeDTO theEmployee, RoleDTO theRole) throws SQLException;

    /**
     * Removes an employee from a specific shift.
     * @param theCaller The user (e.g., manager) requesting the removal.
     * @param theShift The shift from which the employee is being removed.
     */
    void removeEmployeeFromShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException;

    /**
     * Chooses a relevant role to be assigned to a shift.
     * @param theCaller The user (e.g., manager) choosing the relevant role.
     * @param theShift The shift for which the role is being chosen.
     */
    void chooseRelevantRoleForShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException;

    /**
     * Prints the details of a specific shift.
     * @param theCaller The user (e.g., manager) requesting to print the shift details.
     * @param theShift The shift whose details need to be printed.
     */
    void printShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException;

       /**
     * Removes a role from a shift.
     * @param theCaller The user (e.g., manager) requesting the removal of the role.
     * @param theShift The shift from which the role is being removed.
     */
    void removeRoleFromShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException;
}