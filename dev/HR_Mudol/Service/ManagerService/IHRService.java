package HR_Mudol.Service.ManagerService;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.EmployeeController;
import HR_Mudol.domain.Controllers.IRoleController;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.Week;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * IHRSystemManager is an interface that defines the core functionality required to manage the HR system for a given branch.
 * This includes managing employees, roles, shifts, weeks, and generating reports.
 * The interface extends multiple other manager interfaces for employee, role, shift, week, and report generation functionality.
 */
public interface IHRService {

    int getBranchID();

    void displayDashboard(UserDTO caller, WeekDTO currentWeek) throws SQLException;

    // EmployeeService forwarding:
    UserDTO getUserById(int empId) throws SQLException;

    // EmployeeService forwarding:
    boolean isEmployeeInBranch(int empId, int branchId) throws SQLException;

    // EmployeeService forwarding
    void viewMyShifts(UserDTO caller, int empId, WeekDTO currentWeek) throws SQLException;

    void submitConstraint(UserDTO caller, int empId, WeekDTO currentWeek) throws SQLException;

    void updateConstraint(UserDTO caller, int empId, WeekDTO currentWeek);

    void viewPersonalDetails(UserDTO caller, int empId) throws SQLException;

    void viewMyConstraints(UserDTO caller, int empId);

    void changePassword(UserDTO caller, int empId) throws SQLException;

    void viewContractDetails(UserDTO caller, int empId);

    void viewAvailableRoles(UserDTO caller, int empId);

    // RoleController forwarding
    void createRole(UserDTO caller) throws SQLException;

    void insertNewRole(String description) throws SQLException;

    void updateRoleDescription(UserDTO caller) throws SQLException;

    void assignEmployeeToRole(UserDTO caller) throws SQLException;

    void assignEmployeeToShiftManager(UserDTO caller) throws SQLException;

    void removeEmployeeFromALLRoles(UserDTO caller) throws SQLException;


    //List<Employee> getRelevantEmployees(UserDTO caller) throws SQLException;

    List<Role> getAllRoles(UserDTO caller) throws SQLException;

    void printAllRoles(UserDTO caller) throws SQLException;

    Role getRoleByNumber(int roleNumber);

    int countEmployeesWithoutRoles(UserDTO caller, List<EmployeeDTO> employees) throws SQLException;

    // ShiftController forwarding
    void assignEmployeeToShift(UserDTO caller, ShiftDTO shift, EmployeeDTO employee, RoleDTO role) throws SQLException;

    void removeEmployeeFromShift(UserDTO caller, ShiftDTO shift) throws SQLException;

    void chooseRelevantRoleForShift(UserDTO caller, ShiftDTO shift) throws SQLException;

    void printShift(UserDTO caller, ShiftDTO shift) throws SQLException;

    void addEmployeeToShift(UserDTO caller, ShiftDTO shift, EmployeeDTO employee, RoleDTO role) throws SQLException;

    void removeRoleFromShift(UserDTO caller, ShiftDTO shift) throws SQLException;

    // WeekController forwarding
    WeekDTO createNewWeek(UserDTO caller) throws SQLException;

    WeekDTO getNextWeekDTO();

    WeekDTO getCurrentWeekDTO();

    void cancelShift(UserDTO caller, WeekDTO week) throws SQLException;

    void assigningEmployToShifts(UserDTO caller) throws SQLException;

    List<ShiftDTO> getShiftsInDateRange(BranchDTO branchDTO,LocalDate startDate, LocalDate endDate) throws SQLException;

    void printWeek(WeekDTO week);

    void removeEmployeeFromShift(UserDTO caller, WeekDTO week) throws SQLException;

    void removeRoleFromShift(UserDTO caller, WeekDTO week) throws SQLException;

    void addARoleToShift(UserDTO caller, WeekDTO week) throws SQLException;

    void addEmployeeToShift(UserDTO caller, WeekDTO week) throws SQLException;

    int hasUnassignedRoles(WeekDTO week);

    List<ShiftDTO> getShiftsForEmployee(EmployeeDTO employee, WeekDTO curWeek);

    void generateReports(UserDTO caller, BranchDTO branch, String reportType);

    // ReportGenerator forwarding
    void generateEmployeeReport(UserDTO caller, int empId, WeekDTO curWeek) throws SQLException;

    void generateWeeklyReport(UserDTO caller, List<WeekDTO> weeks);

    void generateShiftReport(UserDTO caller, WeekDTO curWeek);

     void close();

    void manageTheWeekRelevantRoles(UserDTO caller, WeekDTO weekDTO) throws SQLException;

    IRoleController getRoleController();

    void removeEmployeeFromRole(UserDTO theCaller, Scanner sc) throws SQLException;

    void removeEmployee(UserDTO caller) throws SQLException;

    void updateBankAccount(UserDTO caller) throws SQLException;

    void deleteRole(UserDTO theCaller, String des) throws SQLException;

    void updateSalary(UserDTO caller) throws SQLException;

    void printAllEmployees(UserDTO caller) throws SQLException;

    void addRoleToShiftIfNeeded(UserDTO caller, ShiftDTO shift, RoleDTO role, int requiredAmount) throws SQLException;

    String getBranchOfShift(ShiftDTO shiftDTO);
}
