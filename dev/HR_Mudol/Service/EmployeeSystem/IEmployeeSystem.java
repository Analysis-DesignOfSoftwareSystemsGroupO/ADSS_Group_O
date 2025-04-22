package HR_Mudol.Service.EmployeeSystem;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

public interface IEmployeeSystem {

    /**
     * Display all upcoming shifts assigned to the given employee.
     */
    public  void viewMyShifts(User caller, Employee self, Week currentWeek);
    /**
     * Allow the employee to submit a new constraint.
     */
    void submitConstraint(User caller, Employee self);

    /**
     * Allow the employee to update an existing constraint.
     */
    void updateConstraint(User caller, Employee self, Week currentWeek);


    /**
     * Display personal details of the employee (e.g., name, role, salary).
     */
    void viewPersonalDetails(User caller, Employee self);

    /**
     * View the list of constraints the employee has submitted for the current week.
     */
    void viewMyConstraints(User caller, Employee self);

    /**
     * Allow the employee to change their account password.
     */
    void changePassword(User caller, Employee self);

    /**
     * Display the employment contract details of the employee.
     */
    void viewContractDetails(User caller, Employee self);

    /**
     * View the roles that the employee is eligible to perform.
     */
    void viewAvailableRoles(User caller, Employee self);
}
