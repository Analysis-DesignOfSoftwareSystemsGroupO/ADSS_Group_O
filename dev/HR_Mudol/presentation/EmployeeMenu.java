package HR_Mudol.presentation;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.domain.*;

import java.util.Scanner;

/**
 * This class represents the Employee menu that provides various options
 * for employees to interact with their system functionalities, such as viewing shifts,
 * submitting constraints, editing their details, and more.
 */
public class EmployeeMenu implements Menu {

    /**
     * Starts the employee menu interface.
     * It checks that the user has access to their own menu.
     * @param caller The user initiating the menu interaction.
     * @param self The employee object representing the caller.
     * @param curBranch The current branch where the employee is working.
     * @return boolean indicating if the menu interaction was completed successfully.
     */
    public boolean start(User caller, AbstractEmployee self, Branch curBranch) {
        // Ensure the caller can only access their own menu
        if (!caller.isSameEmployee(self)) {
            System.out.println("Access denied: You can only access your own menu.");
            return false;
        }

        // Initialize scanner and employee system for user input
        Scanner scanner = new Scanner(System.in);
        EmployeeSystem employeeSystem = new EmployeeSystem();

        // Infinite loop for continuous interaction with the employee menu
        while (true) {
            // Display available options to the user
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

            // Capture the user's input
            String choice = scanner.nextLine().trim();

            // Cast the AbstractEmployee to an Employee type to access specific employee methods
            Employee selfEmp = (Employee) self;

            // Process the user's choice
            switch (choice) {
                case "1":
                    employeeSystem.viewMyShifts(caller, selfEmp, curBranch.getWeeks().getLast());
                    break;
                case "2":
                    employeeSystem.submitConstraint(caller, selfEmp, curBranch.getWeeks().getLast());
                    break;
                case "3":
                    employeeSystem.updateConstraint(caller, selfEmp, curBranch.getWeeks().getLast());
                    break;
                case "4":
                    employeeSystem.viewMyConstraints(caller, selfEmp);
                    break;
                case "5":
                    employeeSystem.viewContractDetails(caller, selfEmp);
                    break;
                case "6":
                    employeeSystem.viewAvailableRoles(caller, selfEmp);
                    break;
                case "7":
                    employeeSystem.viewPersonalDetails(caller, selfEmp);
                    break;
                case "8":
                    employeeSystem.changePassword(caller, selfEmp);
                    break;
                case "0":
                    return true;  // Exit the menu
                default:
                    // If the user provides an invalid option, prompt again
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}
