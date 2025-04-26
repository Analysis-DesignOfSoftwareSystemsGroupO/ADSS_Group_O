package HR_Mudol;

import HR_Mudol.domain.*;
import HR_Mudol.presentation.LoginScreen;

import java.time.LocalDate;

/**
 * Main class to initialize the system and launch the login screen.
 */
public class Main {
    public static void main(String[] args) {

        // Create an empty Branch
        Branch curBranch = new Branch();

        // Create and add HR Manager (ONLY this user will have HRManager level)
        HRManager hrManager = new HRManager("Rami Levi", 111111111, "admin", "122345", 300000, LocalDate.now(), 2, 2, 5, 10);
        curBranch.getEmployees().add(hrManager);
        User hrUser = new User(hrManager, Level.HRManager);
        curBranch.getUsers().add(hrUser);

        // Create and add Shift Manager
        Employee shiftManager = new Employee("Yossi Cohen", 222222222, "shiftadmin", "999999", 7000, LocalDate.now(), 2, 2, 5, 10);
        curBranch.getEmployees().add(shiftManager);
        User shiftUser = new User(shiftManager, Level.shiftManager);
        curBranch.getUsers().add(shiftUser);

        // Create predefined roles
        Role driverRole = new Role("Driver");
        Role cashierRole = new Role("Cashier");
        Role warehouseRole = new Role("Warehouse Worker");
        Role shiftSupervisorRole = new Role("Shift Supervisor");
        Role courierRole = new Role("Courier");
        Role stockOrganizerRole = new Role("Stock Organizer");
        Role cookRole = new Role("Cook");
        Role securityGuardRole = new Role("Security Guard");

        // Add roles to the branch
        curBranch.getRoles().add(driverRole);
        curBranch.getRoles().add(cashierRole);
        curBranch.getRoles().add(warehouseRole);
        curBranch.getRoles().add(shiftSupervisorRole);
        curBranch.getRoles().add(courierRole);
        curBranch.getRoles().add(stockOrganizerRole);
        curBranch.getRoles().add(cookRole);
        curBranch.getRoles().add(securityGuardRole);

        // Add employees assigned to specific roles
        addEmployeeWithRole(curBranch, "Avi levi", 333333333, "driverpass", "BANK1", 4500, driverRole, hrUser);
        addEmployeeWithRole(curBranch, "Tamar blala", 444444444, "cashierpass", "BANK2", 4300, cashierRole, hrUser);
        addEmployeeWithRole(curBranch, "David king", 555555555, "warehousepass", "BANK3", 4200, warehouseRole, hrUser);
        addEmployeeWithRole(curBranch, "Shiran princes", 666666666, "supervisorpass", "BANK4", 5200, shiftSupervisorRole, hrUser);
        addEmployeeWithRole(curBranch, "Oren thetree", 777777777, "courierpass", "BANK5", 4100, courierRole, hrUser);
        addEmployeeWithRole(curBranch, "Noa themagniva", 888888888, "organizerpass", "BANK6", 4000, stockOrganizerRole, hrUser);
        addEmployeeWithRole(curBranch, "Ron shamimkashim", 999999999, "cookpass", "BANK7", 4300, cookRole, hrUser);
        addEmployeeWithRole(curBranch, "Eyal thesmartmen", 123123123, "securitypass", "BANK8", 4400, securityGuardRole, hrUser);

        // Add 8 additional regular employees without specific roles
        for (int i = 0; i < 8; i++) {
            int empId = 600000000 + i;
            String name = "Employee" + (i + 1);
            String password = "pass" + (i + 1);
            String bankAccount = "BANK" + (i + 9);
            int salary = 4000 + (i * 100);

            Employee emp = new Employee(name, empId, password, bankAccount, salary, LocalDate.now(), 2, 2, 5, 10);
            curBranch.getEmployees().add(emp);
            User user = new User(emp, Level.regularEmp);
            curBranch.getUsers().add(user);
        }

        // Launch the login screen
        LoginScreen login = new LoginScreen();
        login.start(curBranch);
    }

    /**
     * Helper method to add an employee to a branch and assign them to a role.
     * The employee is created as a regular employee (Level.regularEmp).
     *
     * @param branch the branch to add the employee to
     * @param name the name of the employee
     * @param id the employee's ID
     * @param password the employee's password
     * @param bankAccount the employee's bank account
     * @param salary the employee's salary
     * @param role the role to assign
     * @param hrUser the HR manager performing the assignment
     */
    private static void addEmployeeWithRole(Branch branch, String name, int id, String password, String bankAccount, int salary, Role role, User hrUser) {
        Employee emp = new Employee(name, id, password, bankAccount, salary, LocalDate.now(), 2, 2, 5, 10);
        branch.getEmployees().add(emp);

        // Always add as regular employee
        User user = new User(emp, Level.regularEmp);
        branch.getUsers().add(user);

        // Assign to role using the HRManager user
        role.addNewEmployee(hrUser, emp);
    }
}
