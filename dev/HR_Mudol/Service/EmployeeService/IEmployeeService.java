package HR_Mudol.Service.EmployeeService;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IEmployeeService {

    /**
     * Display all upcoming shifts assigned to the given employee.
     */

    void viewMyShifts(UserDTO caller, int empId) throws SQLException;

    /**
     * Allow the employee to submit a new constraint.
     */
    void submitConstraint(UserDTO caller, int empId, WeekDTO currentWeek) throws SQLException;

    /**
     * Allow the employee to update an existing constraint.
     */
    void updateConstraint(UserDTO caller, int empId, WeekDTO currentWeek);

    /**
     * Display personal details of the employee (e.g., name, role, salary).
     */
    void viewPersonalDetails(UserDTO caller, int empId) throws SQLException;

    /**
     * View the list of constraints the employee has submitted for the current week.
     */
    void viewMyConstraints(UserDTO caller, int empId);

    /**
     * Allow the employee to change their account password.
     */
    void changePassword(UserDTO caller, int empId) throws SQLException;

    /**
     * Display the employment contract details of the employee.
     */
    void viewContractDetails(UserDTO caller, int empId);

    /**
     * View the roles that the employee is eligible to perform.
     */
    void viewAvailableRoles(UserDTO caller, int empId);

    List<EmployeeDTO> getAllEmployees() throws SQLException;

    int getTotalEmployeeCount() throws SQLException;

    public void close();



}
