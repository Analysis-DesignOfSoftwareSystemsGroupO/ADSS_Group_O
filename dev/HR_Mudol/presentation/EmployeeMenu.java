package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.domain.Controllers.IEmployeeController;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu implements Menu {

    private final EmployeeService employeeService;
    private final Scanner scanner;

    public EmployeeMenu(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public boolean start(UserDTO caller, EmployeeDTO self, BranchDTO branch) {
        long empId = self.getEmployeeId();
        WeekDTO currentWeek = branch.getCurrentWeekDTO();

        if (caller.getUserId() != empId) {
            System.out.println("Access denied: You can only access your own menu.");
            return false;
        }

        while (true) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. View my shifts");
            System.out.println("2. Submit weekly constraints");
            System.out.println("3. Edit existing constraints");
            System.out.println("4. View my constraints (current/Locked)");
            System.out.println("5. View my contract details");
            System.out.println("6. View my available roles");
            System.out.println("7. View my personal details");
            System.out.println("8. Change my password");
            System.out.println("0. Exit");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> employeeService.viewMyShifts(caller, empId);
                    case "2" -> employeeService.submitConstraint(caller, empId, currentWeek);
                    case "3" -> employeeService.updateConstraint(caller, empId, currentWeek);
                    case "4" -> employeeService.viewMyConstraints(caller, empId);
                    case "5" -> employeeService.viewContractDetails(caller, empId);
                    case "6" -> employeeService.viewAvailableRoles(caller, empId);
                    case "7" -> employeeService.viewPersonalDetails(caller, empId);
                    case "8" -> employeeService.changePassword(caller, empId);
                    case "0" -> {
                        employeeService.close();
                        return true;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (SecurityException se) {
                System.out.println("Security error: " + se.getMessage());
            } catch (Exception ex) {
                System.out.println("Unexpected error: " + ex.getMessage());
            }
        }
    }
}
