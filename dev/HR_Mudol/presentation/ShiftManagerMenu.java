package HR_Mudol.presentation;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.Service.ShiftManagerService.ShiftManagerService;
import HR_Mudol.domain.Controllers.ShiftController;
import HR_Mudol.domain.Objects.Branch;

import java.util.Scanner;

public class ShiftManagerMenu implements Menu {

    @Override
    public boolean start(UserDTO caller, EmployeeDTO self, Branch curBranch) {
        if (!caller.isShiftManager()) {
            System.out.println("Access denied.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        HRService hr = new HRService(curBranch);

        while (true) {
            System.out.println("\n=== Shift Manager Menu ===");
            System.out.println("1. My profile management console");
            System.out.println("2. To Shift management console");
            System.out.println("0. Logout");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    EmployeeService empService = new EmployeeService(hr.getEmployeeController());
                    EmployeeMenu menu = new EmployeeMenu(hr.getEmployeeController());
                    WeekDTO currentWeek = curBranch.getWeekRepo().getCurrentWeekDTO();
                    menu.start(caller, self, curBranch);
                }
                case "2" -> manageShift(hr, curBranch, caller);
                case "0" -> {
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageShift(HRService hr, Branch branch, UserDTO callerDTO) {
        WeekDTO currentWeekDTO = branch.getWeekRepo().getCurrentWeekDTO();
        if (currentWeekDTO == null) {
            System.out.println("No current week found.");
            return;
        }

        ShiftController shiftController = new ShiftController(branch, hr.getRoleController());
        ShiftManagerService shiftSys = new ShiftManagerService(shiftController);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Remove an employee from a shift");
            System.out.println("2. Add an employee to a shift");
            System.out.println("3. Transfer cancellation card");
            System.out.println("0. Exit");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> shiftSys.removeEmployeeFromShift(callerDTO);
                case "2" -> shiftSys.addEmployeeToShift(callerDTO);
                case "3" -> shiftSys.transferCancellationCard(callerDTO);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
