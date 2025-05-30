package HR_Mudol.presentation;

import HR_Mudol.DTO.UserDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.domain.Controllers.IEmployeeController;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.User;

import java.sql.SQLException;
import java.util.Scanner;

public class EmployeeMenu implements Menu {

    private final EmployeeService employeeService;
    private final Scanner scanner;

    public EmployeeMenu(IEmployeeController controller) {
        this.employeeService = new EmployeeService(controller);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public boolean start(User caller, HR_Mudol.domain.Objects.AbstractEmployee self, Branch branch) {
        int empId = self.getEmpId();
        WeekDTO currentWeek = branch.getWeekRepo().getCurrentWeekDTO();
        UserDTO callerDTO = new UserDTO(caller.getUser().getEmpId(), caller.getLevel().name());

        if (callerDTO.getUserId() != empId) {
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
                    case "1" -> employeeService.viewMyShifts(callerDTO, empId, currentWeek);
                    case "2" -> employeeService.submitConstraint(callerDTO, empId, currentWeek);
                    case "3" -> employeeService.updateConstraint(callerDTO, empId, currentWeek);
                    case "4" -> employeeService.viewMyConstraints(callerDTO, empId);
                    case "5" -> employeeService.viewContractDetails(callerDTO, empId);
                    case "6" -> employeeService.viewAvailableRoles(callerDTO, empId);
                    case "7" -> employeeService.viewPersonalDetails(callerDTO, empId);
                    case "8" -> employeeService.changePassword(callerDTO, empId);
                    case "0" -> {
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
