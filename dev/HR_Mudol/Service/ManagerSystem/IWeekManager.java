package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Shift;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

import java.util.List;
/**
 * The IWeekManager interface defines the operations for managing a week's shifts in the system.
 * These operations include creating new weeks, managing roles, assigning employees, and printing details of the week.
 */
public interface IWeekManager {

    /**
     * Creates instances of new shifts for each day of the week (Sunday to Friday).
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is creating the new week.
     * @return The newly created Week object containing all shifts for the week.
     */
    Week createNewWeek(User caller);

    /**
     * Cancels a shift for the current week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is canceling the shift.
     * @param week The week object representing the current week.
     */
    void cancelShift(User caller, Week week);

    /**
     * Allows the user to choose relevant roles for each shift within the given week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is choosing the roles for shifts.
     * @param week The week object representing the current week.
     */
    void manageTheWeekRelevantRoles(User caller, Week week);

    /**
     * Assigns employees to the shifts for the given week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is assigning employees.
     * @param week The week object representing the current week.
     */
    void assigningEmployToShifts(User caller, Week week);

    /**
     * Prints the details of the week, including shifts and roles.
     * The user performing this action must be authorized to view the week's details.
     * @param week The week object representing the current week.
     */
    void printWeek(Week week);

    /**
     * Retrieves the shifts assigned to a specific employee for the given week.
     * @param employee The employee whose shifts are being retrieved.
     * @param curWeek The current week object.
     * @return A list of shifts assigned to the employee for the current week.
     */
    List<Shift> getShiftsForEmployee(Employee employee, Week curWeek);

    /**
     * Checks if there are any unassigned roles for the given week.
     * @param week The week object representing the current week.
     * @return An integer representing the number of unassigned roles in the week.
     */
    int hasUnassignedRoles(Week week);

    /**
     * Removes an employee from their assigned shift in the current week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is removing the employee.
     * @param week The week object representing the current week.
     */
    void removeEmployeeFromShift(User caller, Week week);

    /**
     * Removes a role from a shift in the current week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is removing the role.
     * @param week The week object representing the current week.
     */
    void removeRoleFromShift(User caller, Week week);

    /**
     * Adds a role to a shift in the current week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is adding the role.
     * @param week The week object representing the current week.
     */
    void addARoleToShift(User caller, Week week);

    /**
     * Adds an employee to a shift in the current week.
     * The user performing this action must be authorized to manage the week's shifts.
     * @param caller The user (manager or shift manager) who is adding the employee.
     * @param week The week object representing the current week.
     */
    void addEmployeeToShift(User caller, Week week);
}