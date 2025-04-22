package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.LinkedList;
import java.util.List;

public class HRSystemManager implements IHRSystemManager {


    private List<Week> weeksHistory;
    private RoleManager roleManager;
    private EmployeeManager employeeManager;
    private ShiftManager shiftManager;
    private WeekManager weekManager;
    private ReportGenerator reportGenerator;

    public HRSystemManager(){

        this.weeksHistory= new LinkedList<Week>();
        this.roleManager = new RoleManager();
        this.employeeManager = new EmployeeManager();
        this.shiftManager=new ShiftManager(this.roleManager);
        this.weekManager=new WeekManager(shiftManager);
        this.reportGenerator=new ReportGenerator();
        roleManager.setEmployeeManager(employeeManager);
        employeeManager.setRoleManager(roleManager);
    }

    @Override
    public void displayDashboard(User caller) {

    }

    @Override
    public void addEmployee(User caller) {
        employeeManager.addEmployee(caller);
    }

    @Override
    public void removeEmployee(User caller) {
        employeeManager.removeEmployee(caller);
    }

    @Override
    public void updateBankAccount(User caller) {
        employeeManager.updateBankAccount(caller);
    }

    @Override
    public void updateSalary(User caller) {
        employeeManager.updateSalary(caller);
    }

    @Override
    public void updateMinDayShift(User caller) {
        employeeManager.updateMinDayShift(caller);
    }

    @Override
    public void updateMinEveningShift(User caller) {
        employeeManager.updateMinEveningShift(caller);
    }

    @Override
    public void setInitialsickDays(User caller) {
        employeeManager.setInitialsickDays(caller);
    }

    @Override
    public void setInitialdaysOff(User caller) {
        employeeManager.setInitialdaysOff(caller);
    }

    @Override
    public Employee getEmployeeById(User caller, int ID) {
        return employeeManager.getEmployeeById(caller,ID);
    }

    @Override
    public void printEmployees(User caller) {
        employeeManager.printEmployees(caller);
    }

    @Override
    public void printAllEmployees(User caller) {
        employeeManager.printAllEmployees(caller);
    }

    @Override
    public List<User> getAllUsers(User caller) {
        return employeeManager.getAllUsers(caller);
    }

    @Override
    public void generateWeeklyReport(User caller, int weekId) {

    }

    @Override
    public void generateEmployeeReport(User caller, int empId) {

    }

    @Override
    public void generateShiftReport(User caller, int shiftId) {

    }

    @Override
    public void generateCustomReport(User caller, String filter) {

    }

    @Override
    public void createRole(User caller) {
        roleManager.createRole(caller);
    }

    @Override
    public void updateRoleDescription(User caller) {
        roleManager.updateRoleDescription(caller);
    }

    @Override
    public void assignEmployeeToRole(User caller) {
        roleManager.assignEmployeeToRole(caller);
    }

    @Override
    public void assignEmployeeToShiftManager(User caller) {
        roleManager.assignEmployeeToShiftManager(caller);
    }

    @Override
    public void removeEmployeeFromRole(User caller) {
        roleManager.removeEmployeeFromRole(caller);
    }

    @Override
    public void removeEmployeeFromRole(User caller, int roleNumber, Employee e) {
        roleManager.removeEmployeeFromRole(caller,roleNumber,e);
    }

    @Override
    public List<Employee> getRelevantEmployees(User caller) {
       return roleManager.getRelevantEmployees(caller);
    }

    @Override
    public List<Role> getAllRoles(User caller) {
        return roleManager.getAllRoles(caller);
    }

    @Override
    public void printAllRoles(User caller) {
        roleManager.printAllRoles(caller);
    }

    @Override
    public Role getRoleByNumber(int roleNumber) {
        return roleManager.getRoleByNumber(roleNumber);
    }

    @Override
    public void assignEmployeeToShift(User caller, Shift shift, Employee employee) {
        shiftManager.assignEmployeeToShift(caller,shift,employee);
    }

    @Override
    public void removeEmployeeFromShift(User caller, Shift shift, Employee employee) {
        shiftManager.removeEmployeeFromShift(caller,shift,employee);
    }

    @Override
    public void chooseRelevantRoleForShift(User caller, Shift shift) {
        shiftManager.chooseRelevantRoleForShift(caller,shift);
    }

    @Override
    public void printShift(User caller, Shift shift) {
        shiftManager.printShift(caller,shift);
    }

    @Override
    public Week createNewWeek(User caller) {
        Week week=weekManager.createNewWeek(caller);
        weeksHistory.addLast(weekManager.createNewWeek(caller));
        return week;
    }

    @Override
    public void cancelShift(User caller, Week week) {
        weekManager.cancelShift(caller,week);

    }

    @Override
    public void manageTheWeekRelevantRoles(User caller, Week week) {
        weekManager.manageTheWeekRelevantRoles(caller,week);
    }

    @Override
    public void assigningEmployToShifts(User caller, Week week) {
        weekManager.assigningEmployToShifts(caller,week);
    }

    @Override
    public void printWeek(Week week) {
        weekManager.printWeek(week);
    }
}
