package HR_Mudol;

import HR_Mudol.domain.*;
import HR_Mudol.presentation.HRSystemManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HRSystemManager hrSystemManager = new HRSystemManager();
        User admin = new User("admin"); // אפשר לשנות לפי סוגי משתמשים

        while (true) {
            System.out.println("\n=== HR Management Console ===");
            System.out.println("1. View Shift History");
            System.out.println("2. Manage Employees");
            System.out.println("3. Generate Reports");
            System.out.println("4. Manage Weekly Shifts");
            System.out.println("5. Manage Roles");
            System.out.println("6. Display Dashboard");
            System.out.println("0. Exit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    hrSystemManager.printWeek(null); // אפשר לעבור על כמה שבועות אם יש היסטוריה
                    break;

                case "2":
                    manageEmployees(hrSystemManager, admin, scanner);
                    break;

                case "3":
                    generateReports(hrSystemManager, admin, scanner);
                    break;

                case "4":
                    hrSystemManager.createNewWeek(admin);
                    hrSystemManager.manageTheWeekRelevantRoles(admin, null); // week לפי לוגיקה שלך
                    hrSystemManager.assigningEmployToShifts(admin, null);
                    break;

                case "5":
                    manageRoles(hrSystemManager, admin, scanner);
                    break;

                case "6":
                    hrSystemManager.displayDashboard(admin);
                    break;

                case "0":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void manageEmployees(HRSystemManager hr, User caller, Scanner sc) {
        System.out.println("\n--- Employee Management ---");
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. Update Bank Account");
        System.out.println("4. Update Salary");
        System.out.println("5. Print All Employees");

        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                hr.addEmployee(caller);
                break;
            case "2":
                hr.removeEmployee(caller);
                break;
            case "3":
                hr.updateBankAccount(caller);
                break;
            case "4":
                hr.updateSalary(caller);
                break;
            case "5":
                hr.printAllEmployees(caller);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void generateReports(HRSystemManager hr, User caller, Scanner sc) {
        System.out.println("\n--- Report Generation ---");
        System.out.println("1. Weekly Report");
        System.out.println("2. Employee Report");
        System.out.println("3. Shift Report");
        System.out.println("4. Custom Report");

        String choice = sc.nextLine();
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
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void manageRoles(HRSystemManager hr, User caller, Scanner sc) {
        System.out.println("\n--- Role Management ---");
        System.out.println("1. Create Role");
        System.out.println("2. Assign Employee to Role");
        System.out.println("3. Remove Employee from Role");
        System.out.println("4. Print All Roles");

        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                hr.createRole(caller);
                break;
            case "2":
                hr.assignEmployeeToRole(caller);
                break;
            case "3":
                hr.removeEmployeeFromRole(caller);
                break;
            case "4":
                hr.printAllRoles(caller);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
