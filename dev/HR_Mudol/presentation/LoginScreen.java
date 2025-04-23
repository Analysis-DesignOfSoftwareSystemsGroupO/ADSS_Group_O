package HR_Mudol.presentation;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.Service.ManagerSystem.HRSystemManager;
import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LoginScreen {

    private final Scanner scanner = new Scanner(System.in);
    private final EmployeeSystem employeeSystem = new EmployeeSystem();
    private HRSystemManager hrSystemManager;


    public void start(Branch curBranch) {

        this.hrSystemManager = new HRSystemManager(curBranch);

        while (true) {
            System.out.print("Enter employee ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User matched = findUser(id, password,curBranch.getUsers());

            if (matched != null) {
                if (matched.isManager()) {
                    boolean logout = HRManagerMenu.runMenu(matched, hrSystemManager, curBranch);
                    if (logout) continue; // חזרה למסך התחברות
                } else {
                    EmployeeMenu menu = new EmployeeMenu();
                    boolean logout = menu.start(matched, (Employee) matched.getUser(), curBranch);
                    if (logout) continue; // חזרה למסך התחברות
                }
            } else {
                System.out.println("Invalid ID or password. Please try again.\n");
            }
        }
    }


    private User findUser(int id, String password,List<User> users) {
        for (User u : users) {
            if (u.getUser().getEmpId() == id && u.getUser().getEmpPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
