package HR_Mudol.Service.ManagerService;

import HR_Mudol.DTO.*;
import HR_Mudol.Service.EmployeeService.IEmployeeService;
import HR_Mudol.Service.IReportGenerator;
import HR_Mudol.Service.ReportGenerator;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.Service.TransportService.TransportShiftIntegrator;
import HR_Mudol.domain.Controllers.*;
import HR_Mudol.domain.Objects.Role;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * HRSystemManager is responsible for the overall management of the HR system for a specific branch.
 * It coordinates all operational managers: employees, roles, shifts, weeks, and reports.
 */
public class HRService implements IHRService {

    private BranchDTO branchDTO;
    private IRoleController roleController;
    private IEmployeeController employeeController;
    private IShiftController shiftController;
    private IWeekController weekController;
    private IReportGenerator reportGenerator;
    private IEmployeeService employeeService;

    public HRService(BranchDTO curBranch) throws SQLException {

        this.branchDTO=curBranch;
        this.employeeController = new EmployeeController(curBranch);
        this.roleController = new RoleController(curBranch);
        this.roleController.setEmployeeManager(this.employeeController);
        this.shiftController = new ShiftController(curBranch, this.roleController);
        this.weekController = new WeekController(this.shiftController, curBranch, this.roleController);

        this.employeeService = new EmployeeService(curBranch);
        this.reportGenerator = new ReportGenerator(this.weekController, this.employeeController);
    }

    @Override
    public void close() {
        roleController.close();
    }

    @Override
    public void displayDashboard(UserDTO caller, WeekDTO currentWeek) throws SQLException {
        int totalEmployees = employeeService.getTotalEmployeeCount();
        int withoutRoles = roleController.countEmployeesWithoutRoles(caller, employeeService.getAllEmployees());

        System.out.println(" Week starting " + currentWeek.getConstraintDeadline());
        System.out.println(" " + hasUnassignedRoles(currentWeek) + " shifts are required attention!");

        System.out.println(" Employees Status:");
        System.out.println("- Total employees: " + totalEmployees);
        System.out.println("- Without roles: " + withoutRoles);
    }

    // EmployeeService forwarding:
    @Override
    public UserDTO getUserById(int empId) throws SQLException {
        return employeeController.getUserById(empId);
    }

    @Override
    public boolean isEmployeeInBranch(int empId, int branchId) throws SQLException{
        return employeeController.isEmployeeInBranch(empId,branchId);
    }

    @Override
    public void viewMyShifts(UserDTO caller, int empId, WeekDTO currentWeek) throws SQLException {
        employeeService.viewMyShifts(caller, empId);
    }

    @Override
    public void submitConstraint(UserDTO caller, int empId, WeekDTO currentWeek) throws SQLException {
        employeeService.submitConstraint(caller, empId, currentWeek);
    }

    @Override
    public void updateConstraint(UserDTO caller, int empId, WeekDTO currentWeek) {
        employeeService.updateConstraint(caller, empId, currentWeek);
    }

    @Override
    public void viewPersonalDetails(UserDTO caller, int empId) throws SQLException {
        employeeService.viewPersonalDetails(caller, empId);
    }

    @Override
    public void viewMyConstraints(UserDTO caller, int empId) {
        employeeService.viewMyConstraints(caller, empId);
    }

    @Override
    public void changePassword(UserDTO caller, int empId) throws SQLException {
        employeeService.changePassword(caller, empId);
    }

    @Override
    public void viewContractDetails(UserDTO caller, int empId) {
        employeeService.viewContractDetails(caller, empId);
    }

    @Override
    public void viewAvailableRoles(UserDTO caller, int empId) {
        employeeService.viewAvailableRoles(caller, empId);
    }

    // RoleController forwarding:
    @Override
    public void createRole(UserDTO caller) throws SQLException {
        roleController.createRole(caller);
    }

    @Override
    public void updateRoleDescription(UserDTO caller) throws SQLException {
        roleController.updateRoleDescription(caller);
    }

    @Override
    public void assignEmployeeToRole(UserDTO caller) throws SQLException {
        roleController.assignEmployeeToRole(caller);
    }

    @Override
    public void assignEmployeeToShiftManager(UserDTO caller) throws SQLException {
        roleController.assignEmployeeToShiftManager(caller);
    }

    @Override
    public void removeEmployeeFromALLRoles(UserDTO caller) throws SQLException {
        roleController.removeEmployeeFromALLRoles(caller);
    }


    @Override
    public List<Role> getAllRoles(UserDTO caller) throws SQLException {
        return roleController.getAllRoles(caller);
    }

    @Override
    public void printAllRoles(UserDTO caller) throws SQLException {
        roleController.printAllRoles(caller);
    }

    @Override
    public Role getRoleByNumber(int roleNumber) {
        return roleController.getRoleByNumber(roleNumber);
    }

    @Override
    public int countEmployeesWithoutRoles(UserDTO caller, List<EmployeeDTO> employeeList) throws SQLException {
        return roleController.countEmployeesWithoutRoles(caller, employeeList);
    }

    // ShiftController forwarding
    @Override
    public void assignEmployeeToShift(UserDTO caller, ShiftDTO shift, EmployeeDTO employee, RoleDTO role) throws SQLException {
        shiftController.assignEmployeeToShift(caller, shift, employee, role);
    }

    @Override
    public void removeEmployeeFromShift(UserDTO caller, ShiftDTO shift) throws SQLException {
        shiftController.removeEmployeeFromShift(caller, shift);
    }

    @Override
    public void chooseRelevantRoleForShift(UserDTO caller, ShiftDTO shift) throws SQLException {
        shiftController.chooseRelevantRoleForShift(caller, shift);
    }

    @Override
    public void printShift(UserDTO caller, ShiftDTO shift) throws SQLException {
        shiftController.printShift(caller, shift);
    }

    @Override
    public void addEmployeeToShift(UserDTO caller, ShiftDTO shift, EmployeeDTO employee, RoleDTO role) throws SQLException {
        shiftController.assignEmployeeToShift(caller, shift, employee, role);
    }

    @Override
    public void removeRoleFromShift(UserDTO caller, ShiftDTO shift) throws SQLException {
        shiftController.removeRoleFromShift(caller, shift);
    }

    // WeekController forwarding:
    @Override
    public WeekDTO createNewWeek(UserDTO caller) throws SQLException {
        if (caller.isHRManager())
            return weekController.createNewWeek();
        else {
            System.out.println("Access denied");
            return null;
        }
    }
    @Override
    public WeekDTO getNextWeekDTO() {
        return weekController.getNextWeekDTO();
    }

    @Override
    public WeekDTO getCurrentWeekDTO() {
        return weekController.getCurrentWeekDTO();
    }

    public void addEmployee(UserDTO caller) throws SQLException {
        employeeController.addEmployee(caller);
    }


    @Override
    public void cancelShift(UserDTO caller, WeekDTO week) throws SQLException {
        weekController.cancelShift(caller, week);
    }

    @Override
    public void manageTheWeekRelevantRoles(UserDTO caller, WeekDTO week) throws SQLException {
        weekController.manageTheWeekRelevantRoles(caller,this, week);
    }



    @Override
    public void assigningEmployToShifts(UserDTO caller) throws SQLException {
        weekController.assigningEmployToShifts(caller);
    }

    @Override
    public void printWeek(WeekDTO week) {
        weekController.printWeek(week);
    }

    @Override
    public void removeEmployeeFromShift(UserDTO caller, WeekDTO week) throws SQLException {
        weekController.removeEmployeeFromShift(caller, week);
    }

    @Override
    public void removeRoleFromShift(UserDTO caller, WeekDTO week) throws SQLException {
        weekController.removeRoleFromShift(caller, week);
    }

    @Override
    public void addARoleToShift(UserDTO caller, WeekDTO week) throws SQLException {
        weekController.addARoleToShift(caller, week);
    }

    @Override
    public void addEmployeeToShift(UserDTO caller, WeekDTO week) throws SQLException {
        weekController.addEmployeeToShift(caller, week);
    }

    @Override
    public int hasUnassignedRoles(WeekDTO week) {
        return weekController.hasUnassignedRoles(week);
    }

    @Override
    public List<ShiftDTO> getShiftsForEmployee(EmployeeDTO employee, WeekDTO curWeek) {
        return weekController.getShiftsForEmployee(employee, curWeek);
    }

    // ReportGenerator forwarding
    @Override
    public void generateEmployeeReport(UserDTO caller, int empId, WeekDTO curWeek) throws SQLException {
        reportGenerator.generateEmployeeReport(caller, empId, curWeek);
    }

    @Override
    public void generateWeeklyReport(UserDTO caller, List<WeekDTO> weeks) {
        reportGenerator.generateWeeklyReport(caller, weeks);
    }

    @Override
    public void generateShiftReport(UserDTO caller, WeekDTO curWeek) {
        reportGenerator.generateShiftReport(caller, curWeek);
    }

    @Override
    public IRoleController getRoleController() {
        return roleController;
    }

    @Override
    public void removeEmployeeFromRole(UserDTO theCaller, Scanner sc) throws SQLException {
        roleController.removeEmployeeFromRoleInteractive(theCaller, sc);
    }

    @Override
    public void removeEmployee(UserDTO caller) throws SQLException {
        employeeController.removeEmployee(caller);
    }

    @Override
    public void updateBankAccount(UserDTO caller) throws SQLException {
        employeeController.updateBankAccount(caller);
    }

    @Override
    public void deleteRole(UserDTO theCaller,String des) throws SQLException{
        roleController.deleteRole(theCaller,des);
    }
    @Override
    public void updateSalary(UserDTO caller) throws SQLException {
        employeeController.updateSalary(caller);
    }
    @Override
    public void printAllEmployees(UserDTO caller) throws SQLException {
            employeeController.printAllEmployees(caller);
    }

    @Override
    public void addRoleToShiftIfNeeded(UserDTO caller, ShiftDTO shift, RoleDTO role, int requiredAmount) throws SQLException {
        shiftController.addRoleToShiftIfNeeded(caller, shift, role, requiredAmount);
    }



}