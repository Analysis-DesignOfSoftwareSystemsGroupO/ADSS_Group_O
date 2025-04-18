package HR_Mudol.presentation;

public class HRSystemManager implements IHRSystemManager {

    private RoleManager roleManager;
    private EmployeeManager employeeManager;

    public HRSystemManager(){
        RoleManager roleManager = new RoleManager();
        EmployeeManager employeeManager = new EmployeeManager();
        roleManager.setEmployeeManager(employeeManager);
        employeeManager.setRoleManager(roleManager);
    }
}
