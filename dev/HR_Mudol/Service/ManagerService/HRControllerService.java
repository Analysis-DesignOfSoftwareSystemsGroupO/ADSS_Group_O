package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

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

    /**
     * Constructs a new HRSystemManager for a given branch.
     *
     * @param curBranch The branch the system will manage.
     */
    public HRControllerService(Branch curBranch){
        this.curBranch = curBranch;
        this.roleController = new RoleController(curBranch);
        this.employeeController = new EmployeeController(curBranch);
        this.shiftController =new ShiftController(this.roleController);
        this.weekController =new WeekController(shiftController,curBranch);
        this.reportGenerator=new ReportGenerator(weekController, employeeController);
        roleController.setEmployeeManager(employeeController);
        employeeController.setRoleManager(roleController);
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
        System.out.println("🚨" + weekController.hasUnassignedRoles(currentWeek)+ " shifts are required attention!");
        System.out.println(curBranch.getWeeks().getLast()); //print the shifts of the week

        System.out.println("\n👥 Employees Status:");
        System.out.println("- Total employees: " + curBranch.getEmployees().size());
        System.out.println("- Without roles: " + roleController.countEmployeesWithoutRoles(caller,curBranch.getEmployees()));
    }

    // ----- Employee Management Methods -----
    /** {@inheritDoc} */
    @Override
    public void addEmployee(User caller) {
        employeeController.addEmployee(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployee(User caller) {
        employeeController.removeEmployee(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateBankAccount(User caller) {
        employeeController.updateBankAccount(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateSalary(User caller) {
        employeeController.updateSalary(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMinDayShift(User caller) {
        employeeController.updateMinDayShift(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateMinEveningShift(User caller) {
        employeeController.updateMinEveningShift(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void setInitialsickDays(User caller) {
        employeeController.setInitialsickDays(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void setInitialdaysOff(User caller) {
        employeeController.setInitialdaysOff(caller);
    }

    /** {@inheritDoc} */
    @Override
    public Employee getEmployeeById(User caller, int ID) {
        return employeeController.getEmployeeById(caller,ID);
    }

    /** {@inheritDoc} */
    @Override
    public void printEmployees(User caller) {
        employeeController.printEmployees(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void printAllEmployees(User caller) {
        employeeController.printAllEmployees(caller);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> getAllUsers(User caller) {
        return employeeController.getAllUsers(caller);
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
        roleController.createRole(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void updateRoleDescription(User caller) {
        roleController.updateRoleDescription(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void assignEmployeeToRole(User caller) {
        roleController.assignEmployeeToRole(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void assignEmployeeToShiftManager(User caller) {
        roleController.assignEmployeeToShiftManager(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromRole(User caller) {
        roleController.removeEmployeeFromRole(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        roleController.removeEmployeeFromRole(caller,roleNumber,e);
    }

    /** {@inheritDoc} */
    @Override
    public List<Employee> getRelevantEmployees(User caller) {
       return roleController.getRelevantEmployees(caller);
    }

    /** {@inheritDoc} */
    @Override
    public List<Role> getAllRoles(User caller) {
        return roleController.getAllRoles(caller);
    }

    /** {@inheritDoc} */
    @Override
    public void printAllRoles(User caller) {
        roleController.printAllRoles(caller);
    }

    /** {@inheritDoc} */
    @Override
    public Role getRoleByNumber(int roleNumber) {
        return roleController.getRoleByNumber(roleNumber);
    }

    /** {@inheritDoc} */
    @Override
    public int countEmployeesWithoutRoles(User caller, List<Employee> employeeList) {
        return roleController.countEmployeesWithoutRoles(caller,employeeList);
    }

    // ----- Shift Management Methods -----

    /** {@inheritDoc} */
    @Override
    public void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {
        shiftController.assignEmployeeToShift(caller,shift,employee, role);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployeeFromShift(User caller, Shift shift) {
        shiftController.removeEmployeeFromShift(caller,shift);
    }

    /** {@inheritDoc} */
    @Override
    public void chooseRelevantRoleForShift(User caller, Shift shift) {
        shiftController.chooseRelevantRoleForShift(caller,shift);
    }

    /** {@inheritDoc} */
    @Override
    public void printShift(User caller, Shift shift) {
        shiftController.printShift(caller,shift);
    }

    // ----- Week Management Methods -----

    /** {@inheritDoc} */
    @Override
    public Week createNewWeek(User caller) {
        Week week= weekController.createNewWeek(caller);
        return week;
    }

    /** {@inheritDoc} */
    @Override
    public void cancelShift(User caller, Week week) {
        weekController.cancelShift(caller,week);

    }

    /** {@inheritDoc} */
    @Override
    public void manageTheWeekRelevantRoles(User caller, Week week) {
        weekController. manageTheWeekRelevantRoles(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void assigningEmployToShifts(User caller, Week week) {
        weekController.assigningEmployToShifts(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void printWeek(Week week) {
        weekController.printWeek(week);
    }

    // ----- Utility Methods -----

    /** {@inheritDoc} */
    @Override
    public Branch getBranch() {
        return this.curBranch;
    }

    /** {@inheritDoc} */
    @Override
    public IRoleController getRoleManager() {
        return this.roleController;
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
        weekController.removeEmployeeFromShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void removeRoleFromShift(User caller, Week week) {
        weekController.removeRoleFromShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void addARoleToShift(User caller, Week week) {
        weekController.addARoleToShift(caller,week);
    }

    /** {@inheritDoc} */
    @Override
    public void addEmployeeToShift(User caller, Week week) {
        weekController.addEmployeeToShift(caller,week);
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
