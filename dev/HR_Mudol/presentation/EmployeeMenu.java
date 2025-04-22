package HR_Mudol.presentation;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.domain.Employee;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

import java.util.Scanner;

public class EmployeeMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final EmployeeSystem employeeSystem = new EmployeeSystem();

    public boolean start(User caller, Employee self, Week currentWeek) {
        if (!caller.isSameEmployee(self)) {
            System.out.println("Access denied: You can only access your own menu.");
            return false;
        }

        while (true) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. View my shifts");
            System.out.println("2. Submit weekly constraints");
            System.out.println("3. Edit existing constraints");
            System.out.println("4. View my submitted constraints");
            System.out.println("5. View my contract details");
            System.out.println("6. View my available roles");
            System.out.println("7. View my personal details");
            System.out.println("8. Change my password");
            System.out.println("0. Logout");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": employeeSystem.viewMyShifts(caller, self, currentWeek); break;
                case "2": employeeSystem.submitConstraint(caller, self,currentWeek); break;
                case "3": employeeSystem.updateConstraint(caller, self, currentWeek); break;
                case "4": employeeSystem.viewMyConstraints(caller, self); break;
                case "5": employeeSystem.viewContractDetails(caller, self); break;
                case "6": employeeSystem.viewAvailableRoles(caller, self); break;
                case "7": employeeSystem.viewPersonalDetails(caller, self); break;
                case "8": employeeSystem.changePassword(caller, self); break;
                case "0":
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
