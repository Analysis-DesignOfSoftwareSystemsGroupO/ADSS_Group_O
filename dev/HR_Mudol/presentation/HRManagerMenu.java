package HR_Mudol.presentation;

import HR_Mudol.Service.ManagerSystem.*;
import HR_Mudol.domain.*;
import HR_Mudol.Service.ManagerSystem.HRSystemManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HRManagerMenu implements Menu{


    public boolean start(User caller, AbstractEmployee self, Branch curBranch) {
        if (!caller.isShiftManager()) {
            System.out.println("Access denied.");
            return false;
        }

        Scanner scanner = new Scanner(System.in);
        HRSystemManager hrSystemManager=new HRSystemManager(curBranch);

        while (true) {
            System.out.println("\n=== HR Management Console ===");

            System.out.println("1. Manage Employees");
            System.out.println("2. View Shifts History");
            System.out.println("3. Generate Reports");
            System.out.println("4. Manage Weekly Shifts");
            System.out.println("5. Manage Roles");
            System.out.println("6. Display Dashboard");
            System.out.println("0. Logout");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": manageEmployees(hrSystemManager, caller, scanner); break;
                case "2": viewShiftsHistory(hrSystemManager,curBranch.getWeeks()); break;
                case "3": generateReports(hrSystemManager, caller, scanner,curBranch); break;
                case "4": manageShift(hrSystemManager, curBranch.getWeeks(), caller, scanner); break;
                case "5": manageRoles(hrSystemManager, caller, scanner); break;
                case "6": hrSystemManager.displayDashboard(caller,curBranch); break;
                case "0":
                    System.out.println("Logging out. Returning to login screen.");
                    return true;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    private static void viewShiftsHistory(HRSystemManager hr, List<Week> weeks) {
        if (weeks == null || weeks.isEmpty()) {
            System.out.println("No weeks available.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.print("Do you want to see only the last week? (Y/N): ");
        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("Y")) {
            // מציאת השבוע האחרון ידנית
            Week lastWeek = weeks.get(0);
            for (int i = 1; i < weeks.size(); i++) {
                if (weeks.get(i).getConstraintDeadline().isAfter(lastWeek.getConstraintDeadline())) {
                    lastWeek = weeks.get(i);
                }
            }
            System.out.println("Last week:");
            hr.printWeek(lastWeek);
            return;
        }

        // קבלת תאריכי התחלה וסיום
        LocalDate fromDate = null;
        LocalDate toDate = null;

        while (fromDate == null) {
            System.out.print("Enter start date (yyyy-MM-dd): ");
            try {
                fromDate = LocalDate.parse(scanner.nextLine(), formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        while (toDate == null) {
            System.out.print("Enter end date (yyyy-MM-dd): ");
            try {
                toDate = LocalDate.parse(scanner.nextLine(), formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Try again.");
            }
        }

        // סינון שבועות בטווח באופן ידני
        List<Week> filteredWeeks = new ArrayList<>();
        for (int i = 0; i < weeks.size(); i++) {
            LocalDate weekDate = weeks.get(i).getConstraintDeadline().toLocalDate();
            if (!weekDate.isBefore(fromDate) && !weekDate.isAfter(toDate)) {
                filteredWeeks.add(weeks.get(i));
            }
        }

        // מיון ידני לפי תאריך
        for (int i = 0; i < filteredWeeks.size() - 1; i++) {
            for (int j = i + 1; j < filteredWeeks.size(); j++) {
                LocalDate d1 = filteredWeeks.get(i).getConstraintDeadline().toLocalDate();
                LocalDate d2 = filteredWeeks.get(j).getConstraintDeadline().toLocalDate();
                if (d1.isAfter(d2)) {
                    Week temp = filteredWeeks.get(i);
                    filteredWeeks.set(i, filteredWeeks.get(j));
                    filteredWeeks.set(j, temp);
                }
            }
        }

        if (filteredWeeks.isEmpty()) {
            System.out.println("No weeks found in the selected range.");
        } else {
            System.out.println("Weeks in the selected range:");
            for (Week week : filteredWeeks) {
                hr.printWeek(week);
            }
        }
    }


    private static void manageShift(HRSystemManager hr,List<Week> weeks, User caller, Scanner sc) {
        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Assigning roles to weekly shifts");
            System.out.println("2. Assigning employees to weekly shifts");
            System.out.println("3. Edit shifts");
            System.out.println("0. Back to Main Menu");


            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    try {
                        hr.manageTheWeekRelevantRoles(caller, weeks.getLast());
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "2":
                    hr.assigningEmployToShifts(caller, weeks.getLast());
                case "3":
                    editShifts(hr,caller,weeks.getLast());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }


    public static void editShifts(HRSystemManager hr,User caller, Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // הצגת אפשרויות לעריכת המשמרת
            System.out.println("Choose an action: ");
            System.out.println("1. Add employee");
            System.out.println("2. Remove employee");
            System.out.println("3. Add role");
            System.out.println("4. Remove role");
            System.out.println("5. cancel a shift");
            System.out.println("0. Back");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    hr.addEmployeeToShift(caller,week);
                    break;
                case "2":
                    hr.removeRoleFromShift(caller,week);
                    break;
                case "3":
                    hr.addARoleToShift(caller,week);
                    break;
                case "4":
                    hr.removeRoleFromShift(caller,week);
                    break;
                case "5":
                    hr.cancelShift(caller,week);
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

    private static void generateReports(HRSystemManager hr, User caller, Scanner sc,Branch curBranch) {
        System.out.println("\n--- Report Generation ---");
        System.out.println("1. Weekly Report");
        System.out.println("2. Employee Report");
        System.out.println("3. Shift Report");
        System.out.println("0. Back to Main Menu");

        String choice = sc.nextLine();
        try {
            switch (choice) {
                case "1":
                    hr.generateWeeklyReport(caller, curBranch.getWeeks());
                    break;
                case "2":
                    System.out.print("Enter Employee ID: ");
                    int empId = Integer.parseInt(sc.nextLine());
                    hr.generateEmployeeReport(caller, empId,curBranch.getWeeks().getLast());
                    break;
                case "3":
                    hr.generateShiftReport(caller, curBranch.getWeeks().getLast());
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
