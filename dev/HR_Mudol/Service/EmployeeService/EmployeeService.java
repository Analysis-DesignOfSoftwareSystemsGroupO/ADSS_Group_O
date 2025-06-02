
package HR_Mudol.Service.EmployeeService;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.*;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;

import java.sql.SQLException;
import java.util.*;

public class EmployeeService implements IEmployeeService {

    private BranchDTO branchDTO;
    private final Scanner scanner;
    private final EmployeeController empController;
    private final WeekController weekController;

    public EmployeeService(BranchDTO branch) throws SQLException {

        this.branchDTO=branch;
        this.scanner = new Scanner(System.in);
        this.empController = new EmployeeController(branch);
        IRoleController r= new RoleController(branch);
        this.weekController= new WeekController(new ShiftController(branch,r),branch,r);
    }

    @Override
    public void close() {
        empController.close();
    }

    public void viewMyShifts(UserDTO caller, long empId) throws SQLException {
        EmployeeDTO employee = empController.getEmployeeById(caller, empId);
        if (employee == null ) {
            System.out.println("Error: employee not available.");
            return;
        }

        System.out.println("Shifts for " + employee.getFullName() + ":");

        List<ShiftDTO> currentWeek= weekController.getCurrentWeekShifts();

        boolean found = false;
        for (ShiftDTO shift : currentWeek) {
               if (shift.getEmployeeIds().contains(empId)) {
                System.out.println("- " + shift);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No assigned shifts found for the upcoming week.");
        }
    }

    public void submitConstraint(UserDTO caller, long empId, WeekDTO currentWeek) throws SQLException {
        if (!currentWeek.isConstraintSubmissionOpen()) {
            empController.lockWeeklyConstraints(empId);
            System.out.println("Constraint submission is now closed.");
            System.out.println("You are now submitting constraints for the new upcoming week.");
        }

        int morningLimit = empController.getMinDayShifts(empId);
        int eveningLimit = empController.getMinEveningShifts(empId);

        List<ConstraintDTO> submitted = new ArrayList<>();

        System.out.println("Now starting constraint submission for MORNING shifts:");
        handleConstraintSubmission(empId, ShiftType.MORNING, morningLimit, submitted);

        System.out.println("Now starting constraint submission for EVENING shifts:");
        handleConstraintSubmission(empId, ShiftType.EVENING, eveningLimit, submitted);

        printSummary(submitted);
    }

    private void handleConstraintSubmission(long empId, ShiftType type, int shiftLimit, List<ConstraintDTO> submitted) throws SQLException {
        int shiftCount = 0;

        for (WeekDay day : WeekDay.values()) {
            if ((type == ShiftType.EVENING && (day == WeekDay.FRIDAY || day == WeekDay.SATURDAY)) ||
                    (type == ShiftType.MORNING && day == WeekDay.SATURDAY)) continue;

            if (shiftCount >= shiftLimit) {
                System.out.println("You've reached the limit for " + type + " shifts.");
                break;
            }

            System.out.println("Do you want to submit a constraint for " + day + " (" + type + ")? (yes/no)");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("yes")) continue;

            System.out.println("Enter explanation:");
            String explanation = scanner.nextLine().trim();

            ConstraintDTO constraint = new ConstraintDTO(empId, explanation, day.name(), type.name());
            empController.submitConstraint(empId, constraint);
            submitted.add(constraint);
            shiftCount++;
            System.out.println("Constraint submitted for " + day + " (" + type + ").");
        }
    }

    private void printSummary(List<ConstraintDTO> constraints) {
        long morning = constraints.stream()
                .filter(c -> ShiftType.valueOf(c.getType().toUpperCase()) == ShiftType.MORNING)
                .count();

        long evening = constraints.stream()
                .filter(c -> ShiftType.valueOf(c.getType().toUpperCase()) == ShiftType.EVENING)
                .count();

        System.out.println("Finished submitting constraints for the week.");
        System.out.println("Total constraints submitted: " + constraints.size());
        System.out.println(" - Morning shifts: " + morning);
        System.out.println(" - Evening shifts: " + evening);
    }

    @Override
    public void changePassword(UserDTO caller, long empId) throws SQLException {
        EmployeeDTO employee = empController.getEmployeeById(caller, empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        if (caller.getUserId() != employee.getEmployeeId()) {
            throw new SecurityException("Access denied: You may only change your own password.");
        }

        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine().trim();

        if (!empController.verifyPassword(caller, empId, currentPassword)) {
            System.out.println("Incorrect current password. Password change aborted.");
            return;
        }

        String newPassword;
        while (true) {
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine().trim();
            if (!newPassword.isEmpty()) break;
            System.out.println("Password cannot be empty. Please try again.");
        }

        empController.updatePassword(caller, empId, newPassword);
        System.out.println("Password updated successfully.");
    }

    @Override
    public void viewContractDetails(UserDTO caller, long empId) {
        try {
            EmploymentContractDTO contract = empController.getContractDetails(caller, empId);
            if (contract == null) {
                System.out.println("Employee not found or access denied.");
                return;
            }

            System.out.println("\n--- Employment Contract Details ---");
            System.out.println("Minimum Day Shifts: " + contract.getMinDayShift());
            System.out.println("Minimum Evening Shifts: " + contract.getMinEveningShift());
            System.out.println("Sick Days: " + contract.getSickDays());
            System.out.println("Days Off: " + contract.getDaysOff());

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    @Override
    public void viewMyConstraints(UserDTO caller, long employeeId) {
        List<ConstraintDTO> constraints = empController.getConstraintsByEmployeeId(employeeId);
        if (constraints.isEmpty()) {
            System.out.println("No constraints found.");
            return;
        }

        System.out.println("--- Current Constraints ---");
        for (int i = 0; i < constraints.size(); i++) {
            ConstraintDTO c = constraints.get(i);
            System.out.println((i + 1) + ". " + c.getDay() + " - " + c.getType() + " - " + c.getExplanation());
        }
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() throws SQLException {
        return empController.getAllEmployees();
    }

    @Override
    public int getTotalEmployeeCount() throws SQLException {
        return empController.getAllEmployees().size();
    }


    @Override
    public void viewPersonalDetails(UserDTO caller, long employeeId) throws SQLException {
        if (!caller.isRegularEmployee()) {
            throw new SecurityException("Access denied: Only HR managers can view other employees' personal details.");
        }

        EmployeeDTO employee = empController.getEmployeeById(caller, employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\n--- Employee Personal Details ---");
        System.out.println(employee);
    }

    @Override
    public void viewAvailableRoles(UserDTO caller, long employeeId) {
        List<RoleDTO> roles = empController.getRolesForEmployee(employeeId);
        if (roles.isEmpty()) {
            System.out.println("No roles available.");
            return;
        }

        System.out.println("--- Available Roles ---");
        for (RoleDTO role : roles) {
            System.out.println(role.getRoleNumber() + ": " + role.getDescription());
        }
    }
    @Override
    public void updateConstraint(UserDTO caller, long empId, WeekDTO currentWeek) {
        try {
            EmployeeDTO employee = empController.getEmployeeById(caller, empId);
            if (employee == null) {
                System.out.println("Employee not found.");
                return;
            }

            if (caller.getUserId() != empId) {
                throw new SecurityException("Employees may only edit their own constraints.");
            }

            boolean continueEditing = true;

            while (continueEditing) {
                ShiftType selectedType = promptShiftType();
                if (selectedType == null) break;

                boolean typeEditing = true;
                while (typeEditing) {
                    List<ConstraintDTO> constraints = empController.getConstraintsByType(empId, selectedType);

                    if (constraints.isEmpty()) {
                        System.out.println("No constraints found for " + selectedType + " shifts.");
                        break;
                    }

                    printConstraints(constraints, selectedType.name());

                    System.out.println("\nSelect an action:");
                    System.out.println("1. Update explanation");
                    System.out.println("2. Remove constraint");
                    System.out.println("3. Back");

                    String action = scanner.nextLine().trim();
                    if (action.equals("3")) break;

                    if (!action.equals("1") && !action.equals("2")) {
                        System.out.println("Invalid option.");
                        continue;
                    }

                    int index = promptConstraintIndex(constraints.size());
                    if (index == -1) continue;

                    ConstraintDTO selected = constraints.get(index);

                    boolean isAssigned = currentWeek.getShifts().stream()
                            .anyMatch(s -> s.getDay().equalsIgnoreCase(selected.getDay())
                                    && s.getType().equalsIgnoreCase(selected.getType())
                                    && s.getEmployeeIds().contains(empId));

                    if (isAssigned) {
                        System.out.println("You have already been assigned to this shift. Cannot modify/remove constraint.");
                        continue;
                    }

                    if (action.equals("1")) {
                        System.out.print("Enter new explanation: ");
                        String newExp = scanner.nextLine().trim();

                        if (selected.getExplanation().contains("sick day"))
                            newExp += " (used sick day)";
                        else if (selected.getExplanation().contains("day off"))
                            newExp += " (used day off)";

                        empController.updateConstraintExplanation(employee, selected, newExp);
                        System.out.println("✅ Explanation updated successfully.");
                    } else {
                        empController.removeConstraint(empId, selected);
                        System.out.println("✅ Constraint removed.");
                    }

                    // טען מחדש את הרשימה כדי לשקף את השינוי
                    constraints = empController.getConstraintsByType(empId, selectedType);

                    if (constraints.isEmpty()) {
                        System.out.println("No more constraints left for " + selectedType + " shifts.");
                        break;
                    }

                    printConstraints(constraints, selectedType.name());

                    System.out.println("\nDo you want to continue editing constraints of this type? (yes/no)");
                    String cont = scanner.nextLine().trim().toLowerCase();
                    if (!cont.equals("yes")) typeEditing = false;
                }

                System.out.println("\nDo you want to continue editing other shift types? (yes/no)");
                String more = scanner.nextLine().trim().toLowerCase();
                if (!more.equals("yes")) continueEditing = false;
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }



    private ShiftType promptShiftType() {
        while (true) {
            System.out.println("Choose shift type to edit constraints (morning/evening) or 'back':");
            String input = scanner.nextLine().trim().toLowerCase();

            return switch (input) {
                case "morning" -> ShiftType.MORNING;
                case "evening" -> ShiftType.EVENING;
                case "back" -> null;
                default -> {
                    System.out.println("Invalid input.");
                    yield null;
                }
            };
        }
    }

    private int promptConstraintIndex(int maxSize) {
        System.out.print("Enter constraint number to modify/remove: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index < 0 || index >= maxSize) {
                System.out.println("Invalid number.");
                return -1;
            }
            return index;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return -1;
        }
    }

    private void printConstraints(List<ConstraintDTO> constraints, String title) {
        System.out.println("--- " + title + " Constraints ---");
        for (int i = 0; i < constraints.size(); i++) {
            ConstraintDTO c = constraints.get(i);
            System.out.println((i + 1) + ". " + c.getDay() + " - " + c.getExplanation());
        }
    }


}
