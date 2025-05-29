package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Shift;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.Objects.Week;

import java.sql.SQLException;
import java.util.List;
/**
 * The IWeekManager interface defines the operations for managing a theWeek's shifts in the system.
 * These operations include creating new theWeeks, managing roles, assigning employees, and printing details of the theWeek.
 */
public interface IWeekController {

    /**
     * Creates instances of new shifts for each day of the theWeek (Sunday to Friday).
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @return The newly created Week object containing all shifts for the theWeek.
     */
    Week createNewWeek();

    /**
     * Cancels a shift for the current theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is canceling the shift.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void cancelShift(UserDTO theCaller, WeekDTO theWeek);

    /**
     * Allows the user to choose relevant roles for each shift within the given theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is choosing the roles for shifts.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void manageTheWeekRelevantRoles(UserDTO theCaller, WeekDTO theWeek) throws SQLException;

    /**
     * Assigns employees to the shifts for the given theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is assigning employees.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void assigningEmployToShifts(UserDTO theCaller, WeekDTO theWeek);

    /**
     * Prints the details of the theWeek, including shifts and roles.
     * The user performing this action must be authorized to view the theWeek's details.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void printWeek(WeekDTO theWeek);

    /**
     * Retrieves the shifts assigned to a specific employee for the given theWeek.
     * @param theEmployee The employee whose shifts are being retrieved.
     * @param theWeek The current theWeek object.
     * @return A list of shifts assigned to the employee for the current theWeek.
     */
    List<Shift> getShiftsForEmployee(EmployeeDTO theEmployee, WeekDTO theWeek);

    /**
     * Checks if there are any unassigned roles for the given theWeek.
     * @param theWeek The theWeek object representing the current theWeek.
     * @return An integer representing the number of unassigned roles in the theWeek.
     */
    int hasUnassignedRoles(WeekDTO theWeek);

    /**
     * Removes an employee from their assigned shift in the current theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is removing the employee.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void removeEmployeeFromShift(UserDTO theCaller, WeekDTO theWeek);

    /**
     * Removes a role from a shift in the current theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is removing the role.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void removeRoleFromShift(UserDTO theCaller, WeekDTO theWeek);

    /**
     * Adds a role to a shift in the current theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is adding the role.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void addARoleToShift(UserDTO theCaller, WeekDTO theWeek);

    /**
     * Adds an employee to a shift in the current theWeek.
     * The user performing this action must be authorized to manage the theWeek's shifts.
     * @param theCaller The user (manager or shift manager) who is adding the employee.
     * @param theWeek The theWeek object representing the current theWeek.
     */
    void addEmployeeToShift(UserDTO theCaller, WeekDTO theWeek);
}