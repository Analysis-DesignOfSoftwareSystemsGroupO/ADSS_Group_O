package HR_Mudol.presentation;

import HR_Mudol.Service.ManagerSystem.HRSystemManager;
import HR_Mudol.Service.ManagerSystem.ShiftManager;
import HR_Mudol.Service.ShiftManagerSystem.ShiftManagerSystem;
import HR_Mudol.domain.*;

import java.util.List;
import java.util.Scanner;

public class ShiftManagerMenu extends Menu {

    @Override
    public boolean start(User caller, AbstractEmployee self, Branch curBranch) {
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
