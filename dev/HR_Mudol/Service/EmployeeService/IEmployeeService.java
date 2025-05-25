package HR_Mudol.Service.EmployeeService;

import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

public interface IEmployeeService {

    /**
     * Display all upcoming shifts assigned to the given employee.
     */
    void viewMyShifts(User caller, int empId, Week currentWeek);

    /**
     * Allow the employee to submit a new constraint.
     */
    void submitConstraint(User caller, int empId, Week currentWeek);

    /**
     * Allow the employee to update an existing constraint.
     */
    void updateConstraint(User caller, int empId, Week currentWeek);

    /**
     * Display personal details of the employee (e.g., name, role, salary).
     */
    void viewPersonalDetails(User caller, int empId);

    /**
     * View the list of constraints the employee has submitted for the current week.
     */
    void viewMyConstraints(User caller, int empId);

    /**
     * Allow the employee to change their account password.
     */
    void changePassword(User caller, int empId);

    /**
     * Display the employment contract details of the employee.
     */
    void viewContractDetails(User caller, int empId);

    /**
     * View the roles that the employee is eligible to perform.
     */
    void viewAvailableRoles(User caller, int empId);
}
