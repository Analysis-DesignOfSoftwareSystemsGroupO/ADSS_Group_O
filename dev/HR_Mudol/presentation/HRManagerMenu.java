package HR_Mudol.presentation;

import HR_Mudol.DTO.*;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Controllers.RoleController;
import HR_Mudol.domain.Objects.Employee;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HRManagerMenu implements Menu {

    private final HRService hr;

    public HRManagerMenu(HRService hrService) {
        this.hr = hrService;
    }

    @Override
    public boolean start(UserDTO caller, EmployeeDTO self, BranchDTO curBranch) throws SQLException {
        if (!caller.isHRManager()) {
            System.out.println("Access denied.");
            return false;        }

        Scanner scanner = new Scanner(System.in);

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
                case "1" -> manageEmployees(caller);
                case "2" -> viewShiftsHistory(caller, curBranch);
                case "3" -> generateReports(caller, curBranch);
                case "4" -> manageShift(caller);
                case "5" -> manageRoles(caller);
                case "6" -> displayDashboard(caller, curBranch);
                case "0" -> {
                    System.out.println("Logging out. Returning to login screen.");
                    hr.close();
                    return true;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void manageEmployees(UserDTO caller) {
        Scanner sc = new Scanner(System.in);
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
                    case "1": hr.addEmployee(caller); break;
                    case "2": hr.removeEmployee(caller); break;
                    case "3": hr.updateBankAccount(caller); break;
                    case "4": hr.updateSalary(caller); break;
                    case "5": hr.printAllEmployees(caller); break;
                    case "0": return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void viewShiftsHistory(UserDTO caller, BranchDTO branch) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate from, to;

        try {
            System.out.print("Enter start date (yyyy-MM-dd): ");
            from = LocalDate.parse(sc.nextLine(), formatter);
            System.out.print("Enter end date (yyyy-MM-dd): ");
            to = LocalDate.parse(sc.nextLine(), formatter);
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
            return;
        }

        List<ShiftDTO> shiftsInRange;
        try {
            shiftsInRange = hr.getShiftsInDateRange(branch, from, to);
        } catch (SQLException e) {
            System.out.println("Error retrieving shifts: " + e.getMessage());
            return;
        }

        if (shiftsInRange.isEmpty()) {
            System.out.println("No shifts found in the selected date range.");
            return;
        }

        System.out.println("\nShift History Report for Branch " + branch.getBranchID() +
                " between " + from + " and " + to + ":\n");

        for (ShiftDTO shift : shiftsInRange) {
            System.out.println("Shift ID: " + shift.getShiftID());
            System.out.println("Date: " + shift.getDay());
            System.out.println("Type: " + shift.getType());
            System.out.println("Status: " + shift.getStatus());
            System.out.println("Manager ID: " + (shift.getShiftManagerId() != -1 ? shift.getShiftManagerId() : "None"));

            System.out.println("Assigned Employees:");
            for (EmployeeDTO emp : shift.getEmployees()) {
                System.out.println("  - " + emp.getFullName() + " (ID: " + emp.getEmployeeId() + ")");
            }

            shift.printRole();

            System.out.println("--------------------------------------------------");
        }
    }


    private void generateReports(UserDTO caller, BranchDTO branch) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Report Generation ---");
        System.out.println("1. Weekly Report");
        System.out.println("2. Future Unassigned Shifts");
        System.out.println("0. Back");

        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> hr.generateReports(caller, branch, "WEEKLY");
            case "2" -> hr.generateReports(caller, branch, "FUTURE");
            case "0" -> {
                return;
            }
            default -> System.out.println("Invalid option.");
        }
    }

    private void manageRoles(UserDTO caller) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Role Management ---");
            System.out.println("1. Create Role");
            System.out.println("2. Assign Employee to Role");
            System.out.println("3. Remove Employee from Role");
            System.out.println("4. Assign Employee to Shift Manager");
            System.out.println("5. Print All Roles");
            System.out.println("6. Delete Role");
            System.out.println("0. Back");

            String choice = sc.nextLine();
            try {
                switch (choice) {
                    case "1" -> hr.createRole(caller);
                    case "2" -> hr.assignEmployeeToRole(caller);
                    case "3" -> hr.removeEmployeeFromRole(caller,sc);
                    case "4" -> hr.assignEmployeeToShiftManager(caller);
                    case "5" -> hr.printAllRoles(caller);
                    case "6" -> {
                        System.out.print("Enter role description to delete: ");
                        String desc = sc.nextLine().trim();
                        hr.deleteRole(caller, desc);
                    }

                    case "0" -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void manageShift(UserDTO caller) throws SQLException {
        WeekDTO week=hr.createNewWeek(caller);
        if (week == null) {
            System.out.println("No current week available.");
            return;
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Shift Management ---");
            System.out.println("1. Assigning roles to weekly shifts");
            System.out.println("2. Assigning employees to weekly shifts");
            System.out.println("3. Edit shifts");
            System.out.println("0. Back");

            String choice = sc.nextLine();
            try {
                switch (choice) {
                    case "1" -> hr.manageTheWeekRelevantRoles(caller,week);
                    case "2" -> hr.assigningEmployToShifts(caller);
                    case "3" -> editShifts(caller,week);
                    case "0" -> { return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void editShifts(UserDTO caller, WeekDTO week) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an action: ");
            System.out.println("1. Add employee");
            System.out.println("2. Remove employee");
            System.out.println("3. Add role");
            System.out.println("4. Remove role");
            System.out.println("5. Cancel a shift");
            System.out.println("0. Back");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> hr.addEmployeeToShift(caller, week);
                case "2" -> hr.removeEmployeeFromShift(caller, week);
                case "3" -> hr.addARoleToShift(caller, week);
                case "4" -> hr.removeRoleFromShift(caller, week);
                case "5" -> hr.cancelShift(caller, week);
                case "0" -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void displayDashboard(UserDTO caller, BranchDTO branch) {
        WeekDTO week = hr.getCurrentWeekDTO();
        if (week == null) {
            System.out.println("No current week available.");
            return;
        }
        try {
            hr.displayDashboard(caller, week);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
