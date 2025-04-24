package HR_Mudol.presentation;

import HR_Mudol.Service.ManagerSystem.HRSystemManager;
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
                case "1": {
                    EmployeeMenu menu = new EmployeeMenu();
                    menu.start(caller, self, curBranch);
                }
                break;
                case "2":
                    manageShift(hr, curBranch.getWeeks(), caller, scanner);
                    break;
                case "0":
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageShift(HRSystemManager hr, List<Week> weeks, User caller, Scanner sc) {
        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Remove an employee from a shift");
            System.out.println("2. Add an employee to a shift");
            System.out.println("3. Transfer cancellation card");
            System.out.println("0. Exit");

            String choice = sc.nextLine();
            /*
            switch (choice) {
                case "1":
                    try {
                        hr.manageTheWeekRelevantRoles(caller, weeks.getLast());
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    hr.assigningEmployToShift(caller, weeks.getLast());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
            */
        }
    }

    private void transferCancellationCard(User caller) {
        if (!caller.isShiftManager()){

        }
    }
}
