package HR_Mudol.domain;

/**
 * This interface defines the contract for managing employee shifts within the system.
 * It includes operations such as assigning and removing employees from shifts,
 * choosing relevant roles for shifts, and printing shift details.
 * The primary user of this interface is a manager or a user with the required permissions
 * who can manage shift assignments and roles for employees.
 */
public interface IShiftManager {

    /**
     * Assigns an employee to a specific shift with a particular role.
     * @param caller The user (e.g., manager) requesting the assignment.
     * @param shift The shift the employee is being assigned to.
     * @param employee The employee being assigned to the shift.
     * @param role The role the employee will take on during the shift.
     */
    void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role);

    /**
     * Removes an employee from a specific shift.
     * @param caller The user (e.g., manager) requesting the removal.
     * @param shift The shift from which the employee is being removed.
     */
    void removeEmployeeFromShift(User caller, Shift shift);

    /**
     * Chooses a relevant role to be assigned to a shift.
     * @param caller The user (e.g., manager) choosing the relevant role.
     * @param shift The shift for which the role is being chosen.
     */
    void chooseRelevantRoleForShift(User caller, Shift shift);

    /**
     * Prints the details of a specific shift.
     * @param caller The user (e.g., manager) requesting to print the shift details.
     * @param shift The shift whose details need to be printed.
     */
    void printShift(User caller, Shift shift);

    /**
     * Adds an employee to a shift with a specific role.
     * @param caller The user (e.g., manager) requesting the addition.
     * @param shift The shift the employee is being added to.
     * @param employee The employee being added to the shift.
     * @param role The role the employee will take on during the shift.
     */
    void addEmployeeToShift(User caller, Shift shift, Employee employee, Role role);

    /**
     * Removes a role from a shift.
     * @param caller The user (e.g., manager) requesting the removal of the role.
     * @param shift The shift from which the role is being removed.
     */
    void removeRoleFromShift(User caller, Shift shift);
}