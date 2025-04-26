package HR_Mudol.presentation;

import HR_Mudol.Service.ManagerSystem.HRSystemManager;
import HR_Mudol.Service.ManagerSystem.ShiftManager;
import HR_Mudol.Service.ShiftManagerSystem.ShiftManagerSystem;
import HR_Mudol.domain.*;
import java.util.Scanner;

/**
 * ShiftManagerMenu is the menu for shift managers, providing access to various shift management features.
 * It ensures that only shift managers can access this menu, and it offers the user options to manage shifts, profiles, and logout.
 */
public class ShiftManagerMenu implements Menu {

    /**
     * Starts the Shift Manager menu for the caller (shift manager).
     * This method will display the options for managing the profile or shifts and process the user's choice.
     * @param caller The user initiating the menu interaction (must be a shift manager).
     * @param self The AbstractEmployee object representing the caller's personal data.
     * @param curBranch The branch where the caller works.
     * @return boolean indicating whether the menu interaction was completed successfully (i.e., if the user logged out or not).
     */
    @Override
    public boolean start(User caller, AbstractEmployee self, Branch curBranch) {
        if (!caller.isShiftManager()) {
            System.out.println("Access denied.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        HRSystemManager hr = new HRSystemManager(curBranch);

        while (true) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. My profile management console");
            System.out.println("2. To Shift management console");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    EmployeeMenu menu = new EmployeeMenu();
                    menu.start(caller, self, curBranch);
                    break;
                case "2":
                    manageShift(hr, curBranch, caller, scanner);
                    break;
                case "0":
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    /**
     * Manages the shift assignments for the current week, such as adding/removing employees from shifts or handling shift cancellations.
     * It allows the Shift Manager to interact with the system to manage shifts for employees.
     * @param hr The HR system manager to interact with role management.
     * @param branch The branch where the shift management takes place.
     * @param caller The shift manager initiating the shift management actions.
     * @param sc The scanner to capture user input.
     */
    private static void manageShift(HRSystemManager hr, Branch branch, User caller, Scanner sc) {
        Week currentWeek = branch.getWeeks().get(branch.getWeeks().size() - 1);

        // יצירת ShiftManager (דורש IRoleManager שנמצא ב־HRSystemManager)
        ShiftManager shiftManager = new ShiftManager(hr.getRoleManager());

        // יצירת ShiftManagerSystem
        ShiftManagerSystem shiftSys = new ShiftManagerSystem(currentWeek, branch, shiftManager);


        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Remove an employee from a shift");
            System.out.println("2. Add an employee to a shift");
            System.out.println("3. Transfer cancellation card");
            System.out.println("0. Exit");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    shiftSys.removeEmployeeFromShift(caller);
                    break;
                case "2":
                    shiftSys.addEmployeeToShift(caller);
                    break;
                case "3":
                    shiftSys.transferCancellationCard(caller);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
