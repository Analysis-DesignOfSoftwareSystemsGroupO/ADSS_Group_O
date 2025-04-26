package HR_Mudol.presentation;

import HR_Mudol.domain.*;

import java.util.List;
import java.util.Scanner;

/**
 * LoginScreen class handles login and menu redirection based on user role.
 */
public class LoginScreen {

    /**
     * Starts the login process, prompts for user credentials, and redirects to the appropriate menu
     * based on the user role.
     *
     * @param curBranch The current branch object which contains the list of users.
     */
    public void start(Branch curBranch) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter employee ID: ");
            String idInput = scanner.nextLine();
            int id;

            try {
                id = Integer.parseInt(idInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter numbers only.\n");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User matched = findUser(id, password, curBranch.getUsers());

            if (matched != null) {
                if (matched.isManager()) {
                    HRManagerMenu menu = new HRManagerMenu();
                    boolean logout = menu.start(matched, matched.getUser(), curBranch);
                    if (logout) continue; // Return to login
                } else if (matched.isShiftManager()) {
                    ShiftManagerMenu menu = new ShiftManagerMenu();
                    boolean logout = menu.start(matched, matched.getUser(), curBranch);
                    if (logout) continue; // Return to login
                } else {
                    EmployeeMenu menu = new EmployeeMenu();
                    boolean logout = menu.start(matched, matched.getUser(), curBranch);
                    if (logout) continue; // Return to login
                }
            } else {
                System.out.println("Invalid ID or password. Please try again.\n");
            }
        }
    }

    /**
     * Finds a user by employee ID and password.
     *
     * @param id The employee ID.
     * @param password The password entered by the user.
     * @param users The list of users to search through.
     * @return The matching user if found, or null if no match is found.
     */
    private User findUser(int id, String password, List<User> users) {
        for (User u : users) {
            if (u.getUser().getEmpId() == id && u.getUser().getEmpPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

}
