package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;

/**
 * HRSystemManager is responsible for the overall management of the HR system for a specific branch.
 * It coordinates all operational managers: employees, roles, shifts, weeks, and reports.
 */
public class HRSystemManager implements IHRSystemManager {

    private Branch curBranch;
    private RoleManager roleManager;
    private EmployeeManager employeeManager;
    private ShiftManager shiftManager;
    private WeekManager weekManager;
    private ReportGenerator reportGenerator;

    /**
     * Constructs a new HRSystemManager for a given branch.
     *
     * @param curBranch The branch the system will manage.
     */
    public HRSystemManager(Branch curBranch){
        this.curBranch = curBranch;
        this.roleManager = new RoleManager(curBranch);
        this.employeeManager = new EmployeeManager(curBranch);
        this.shiftManager=new ShiftManager(this.roleManager);
        this.weekManager=new WeekManager(shiftManager,curBranch);
        this.reportGenerator=new ReportGenerator(weekManager,employeeManager);
        roleManager.setEmployeeManager(employeeManager);
        employeeManager.setRoleManager(roleManager);
    }

    /**
     * Constructs a new HRSystemManager for a given branch.
     *
     * @param curBranch The branch the system will manage.
     */
    @Override
    public void displayDashboard(User caller, Branch curBranch) {
        Week currentWeek = curBranch.getWeeks().getLast();
        System.out.println("📅 Week starting " + currentWeek.getConstraintDeadline());
        System.out.println("🚨" + weekManager.hasUnassignedRoles(currentWeek)+ " shifts are required attention!");
        System.out.println(curBranch.getWeeks().getLast()); //print the shifts of the week

        System.out.println("\n👥 Employees Status:");
        System.out.println("- Total employees: " + curBranch.getEmployees().size());
        System.out.println("- Without roles: " + roleManager.countEmployeesWithoutRoles(caller,curBranch.getEmployees()));
    }

    // ----- Employee Management Methods -----
    /** {@inheritDoc} */
    @Override
    public void addEmployee(User caller) {
        employeeManager.addEmployee(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployee(User caller) {
        employeeManager.removeEmployee(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateBankAccount(User caller) {
        employeeManager.updateBankAccount(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateSalary(User caller) {
        employeeManager.updateSalary(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMinDayShift(User caller) {
        employeeManager.updateMinDayShift(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMinEveningShift(User caller) {
        employeeManager.updateMinEveningShift(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void setInitialsickDays(User caller) {
        employeeManager.setInitialsickDays(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void setInitialdaysOff(User caller) {
        employeeManager.setInitialdaysOff(caller);
    }

    /** {@inheritDoc} */
    @Override
    public Employee getEmployeeById(User caller, int ID) {
        return employeeManager.getEmployeeById(caller,ID);
    }

    /** {@inheritDoc} */
    @Override
    public void printEmployees(User caller) {
        employeeManager.printEmployees(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void printAllEmployees(User caller) {
        employeeManager.printAllEmployees(caller);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> getAllUsers(User caller) {
        return employeeManager.getAllUsers(caller);
    }

    // ----- Report Generation Methods -----

    /** {@inheritDoc} */
    @Override
    public void generateWeeklyReport(User caller, List<Week> weeks) {
        reportGenerator.generateWeeklyReport(caller,weeks);
    }

    /** {@inheritDoc} */
    @Override
    public void generateEmployeeReport(User caller, int empId, Week curWeek) {
        reportGenerator.generateEmployeeReport(caller, empId, curWeek);
    }

    /** {@inheritDoc} */
    @Override
    public void generateShiftReport(User caller, Week curWeek)  {
        reportGenerator.generateShiftReport(caller,curWeek);
    }

    // ----- Role Management Methods -----

    /** {@inheritDoc} */
    @Override
    public void createRole(User caller) {
        roleManager.createRole(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateRoleDescription(User caller) {
        roleManager.updateRoleDescription(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void assignEmployeeToRole(User caller) {
        roleManager.assignEmployeeToRole(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void assignEmployeeToShiftManager(User caller) {
        roleManager.assignEmployeeToShiftManager(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromRole(User caller) {
        roleManager.removeEmployeeFromRole(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        roleManager.removeEmployeeFromRole(caller,roleNumber,e);
    }

    /** {@inheritDoc} */
    @Override
    public List<Employee> getRelevantEmployees(User caller) {
       return roleManager.getRelevantEmployees(caller);
    }

    /** {@inheritDoc} */
    @Override
    public List<Role> getAllRoles(User caller) {
        return roleManager.getAllRoles(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void printAllRoles(User caller) {
        roleManager.printAllRoles(caller);
    }

    /** {@inheritDoc} */
    @Override
    public Role getRoleByNumber(int roleNumber) {
        return roleManager.getRoleByNumber(roleNumber);
    }

    /** {@inheritDoc} */
    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        return roleManager.countEmployeesWithoutRoles(caller,employeeList);
    }

    // ----- Shift Management Methods -----

    /** {@inheritDoc} */
    @Override
    public void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {
        shiftManager.assignEmployeeToShift(caller,shift,employee, role);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromShift(User caller, Shift shift) {
        shiftManager.removeEmployeeFromShift(caller,shift);
    }

    /** {@inheritDoc} */
    @Override
    public void chooseRelevantRoleForShift(User caller, Shift shift) {
        shiftManager.chooseRelevantRoleForShift(caller,shift);
    }

    /** {@inheritDoc} */
    @Override
    public void printShift(User caller, Shift shift) {
        shiftManager.printShift(caller,shift);
    }

    // ----- Week Management Methods -----

    /** {@inheritDoc} */
    @Override
    public Week createNewWeek(User caller) {
        Week week=weekManager.createNewWeek(caller);
        return week;
    }

    /** {@inheritDoc} */
    @Override
    public void cancelShift(User caller, Week week) {
        weekManager.cancelShift(caller,week);

    }

    /** {@inheritDoc} */
    @Override
    public void manageTheWeekRelevantRoles(User caller, Week week) {
        weekManager. manageTheWeekRelevantRoles(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void assigningEmployToShifts(User caller, Week week) {
        weekManager.assigningEmployToShifts(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void printWeek(Week week) {
        weekManager.printWeek(week);
    }

    // ----- Utility Methods -----

    /** {@inheritDoc} */
    @Override
    public Branch getBranch() {
        return this.curBranch;
    }

    /** {@inheritDoc} */
    @Override
    public IRoleManager getRoleManager() {
        return this.roleManager;
    }

    /** {@inheritDoc} */
    @Override
    public List<Shift> getShiftsForEmployee(Employee employee, Week curWeek) {
        return List.of();
    }

    /** {@inheritDoc} */
    @Override
    public int hasUnassignedRoles(Week week) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromShift(User caller, Week week) {
        weekManager.removeEmployeeFromShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void removeRoleFromShift(User caller, Week week) {
        weekManager.removeRoleFromShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void addARoleToShift(User caller, Week week) {
        weekManager.addARoleToShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void addEmployeeToShift(User caller, Week week) {
        weekManager.addEmployeeToShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void addEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {

    }

    /** {@inheritDoc} */
    @Override
    public void removeRoleFromShift(User caller, Shift shift) {

    }

}
