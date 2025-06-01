package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.Service.ManagerService.HRService;

import java.sql.SQLException;
import java.util.Scanner;

public class HRManagerMenu implements Menu {

    private final HRService hr;

    public HRManagerMenu(HRService hrService) {
        this.hr = hrService;
    }

    @Override
    public boolean start(UserDTO caller, EmployeeDTO self, BranchDTO curBranch) throws SQLException {
        if (!caller.isHRManager()) {
            System.out.println("Access denied.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== HR Management Console ===");
            System.out.println("1. Manage Employees");
            System.out.println("2. View Shifts History");
            System.out.println("3. Generate Reports");
            System.out.println("4. Manage Weekly Shifts");
            System.out.println("5. Manage Roles");
            System.out.println("6. Display Dashboard");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> System.out.println("Not yet implemented");
                case "2" -> System.out.println("Not yet implemented");
                case "3" -> System.out.println("Not yet implemented");
                case "4" -> manageShift(hr, curBranch, caller);
                case "5" -> System.out.println("Not yet implemented");
                case "6" -> {
                    WeekDTO currentWeek = curBranch.getCurrentWeekDTO();
                    if (currentWeek != null) {
                        try {
                            hr.displayDashboard(caller, currentWeek);
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("No current week available.");
                    }
                }
                case "0" -> {
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageShift(HRService hr, BranchDTO branch, UserDTO callerDTO) {
        WeekDTO currentWeekDTO = branch.getCurrentWeekDTO();
        if (currentWeekDTO == null) {
            System.out.println("No current week found.");
            return;
        }

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Assigning roles to weekly shifts");
            System.out.println("2. Assigning employees to weekly shifts");
            System.out.println("3. Edit shifts");
            System.out.println("0. Back to Main Menu");

            String choice = sc.nextLine();
            try {
                switch (choice) {
                    case "1" -> hr.manageTheWeekRelevantRoles(callerDTO, currentWeekDTO);
                    case "2" -> hr.assigningEmployToShifts(callerDTO, currentWeekDTO);
                    case "3" -> editShifts(hr, callerDTO, currentWeekDTO);
                    case "0" -> {
                        hr.close();
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void editShifts(HRService hr, UserDTO caller, WeekDTO week) {
        if (!caller.isHRManager()) {
            throw new SecurityException("Access denied.");
        }
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an action: ");
            System.out.println("1. Add employee");
            System.out.println("2. Remove employee");
            System.out.println("3. Add role");
            System.out.println("4. Remove role");
            System.out.println("5. Cancel a shift");
            System.out.println("0. Back");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1" -> hr.addEmployeeToShift(caller, week);
                    case "2" -> hr.removeEmployeeFromShift(caller, week);
                    case "3" -> hr.addARoleToShift(caller, week);
                    case "4" -> hr.removeRoleFromShift(caller, week);
                    case "5" -> hr.cancelShift(caller, week);
                    case "0" -> {
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

}
