package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.Service.ShiftManagerService.ShiftManagerService;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Controllers.ShiftController;
import HR_Mudol.domain.Objects.Branch;
import TransportModule.transport_module.ITransportController;

import java.sql.SQLException;
import java.util.Scanner;

public class ShiftManagerMenu implements Menu {

    @Override
    public boolean start(UserDTO caller, EmployeeDTO self, BranchDTO curBranch) throws SQLException {
        if (!caller.isShiftManager()) {
            System.out.println("Access denied.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        Branch branch = DTOToDomainMapper.fromDTO(curBranch);

        // שירותים
        EmployeeService employeeService = new EmployeeService(branch);
        ITransportController transportController = new TransportController(); // או RealTransportController
        HRService hrService = new HRService(branch, transportController);


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
                    return true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageShift(HRService hr, BranchDTO branch, UserDTO callerDTO) throws SQLException {
        WeekDTO currentWeekDTO = branch.getCurrentWeekDTO();
        if (currentWeekDTO == null) {
            System.out.println("No current week found.");
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
