package HR_Mudol;

import HR_Mudol.domain.*;
import HR_Mudol.presentation.HRSystemManager;

import java.time.LocalDate;
import java.util.Scanner;

//test

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HRSystemManager hrSystemManager = new HRSystemManager();
        HR_Manger manager = new HR_Manger("Rami Levi", 111111111, "111111111", "111111111", 30000, LocalDate.now());
        User admin = new User(manager, Level.HRManager);

        Week curWeek = hrSystemManager.createNewWeek(admin);

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

            try {
                switch (choice) {
                    case "1":
                        hrSystemManager.printWeek(null);
                        break;
                    case "2":
                        manageEmployees(hrSystemManager, admin, scanner);
                        break;
                    case "3":
                        generateReports(hrSystemManager, admin, scanner);
                        break;
                    case "4":
                        manageShift(hrSystemManager, curWeek,admin, scanner);
                        break;
                    case "5":
                        manageRoles(hrSystemManager, admin, scanner);
                        break;
                    case "6":
                        try {
                            hrSystemManager.displayDashboard(admin);
                        } catch (Exception e) {
                            System.out.println("Failed to display dashboard.");
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("\nError occurred while processing the option: " + choice);
                System.out.println(e.getMessage());
            }
        }
    }

    private static void manageShift(HRSystemManager hr, Week curWeek, User caller, Scanner sc) {
        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Assigning roles to weekly shifts");
            System.out.println("2. Assigning employees to weekly shifts");

            String choice = sc.nextLine();
            try {
                switch (choice) {
                    case "1":
                        try {
                            hr.manageTheWeekRelevantRoles(caller, curWeek);

                        } catch (Exception e) {
                            System.out.println("Failed to manage weekly shifts.");
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "2":
                        try {
                            hr.assigningEmployToShifts(caller, curWeek);
                        } catch (Exception e) {
                            System.out.println("Failed to manage weekly shifts.");
                            System.out.println(e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("The request was failed.");
                System.out.println(e.getMessage());
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
            try {
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
                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("The request was failed.");
                System.out.println(e.getMessage());
            }
        }
    }

    private static void generateReports(HRSystemManager hr, User caller, Scanner sc) {
        System.out.println("\n--- Report Generation ---");
        System.out.println("1. Weekly Report");
        System.out.println("2. Employee Report");
        System.out.println("3. Shift Report");
        System.out.println("4. Custom Report");

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
        System.out.println("4. Print All Roles");

        String choice = sc.nextLine();
        try {
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
        } catch (Exception e) {
            System.out.println("Failed to manage roles.");
            System.out.println(e.getMessage());
        }
    }
}
