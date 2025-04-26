package HR_Mudol.presentation;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.domain.*;

import java.util.Scanner;

public class EmployeeMenu implements Menu {

    public boolean start(User caller, AbstractEmployee self, Branch curBranch) {
        if (!caller.isSameEmployee(self)) {
            System.out.println("Access denied: You can only access your own menu.");
            return false;
        }
        Scanner scanner = new Scanner(System.in);
        EmployeeSystem employeeSystem = new EmployeeSystem();
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
            System.out.println("0. Exit");

            String choice = scanner.nextLine().trim();
            Employee selfEmp=(Employee)self;
            switch (choice) {
                case "1": employeeSystem.viewMyShifts(caller, selfEmp, curBranch.getWeeks().getLast()); break;
                case "2": employeeSystem.submitConstraint(caller, selfEmp,curBranch.getWeeks().getLast()); break;
                case "3": employeeSystem.updateConstraint(caller, selfEmp, curBranch.getWeeks().getLast()); break;
                case "4": employeeSystem.viewMyConstraints(caller, selfEmp); break;
                case "5": employeeSystem.viewContractDetails(caller, selfEmp); break;
                case "6": employeeSystem.viewAvailableRoles(caller, selfEmp); break;
                case "7": employeeSystem.viewPersonalDetails(caller, selfEmp); break;
                case "8": employeeSystem.changePassword(caller, selfEmp); break;
                case "0":
                    return true;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
