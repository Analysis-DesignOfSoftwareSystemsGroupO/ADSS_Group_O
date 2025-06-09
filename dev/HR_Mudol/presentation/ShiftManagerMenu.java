package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.Service.ShiftManagerService.ShiftManagerService;
import HR_Mudol.Service.TransportService.TransportShiftIntegrator;
import HR_Mudol.domain.Controllers.RoleController;
import HR_Mudol.domain.Objects.Week;
import TransportModule.transport_module.ITransportController;
import TransportModule.transport_module.TransportContorollerDomain;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ShiftManagerMenu implements Menu {

    @Override
    public boolean start(UserDTO caller, EmployeeDTO self, BranchDTO curBranch) throws SQLException {
        if (!caller.isSHManager()) {
            System.out.println("Access denied.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);

        // שירותים
        EmployeeService employeeService = new EmployeeService(curBranch);
        HRService hrService = new HRService(curBranch);

        while (true) {
            System.out.println("\n=== Shift Manager Menu ===");
            System.out.println("1. My profile management console");
            System.out.println("2. To Shift management console");
            System.out.println("0. Logout");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    EmployeeMenu menu = new EmployeeMenu(employeeService);
                    menu.start(caller, self, curBranch);
                }
                case "2" -> manageShift(hrService, curBranch, caller);
                case "0" -> {
                    System.out.println("Logging out. Returning to login screen.");
                    hrService.close();
                    employeeService.close();
                    return true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageShift(HRService hr, BranchDTO branch, UserDTO callerDTO) throws SQLException {
        WeekDTO weeks = hr.getCurrentWeekDTO();
        if (weeks == null) {
            System.out.println("❌ No weeks available for shift management.");
            return;
        }
        ShiftManagerService shiftSys = new ShiftManagerService(branch, hr.getRoleController());
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
