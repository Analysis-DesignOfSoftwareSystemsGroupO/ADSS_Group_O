package HR_Mudol.Service;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.*;
import HR_Mudol.domain.*;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * The ReportGenerator class is responsible for generating various reports related to employee shifts, weekly activities,
 * and specific employee details. It implements the {@link IReportGenerator} interface.
 */
public class ReportGenerator implements IReportGenerator {

    private final IEmployeeController empM;
    private final IWeekController weekM;
    private final IRoleController roleM;


    /**
     * Constructor to initialize the ReportGenerator with necessary managers.
     *
     * @param weekM The WeekManager used for week-related operations.
     * @param empM  The EmployeeManager used for employee-related operations.
     */
    public ReportGenerator(IWeekController weekM, IEmployeeController empM,IRoleController roleM) {
        this.weekM = weekM;
        this.empM = empM;
        this.roleM=roleM;
    }


    /**
     * Generates a weekly report for the specified weeks. The report includes shift assignments and their details.
     * The user is prompted to enter a date, and the report for the corresponding week is generated.
     *
     * @param caller The user requesting the report (e.g., HR manager or admin).
     * @param weeks  The list of weeks to search for the required week.
     */
    @Override
    public void generateWeeklyReport(UserDTO caller, List<WeekDTO> weeks) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate time = null;

        // בקשת תאריך מהמשתמש
        while (time == null) {
            System.out.print("Enter date to search for a week (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            try {
                time = LocalDate.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        WeekDTO targetWeek = null;

        // חיפוש השבוע לפי תאריך
        for (WeekDTO week : weeks) {
            if (week.getConstraintDeadline().toLocalDate().equals(time)) {
                targetWeek = week;
                break;
            }
        }

        if (targetWeek == null) {
            System.out.println("Week for " + time + " not found.");
            return;
        }

        System.out.println("=== Weekly Report for Week - " + time + " ===");

        for (ShiftDTO shift : targetWeek.getShifts()) {
            System.out.println("Shift ID: " + shift.getShiftID());
            System.out.println("Day: " + shift.getDay());
            System.out.println("Type: " + shift.getType());
            System.out.println("Status: " + shift.getStatus());
            System.out.println("Manager ID: " + shift.getShiftManagerId());
            System.out.println("-------------------------------------------------");
        }
    }


    /**
     * Generates a report for a specific employee. The report includes the employee's personal details, shifts,
     * roles, and available vacation and sick days.
     *
     * @param caller The user requesting the report (e.g., HR manager or admin).
     * @param empId  The ID of the employee for which the report is generated.
     * @param curWeek The current week for which the employee's shift details are requested.
     */
    @Override
    public void generateEmployeeReport(UserDTO caller, int empId, WeekDTO curWeek) throws SQLException {
        EmployeeDTO employee = empM.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee with ID " + empId + " not found.");
            return;
        }

        System.out.println("===== Employee Report =====");
        System.out.println("Name: " + employee.getFullName());
        System.out.println("ID: " + employee.getEmployeeId());
        System.out.println("Bank Account: " + employee.getBankAccount());
        System.out.println("Salary: " + employee.getSalary());
        System.out.println("Start Date: " + employee.getStartDate());
        System.out.println("Vacation Days Left: " + employee.getDaysOff());
        System.out.println("Sick Days Left: " + employee.getSickDays());

        List<ShiftDTO> shifts = weekM.getShiftsForEmployee(employee, curWeek);  // מקבל DTO של shift
        if (shifts == null || shifts.isEmpty()) {
            System.out.println("No shifts assigned.");
        } else {
            System.out.println("Assigned Shifts:");
            for (ShiftDTO s : shifts) {
                System.out.println(" # Shift " + s.getType() + " - " + s.getDay());
            }
        }
    }

    /**
     * Generates a shift report for a specific week. The user is prompted to input a day of the week and a shift type
     * (morning or evening). The report includes shift details and assigned employees.
     *
     * @param caller The user requesting the report (e.g., HR manager or admin).
     * @param curWeek The current week for which the shift report is generated.
     */
    @Override
    public void generateShiftReport(UserDTO caller, WeekDTO curWeek) {

        askAndGenerateShiftReport(caller, curWeek);

    }

    /**
     * Prompts the user to input a day and shift type, then generates the shift report for the corresponding day and shift type.
     *
     * @param caller The user requesting the shift report.
     * @param curWeek The current week for which the shift report is generated.
     */
    private static void askAndGenerateShiftReport(UserDTO caller, WeekDTO curWeek) {
        Scanner scanner = new Scanner(System.in);

        // בחר יום
        WeekDay day = null;
        while (day == null) {
            System.out.println("Enter day of the week (e.g., SUNDAY, MONDAY, ...):");
            String dayInput = scanner.nextLine().trim().toUpperCase();
            try {
                day = WeekDay.valueOf(dayInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid day. Please try again.");
            }
        }

        // בחר סוג משמרת
        ShiftType type = null;
        while (type == null) {
            System.out.println("Enter shift type (MORNING / EVENING):");
            String typeInput = scanner.nextLine().trim().toUpperCase();
            try {
                type = ShiftType.valueOf(typeInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid shift type. Please try again.");
            }
        }

        // קריאה לדוח
        shiftReport(type, day, curWeek);
    }

    private static void shiftReport(ShiftType type, WeekDay day, WeekDTO curWeek) {
        if (curWeek == null || curWeek.getShifts() == null) {
            System.out.println("Week or shifts are not available.");
            return;
        }

        ShiftDTO foundShift = null;

        for (ShiftDTO shift : curWeek.getShifts()) {
            try {
                WeekDay shiftDay = WeekDay.valueOf(shift.getDay().toUpperCase());
                ShiftType shiftType = ShiftType.valueOf(shift.getType().toUpperCase());

                if (shiftDay == day && shiftType == type) {
                    foundShift = shift;
                    break;
                }
            } catch (IllegalArgumentException e) {
                // אם הערך ב־DTO אינו חוקי כ־enum
                System.out.println("Invalid shift data format in DTO.");
            }
        }

        if (foundShift == null) {
            System.out.println("No shift found on " + day + " - " + type);
            return;
        }

        System.out.println("===== Shift Report =====");
        System.out.println("Day: " + day);
        System.out.println("Type: " + type);
        System.out.println("Status: " + foundShift.getStatus());

        System.out.println("\nRequired Roles:");
        for (RoleDTO role : foundShift.getNecessaryRoles()) {
            System.out.println("- " + role.getDescription());
        }

        System.out.println("\nAssigned Employees:");
        for (EmployeeDTO emp : foundShift.getEmployees()) {
            System.out.println("- " + emp.getFullName());
        }
    }

    @Override
    public void generateReports(UserDTO caller, BranchDTO branch, String reportType) {
        Scanner sc = new Scanner(System.in);

        try {
            switch (reportType) {
                case "WEEKLY" -> printWeeks(weekM.getCurrentWeekDTO(), "Weekly Shifts");

                case "FUTURE" -> printWeeks(weekM.getNextWeekDTO(), "Future Unassigned Shifts");

                default -> System.out.println("Unknown report type: " + reportType);
            }

        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
        }

        private void printWeeks(WeekDTO week, String title) {
            System.out.println("\n=== " + title + " ===");
            for (ShiftDTO shift : week.getShifts()) {
                System.out.println("Shift ID: " + shift.getShiftID() +
                        " | Day: " + shift.getDay() +
                        " | Type: " + shift.getType() +
                        " | Status: " + shift.getStatus());
            }
        }

    private void printShifts(List<ShiftDTO> shifts, String title) {
        System.out.println("\n=== " + title + " ===");
        for (ShiftDTO shift : shifts) {
            System.out.println("Shift ID: " + shift.getShiftID() +
                    " | Day: " + shift.getDay() +
                    " | Type: " + shift.getType() +
                    " | Status: " + shift.getStatus());
        }
    }

        private LocalDate promptDate(String message) {
            Scanner sc = new Scanner(System.in);
            System.out.print(message);
            return LocalDate.parse(sc.nextLine().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

    }

