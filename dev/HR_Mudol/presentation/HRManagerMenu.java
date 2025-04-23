package HR_Mudol.presentation;

import HR_Mudol.domain.*;
import HR_Mudol.Service.ManagerSystem.HRSystemManager;

import java.util.Scanner;

public class HRManagerMenu {

    public static boolean runMenu(User admin, HRSystemManager hrSystemManager, Week curWeek) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== HR Management Console ===");
            System.out.println("1. View Shift History");
            System.out.println("2. Manage Employees");
            System.out.println("3. Generate Reports");
            System.out.println("4. Manage Weekly Shifts");
            System.out.println("5. Manage Roles");
            System.out.println("6. Display Dashboard");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": hrSystemManager.printWeek(null); break;
                case "2": manageEmployees(hrSystemManager, admin, scanner); break;
                case "3": generateReports(hrSystemManager, admin, scanner); break;
                case "4": manageShift(hrSystemManager, curWeek, admin, scanner); break;
                case "5": manageRoles(hrSystemManager, admin, scanner); break;
                case "6": hrSystemManager.displayDashboard(admin); break;
                case "0":
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }


    private static void manageShift(HRSystemManager hr, Week curWeek, User caller, Scanner sc) {
        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Assigning roles to weekly shifts");
            System.out.println("2. Assigning employees to weekly shifts");
            System.out.println("0. Back to Main Menu");

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    hr.manageTheWeekRelevantRoles(caller, curWeek);
                    break;
                case "2":
                    hr.assigningEmployToShifts(caller, curWeek);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void manageEmployees(HRSystemManager hr, User caller, Scanner sc) {
        while (true) {
            System.out.println("\n--- Employee Management ---");
            System.out.println("1. Add Employee");
            System.out.println("2. Remove Employee");
            System.out.println("3. Update Bank Account");
            System.out.println("4. Update Salary");
            System.out.println("5. Print All Employees");
            System.out.println("0. Back to Main Menu");

            String choice = sc.nextLine();
            switch (choice) {
                case "1": hr.addEmployee(caller); break;
                case "2": hr.removeEmployee(caller); break;
                case "3": hr.updateBankAccount(caller); break;
                case "4": hr.updateSalary(caller); break;
                case "5": hr.printAllEmployees(caller); break;
                case "0": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void generateReports(HRSystemManager hr, User caller, Scanner sc) {
        System.out.println("\n--- Report Generation ---");
        System.out.println("1. Weekly Report");
        System.out.println("2. Employee Report");
        System.out.println("3. Shift Report");
        System.out.println("4. Custom Report");
        System.out.println("0. Back to Main Menu");

        String choice = sc.nextLine();
        try {
            switch (choice) {
                case "1":
                    System.out.print("Enter Week ID: ");
                    int weekId = Integer.parseInt(sc.nextLine());
                    hr.generateWeeklyReport(caller, weekId);
                    break;
                case "2":
                    System.out.print("Enter Employee ID: ");
                    int empId = Integer.parseInt(sc.nextLine());
                    hr.generateEmployeeReport(caller, empId);
                    break;
                case "3":
                    System.out.print("Enter Shift ID: ");
                    int shiftId = Integer.parseInt(sc.nextLine());
                    hr.generateShiftReport(caller, shiftId);
                    break;
                case "4":
                    System.out.print("Enter filter: ");
                    String filter = sc.nextLine();
                    hr.generateCustomReport(caller, filter);
                    break;
                case "0": return;
                default:
                    System.out.println("Invalid option.");

            }
        } catch (Exception e) {
            System.out.println("Failed to generate report.");
            System.out.println(e.getMessage());
        }
    }

    private static void manageRoles(HRSystemManager hr, User caller, Scanner sc) {
        System.out.println("\n--- Role Management ---");
        System.out.println("1. Create Role");
        System.out.println("2. Assign Employee to Role");
        System.out.println("3. Remove Employee from Role");
        System.out.println("4. Assign employee to shift manager");
        System.out.println("5. Print All Roles");
        System.out.println("0. Back to Main Menu");

        String choice = sc.nextLine();
        switch (choice) {
            case "1": hr.createRole(caller); break;
            case "2": hr.assignEmployeeToRole(caller); break;
            case "3": hr.removeEmployeeFromRole(caller); break;
            case "4": hr.assignEmployeeToShiftManager(caller); break;
            case "5": hr.printAllRoles(caller); break;
            case "0": return;
            default: System.out.println("Invalid option.");
        }
    }
}
