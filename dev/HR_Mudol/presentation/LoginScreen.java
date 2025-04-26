package HR_Mudol.presentation;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.Service.ManagerSystem.HRSystemManager;
import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * The LoginScreen class manages the user login process.
 * It prompts the user for their employee ID and password, then verifies the credentials against the stored users.
 * Based on the user role (Manager, Shift Manager, or Employee), it redirects to the appropriate menu.
 */
public class LoginScreen {

    /**
     * Starts the login process. It continually asks for user credentials and directs the user to the appropriate menu based on their role.
     * @param curBranch The current branch that holds the user data to verify against.
     */
    public void start(Branch curBranch) {

     Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter employee ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User matched = findUser(id, password,curBranch.getUsers());

            if (matched != null) {
                if (matched.isManager()) {
                    HRManagerMenu menu=new HRManagerMenu();
                    boolean logout = menu.start(matched,  matched.getUser(), curBranch);
                    if (logout) continue; // חזרה למסך התחברות
                } else if (matched.isShiftManager()){
                    ShiftManagerMenu menu=new ShiftManagerMenu();
                    boolean logout = menu.start(matched, matched.getUser(), curBranch);
                    if (logout) continue; // חזרה למסך התחברות
                }
                else {
                    EmployeeMenu menu = new EmployeeMenu();
                    boolean logout = menu.start(matched, matched.getUser(), curBranch);
                    if (logout) continue; // חזרה למסך התחברות
                }
            } else {
                System.out.println("Invalid ID or password. Please try again.\n");
            }
        }
    }

    /**
     * Finds a user by their employee ID and password.
     * @param id The employee ID to search for.
     * @param password The password to check.
     * @param users The list of users to search through.
     * @return The matched User object if found, or null if no match is found.
     */
    private User findUser(int id, String password,List<User> users) {
        for (User u : users) {
            if (u.getUser().getEmpId() == id && u.getUser().getEmpPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }
}
