package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ReportGenerator implements IReportGenerator {

    private EmployeeManager empM;
    private WeekManager weekM;

    public ReportGenerator(WeekManager weekM, EmployeeManager empM) {
        this.weekM = weekM;
        this.empM = empM;
    }

    @Override
    public void generateWeeklyReport(User caller, List<Week> weeks) {
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

        Week targetWeek = null;

        // חיפוש השבוע לפי תאריך
        for (Week week : weeks) {
            if (week.getConstraintDeadline().equals(time)) {
                targetWeek = week;
                break;
            }
        }

        if (targetWeek == null) {
            System.out.println("Week for " + time + " not found.");
            return;
        }

        System.out.println("=== Weekly Report for Week - " + time + " ===");
        for (Shift shift : targetWeek.getShifts()) {
            System.out.println(shift);
            System.out.println("-------------------------------------------------");
        }
    }


    @Override
    public void generateEmployeeReport(User caller, int empId, Week curWeek) {
        Employee employee = empM.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee with ID " + empId + " not found.");
            return;
        }

        System.out.println("===== Employee Report =====");
        System.out.println("Name: " + employee.getEmpName());
        System.out.println("ID: " + employee.getEmpId());
        System.out.println("Bank Account: " + employee.getEmpBankAccount());
        System.out.println("Salary: " + employee.getEmpSalary());
        System.out.println("Start Date: " + employee.getEmpStartDate());
        System.out.println("Vacation Days Left: " + employee.getDaysOff(caller));
        System.out.println("Sick Days Left: " + employee.getSickDays(caller));

        List<Shift> shifts = weekM.getShiftsForEmployee(employee,curWeek);
        if (shifts.isEmpty()) {
            System.out.println("No shifts assigned.");
        } else {
            System.out.println("Assigned Shifts:");
            for (Shift s : shifts) {
                System.out.println(" # Shift " + s.getType() + "-"+s.getDay());
            }
        }
    }

    @Override
    public void generateShiftReport(User caller, Week curWeek) {

        askAndGenerateShiftReport(caller, curWeek);

    }

    private static void askAndGenerateShiftReport(User caller, Week curWeek) {
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

    private static void shiftReport(ShiftType type, WeekDay day, Week curWeek){

        if (curWeek == null) {
            System.out.println("No current week provided.");
            return;
        }

        Shift foundShift = curWeek.getAShift(day, type);

        if (foundShift == null) {
            System.out.println("No shift found on " + day + " - " + type);
            return;
        }

        System.out.println("===== Shift Report =====");
        System.out.println("Day: " + day);
        System.out.println("Type: " + type);
        System.out.println("Status: " + foundShift.getStatus());

        System.out.println("\nRequired Roles:");
        for (Role role : foundShift.getNecessaryRoles()) {
            System.out.println("- " + role.getDescription());
        }

        System.out.println("\nAssigned Employees:");
        for (Employee emp : foundShift.getEmployees()) {
            System.out.println("- " + emp.getEmpName());
        }
    }

}
