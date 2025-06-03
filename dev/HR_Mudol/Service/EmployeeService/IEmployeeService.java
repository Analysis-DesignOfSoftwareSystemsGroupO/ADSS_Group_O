package HR_Mudol.Service.EmployeeService;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeService {

    WeekDTO getNextWeekDTO();

    WeekDTO getCurrentWeekDTO();

    /**
     * Display all upcoming shifts assigned to the given employee.
     */

    void viewMyShifts(UserDTO caller, long empId) throws SQLException;

    /**
     * Allow the employee to submit a new constraint.
     */
    void submitConstraint(UserDTO caller, long empId, WeekDTO currentWeek) throws SQLException;

    /**
     * Allow the employee to update an existing constraint.
     */
    void updateConstraint(UserDTO caller, long empId, WeekDTO currentWeek);

    /**
     * Display personal details of the employee (e.g., name, role, salary).
     */
    void viewPersonalDetails(UserDTO caller, long employeeId) throws SQLException;

    /**
     * View the list of constraints the employee has submitted for the current week.
     */
    void viewMyConstraints(UserDTO caller, long employeeId);

    /**
     * Allow the employee to change their account password.
     */
    void changePassword(UserDTO caller, long empId) throws SQLException;

    /**
     * Display the employment contract details of the employee.
     */
    void viewContractDetails(UserDTO caller, long empId);

    /**
     * View the roles that the employee is eligible to perform.
     */
    void viewAvailableRoles(UserDTO caller, long employeeId);

    List<EmployeeDTO> getAllEmployees() throws SQLException;

    int getTotalEmployeeCount() throws SQLException;

    public void close();



}
