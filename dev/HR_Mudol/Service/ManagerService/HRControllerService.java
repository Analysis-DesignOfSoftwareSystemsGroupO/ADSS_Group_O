package HR_Mudol.Service.ManagerService;

import HR_Mudol.domain.*;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import java.util.List;

/**
 * HRSystemManager is responsible for the overall management of the HR system for a specific branch.
 * It coordinates all operational managers: employees, roles, shifts, weeks, and reports.
 */
public class HRControllerService implements IHRControllerService {

    private Branch curBranch;
    private RoleController roleController;
    private EmployeeController employeeController;
    private ShiftController shiftController;
    private WeekController weekController;
    private ReportGenerator reportGenerator;
    private EmployeeService employeeService;

    public HRControllerService(Branch curBranch){
        this.curBranch = curBranch;
        this.roleController = new RoleController(curBranch);
        this.employeeController = new EmployeeController(curBranch);
        this.shiftController = new ShiftController(this.roleController);
        this.weekController = new WeekController(shiftController, curBranch);
        this.employeeService = new EmployeeService(curBranch.getEmployeeRepo());
        this.reportGenerator = new ReportGenerator(weekController, employeeController);
        roleController.setEmployeeManager(employeeController);
        employeeController.setRoleManager(roleController);
    }

    @Override
    public void displayDashboard(User caller, Branch curBranch) {
        Week currentWeek = curBranch.getWeekRepo().getAll().getLast();
        System.out.println("\uD83D\uDCC5 Week starting " + currentWeek.getConstraintDeadline());
        System.out.println("\uD83D\uDEA8" + weekController.hasUnassignedRoles(currentWeek) + " shifts are required attention!");
        System.out.println(currentWeek);

        System.out.println("\n\uD83D\uDC65 Employees Status:");
        System.out.println("- Total employees: " + curBranch.getEmployeeRepo().getAll().size());
        System.out.println("- Without roles: " + roleController.countEmployeesWithoutRoles(caller, curBranch.getEmployeeRepo().getAll()));
    }

    // EmployeeService forwarding
    @Override
    public void viewMyShifts(User caller, int empId, Week currentWeek) {
        employeeService.viewMyShifts(caller, empId, currentWeek);
    }

    @Override
    public void submitConstraint(User caller, int empId, Week currentWeek) {
        employeeService.submitConstraint(caller, empId, currentWeek);
    }

    @Override
    public void updateConstraint(User caller, int empId, Week currentWeek) {
        employeeService.updateConstraint(caller, empId, currentWeek);
    }

    @Override
    public void viewPersonalDetails(User caller, int empId) {
        employeeService.viewPersonalDetails(caller, empId);
    }

    @Override
    public void viewMyConstraints(User caller, int empId) {
        employeeService.viewMyConstraints(caller, empId);
    }

    @Override
    public void changePassword(User caller, int empId) {
        employeeService.changePassword(caller, empId);
    }

    @Override
    public void viewContractDetails(User caller, int empId) {
        employeeService.viewContractDetails(caller, empId);
    }

    @Override
    public void viewAvailableRoles(User caller, int empId) {
        employeeService.viewAvailableRoles(caller, empId);
    }

    // RoleController forwarding
    @Override public void createRole(User caller) { roleController.createRole(caller); }
    @Override public void updateRoleDescription(User caller) { roleController.updateRoleDescription(caller); }
    @Override public void assignEmployeeToRole(User caller) { roleController.assignEmployeeToRole(caller); }
    @Override public void assignEmployeeToShiftManager(User caller) { roleController.assignEmployeeToShiftManager(caller); }
    @Override public void removeEmployeeFromRole(User caller) { roleController.removeEmployeeFromRole(caller); }
    @Override public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) { roleController.removeEmployeeFromRole(caller, roleNumber, e); }
    @Override public List<Employee> getRelevantEmployees(User caller) { return roleController.getRelevantEmployees(caller); }
    @Override public List<Role> getAllRoles(User caller) { return roleController.getAllRoles(caller); }
    @Override public void printAllRoles(User caller) { roleController.printAllRoles(caller); }
    @Override public Role getRoleByNumber(int roleNumber) { return roleController.getRoleByNumber(roleNumber); }
    @Override public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) { return roleController.countEmployeesWithoutRoles(caller, employeeList); }

    // ShiftController forwarding
    @Override public void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role) { shiftController.assignEmployeeToShift(caller, shift, employee, role); }
    @Override public void removeEmployeeFromShift(User caller, Shift shift) { shiftController.removeEmployeeFromShift(caller, shift); }
    @Override public void chooseRelevantRoleForShift(User caller, Shift shift) { shiftController.chooseRelevantRoleForShift(caller, shift); }
    @Override public void printShift(User caller, Shift shift) { shiftController.printShift(caller, shift); }
    @Override public void addEmployeeToShift(User caller, Shift shift, Employee employee, Role role) { shiftController.addEmployeeToShift(caller, shift, employee, role); }
    @Override public void removeRoleFromShift(User caller, Shift shift) { shiftController.removeRoleFromShift(caller, shift); }

    // WeekController forwarding
    @Override public Week createNewWeek(User caller) { return weekController.createNewWeek(caller); }
    @Override public void cancelShift(User caller, Week week) { weekController.cancelShift(caller, week); }
    @Override public void manageTheWeekRelevantRoles(User caller, Week week) { weekController.manageTheWeekRelevantRoles(caller, week); }
    @Override public void assigningEmployToShifts(User caller, Week week) { weekController.assigningEmployToShifts(caller, week); }
    @Override public void printWeek(Week week) { weekController.printWeek(week); }
    @Override public void removeEmployeeFromShift(User caller, Week week) { weekController.removeEmployeeFromShift(caller, week); }
    @Override public void removeRoleFromShift(User caller, Week week) { weekController.removeRoleFromShift(caller, week); }
    @Override public void addARoleToShift(User caller, Week week) { weekController.addARoleToShift(caller, week); }
    @Override public void addEmployeeToShift(User caller, Week week) { weekController.addEmployeeToShift(caller, week); }
    @Override public int hasUnassignedRoles(Week week) { return weekController.hasUnassignedRoles(week); }
    @Override public List<Shift> getShiftsForEmployee(Employee employee, Week curWeek) { return weekController.getShiftsForEmployee(employee, curWeek); }

    // ReportGenerator forwarding
    @Override public void generateWeeklyReport(User caller, List<Week> weeks) { reportGenerator.generateWeeklyReport(caller, weeks); }
    @Override public void generateEmployeeReport(User caller, int empId, Week curWeek) { reportGenerator.generateEmployeeReport(caller, empId, curWeek); }
    @Override public void generateShiftReport(User caller, Week curWeek) { reportGenerator.generateShiftReport(caller, curWeek); }

    @Override public Branch getBranch() { return this.curBranch; }
    @Override public IRoleController getRoleManager() { return this.roleController; }
}
