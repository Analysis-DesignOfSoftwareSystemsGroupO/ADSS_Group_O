package HR_Mudol.Service.EmployeeSystem;

import HR_Mudol.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeSystem implements IEmployeeSystem {

    private Scanner scanner;


    /**
     * Default constructor initializes the scanner using System.in
     */
    public EmployeeSystem() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructor for injecting a custom scanner (useful for testing purposes)
     */
    public EmployeeSystem(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays the shifts assigned to the employee for the current week
     * @param caller The user making the request
     * @param self The employee whose shifts are to be viewed
     * @param currentWeek The current week containing the shifts
     */
    @Override
    public void viewMyShifts(User caller, Employee self, Week currentWeek) {
        try {
            if (self == null) { // Check if employee exists
                System.out.println("Error: employee does not exist.");
                return;
            }

            if (currentWeek == null) { // Check if week object exists
                System.out.println("Error: current week is not available.");
                return;
            }

            System.out.println("Shifts for " + self.getEmpName() + ":");
            boolean found = false;

            // Iterate through all shifts of the week
            for (Shift shift : currentWeek.getShifts()) {
                if (shift.getEmployees().contains(self)) { // If the employee is assigned to the shift
                    System.out.println("- " + shift);
                    found = true;
                }
            }

            if (!found) { // No shifts assigned
                System.out.println("No assigned shifts found for the upcoming week.");
            }

        } catch (SecurityException se) { // Handle permission errors
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) { // Handle unexpected errors
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    /**
     * Allows an employee to submit their shift constraints for the week
     * @param caller The user making the request
     * @param self The employee submitting constraints
     * @param currentWeek The current week for which the constraints are being submitted
     */
    @Override
    public void submitConstraint(User caller, Employee self, Week currentWeek) {
        try {
            if (!caller.isSameEmployee(self)) { // Ensure the employee submits their own constraints
                throw new SecurityException("Employees may only submit constraints for themselves.");
            }

            if (!currentWeek.isConstraintSubmissionOpen()) { // Handle constraint submission deadline
                self.lockWeeklyConstraints(caller);
                System.out.println("Constraint submission is now closed.");
                System.out.println("You are now submitting constraints for the new upcoming week.");
            }

            int dayShiftLimit = self.getMinDayShift(caller);
            int eveningShiftLimit = self.getMinEveninigShift(caller);

            List<Constraint> submittedConstraints = new ArrayList<>();

            System.out.println("Now starting constraint submission for MORNING shifts:");
            int dayShiftCount = handleShiftTypeConstraints(caller, self, ShiftType.MORNING, dayShiftLimit, submittedConstraints);

            System.out.println("Now starting constraint submission for EVENING shifts:");
            int eveningShiftCount = handleShiftTypeConstraints(caller, self, ShiftType.EVENING, eveningShiftLimit, submittedConstraints);

            // Print submission summary
            System.out.println("Finished submitting constraints for the week.");
            long morningCount = submittedConstraints.stream().filter(c -> c.getType() == ShiftType.MORNING).count();
            long eveningCount = submittedConstraints.stream().filter(c -> c.getType() == ShiftType.EVENING).count();
            System.out.println("Total constraints submitted: " + submittedConstraints.size());
            System.out.println(" - Morning shifts: " + morningCount);
            System.out.println(" - Evening shifts: " + eveningCount);

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }

    /**
     * Handles the submission of constraints for a specific shift type (morning or evening)
     * @param caller The user making the request
     * @param self The employee submitting constraints
     * @param type The shift type (morning or evening)
     * @param shiftLimit The limit on the number of shifts the employee can submit constraints for
     * @param submittedConstraints The list of submitted constraints
     * @return The total number of shifts submitted
     */
    private int handleShiftTypeConstraints(User caller, Employee self, ShiftType type, int shiftLimit, List<Constraint> submittedConstraints) {
        int shiftCount = 0;

        for (WeekDay day : WeekDay.values()) { // Loop over all weekdays
            if ((type == ShiftType.EVENING && (day == WeekDay.FRIDAY || day == WeekDay.SATURDAY)) ||
                    (type == ShiftType.MORNING && day == WeekDay.SATURDAY)) {
                continue; // Skip illegal days
            }

            int remaining = Math.max(0, shiftLimit - shiftCount);
            System.out.println("You can still submit " + remaining + " constraint(s) for " + type + " shifts.");

            String answer;
            while (true) { // Force yes/no input
                System.out.println("Do you want to submit a constraint for " + day + " (" + type + ")? (yes/no)");
                answer = scanner.nextLine().trim().toLowerCase();
                if (answer.equals("yes") || answer.equals("no")) break;
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }

            if (!answer.equals("yes")) continue;

            System.out.println("Enter explanation for the constraint:");
            String explanation = scanner.nextLine().trim();

            shiftCount++;
            boolean exceeds = shiftCount > shiftLimit;
            String choiceUsed = null;

            if (exceeds) { // If exceeding contract limits
                System.out.println("Warning: You're exceeding your contract shift limits.");
                System.out.println("You have " + self.getSickDays(caller) + " sick days and " + self.getDaysOff(caller) + " days off remaining.");

                while (true) { // Handle choice of using sick day, day off, or cancelling constraint
                    System.out.println("Do you want to use a sick day or a day off? (sick/off/cancel)");
                    String choice = scanner.nextLine().trim().toLowerCase();

                    switch (choice) {
                        case "sick":
                            int sickDays = self.getSickDays(caller);
                            if (sickDays > 0) {
                                self.setSickDays(caller, sickDays - 1);
                                choiceUsed = "sick";
                                shiftCount--;
                                System.out.println("One sick day used.");
                                break;
                            } else {
                                System.out.println("No sick days left.");
                            }
                            break;
                        case "off":
                            int daysOff = self.getDaysOff(caller);
                            if (daysOff > 0) {
                                self.setDaysOff(caller, daysOff - 1);
                                choiceUsed = "off";
                                shiftCount--;
                                System.out.println("One day off used.");
                                break;
                            } else {
                                System.out.println("No days off left.");
                            }
                            break;
                        case "cancel":
                            System.out.println("Constraint cancelled.");
                            shiftCount--;
                            return shiftCount; // Exit if cancelled
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }

                    if (choiceUsed != null) break;
                }
            }

            if (choiceUsed != null) {
                explanation += " (used " + (choiceUsed.equals("sick") ? "sick day" : "day off") + ")";
            }

            // Create and save constraint
            Constraint constraint = new Constraint(explanation, day, type);
            self.addNewConstraints(caller, constraint);
            if (type == ShiftType.MORNING) {
                self.addNewMorningConstraints(caller, constraint);
            } else {
                self.addNewEveningConstraints(caller, constraint);
            }
            submittedConstraints.add(constraint);
            System.out.println("Constraint submitted for " + day + " (" + type + ").");
        }

        return shiftCount;
    }


    /**
     * Prints the list of constraints for a specific shift type
     * @param constraints The list of constraints to display
     * @param title The title of the constraints (morning/evening)
     */
    private void printConstraintsList(List<Constraint> constraints, String title) {
        if (constraints.isEmpty()) {
            System.out.println("No " + title + " constraints found."); // No constraints to show
            return;
        }

        System.out.println("Current " + title + " constraints:");
        for (int i = 0; i < constraints.size(); i++) { // Loop through each constraint
            Constraint c = constraints.get(i);
            System.out.println((i + 1) + ". " + c.getDay() + " - " + c.getExplanation());
        }
    }


    /**
     * Allows an employee to update or remove their own shift constraints for a given week.
     *
     * This method ensures that:
     *
     *   Employees can only edit their own constraints.
     *   Constraints associated with assigned shifts cannot be changed or removed.
     *   When removing a constraint, any associated sick day or day off is restored.
     *
     * @param caller The user requesting the update (must be the same as 'self').
     * @param self The employee whose constraints are being updated.
     * @param currentWeek The current week containing assigned shifts.
     */
    @Override
    public void updateConstraint(User caller, Employee self, Week currentWeek) {
        try {
            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Employees may only edit their own constraints."); // Only the employee himself can edit
            }

            while (true) { // Main loop for selecting shift type
                ShiftType selectedType = null;

                // Ask which type of shift to edit
                while (selectedType == null) {
                    System.out.println("Which shift type do you want to edit constraints for? (morning/evening) or 'back' to exit:");
                    String typeChoice = scanner.nextLine().trim().toLowerCase();

                    switch (typeChoice) {
                        case "morning":
                            selectedType = ShiftType.MORNING;
                            break;
                        case "evening":
                            selectedType = ShiftType.EVENING;
                            break;
                        case "back":
                            System.out.println("Returning to main menu.");
                            return;
                        default:
                            System.out.println("Invalid input. Please enter 'morning', 'evening' or 'back'.");
                    }
                }

                // Select the correct list based on shift type
                List<Constraint> relevantList = (selectedType == ShiftType.MORNING)
                        ? self.getMorningConstraints(caller)
                        : self.getEveningConstraints(caller);

                if (relevantList.isEmpty()) {
                    System.out.println("No constraints found for " + selectedType + " shifts.");
                    continue; // Go back to selecting shift type
                }

                while (true) { // Inner loop for editing/removing constraints
                    printConstraintsList(relevantList, selectedType + " shift");

                    System.out.println("\nSelect an action:");
                    System.out.println("1. Update explanation of an existing constraint");
                    System.out.println("2. Remove an existing constraint");
                    System.out.println("3. Back to shift type selection");

                    String action = scanner.nextLine().trim();

                    if (action.equals("3")) {
                        break; // Back to selecting shift type
                    }

                    if (!action.equals("1") && !action.equals("2")) {
                        System.out.println("Invalid input. Please choose 1, 2, or 3.");
                        continue;
                    }

                    if (relevantList.isEmpty()) {
                        System.out.println("No constraints left to modify or remove.");
                        break;
                    }

                    int index = -1;

                    while (true) { // Ask for constraint number
                        System.out.print("Enter number of constraint to modify/remove: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                            if (index < 0 || index >= relevantList.size()) {
                                System.out.println("Invalid selection. Please choose a valid number.");
                            } else {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }

                    Constraint selected = relevantList.get(index);

                    // Check if the employee has already been assigned to this shift
                    boolean assigned = currentWeek.getShifts().stream()
                            .filter(s -> s.getDay() == selected.getDay() && s.getType() == selected.getType())
                            .anyMatch(s -> s.getEmployees().contains(self));

                    if (assigned) {
                        System.out.println("You have already been assigned to this shift: " +
                                selected.getDay() + " (" + selected.getType() + "). Constraint cannot be modified or removed.");
                        continue;
                    }

                    if (action.equals("1")) {
                        System.out.print("Enter new explanation: ");
                        String newExplanation = scanner.nextLine().trim();

                        // Preserve special tags (sick day / day off)
                        if (selected.getExplanation().contains("(used sick day)")) {
                            newExplanation += " (used sick day)";
                        } else if (selected.getExplanation().contains("(used day off)")) {
                            newExplanation += " (used day off)";
                        }

                        selected.setExplanation(caller, self, newExplanation);
                        System.out.println("Explanation updated.");

                    } else { // action.equals("2")
                        relevantList.remove(index);
                        self.getWeeklyConstraints(caller).remove(selected);
                        System.out.println("Constraint removed.");

                        // Restore sick day or day off if necessary
                        if (selected.getExplanation().contains("(used sick day)")) {
                            self.setSickDays(caller, self.getSickDays(caller) + 1);
                            System.out.println("Sick day restored.");
                        } else if (selected.getExplanation().contains("(used day off)")) {
                            self.setDaysOff(caller, self.getDaysOff(caller) + 1);
                            System.out.println("Day off restored.");
                        }

                        if (relevantList.isEmpty()) {
                            System.out.println("No more constraints left for " + selectedType + " shifts.");
                            break; // Break out to shift type selection
                        }
                    }
                }
            }
        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }



    /**
     * Displays all submitted shift constraints for the employee.
     *
     * @param caller The user requesting the view (must be a manager or the employee himself).
     * @param self The employee whose constraints are being viewed.
     */
    @Override
    public void viewMyConstraints(User caller, Employee self) {
        try {
            if (!caller.isManager() && !caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: You may only view your own constraints.");
            }

            System.out.println("Which constraints would you like to view?");
            System.out.println("1. Current week's submitted constraints");
            System.out.println("2. Locked (past) constraints");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                List<Constraint> morning = self.getMorningConstraints(caller);
                List<Constraint> evening = self.getEveningConstraints(caller);

                System.out.println("\n=== Current Week Constraints ===");

                if (morning.isEmpty() && evening.isEmpty()) {
                    System.out.println("No current week constraints submitted.");
                    return;
                }

                printConstraintsList(morning, "MORNING shift");
                printConstraintsList(evening, "EVENING shift");

            } else if (input.equals("2")) {
                List<Constraint> locked = self.getLockedConstraintsAllowSelfView(caller);

                System.out.println("\n=== Locked Constraints ===");

                if (locked.isEmpty()) {
                    System.out.println("No locked constraints found.");
                    return;
                }

                printConstraintsList(locked, "LOCKED");
            } else {
                System.out.println("Invalid input. Returning to menu.");
            }

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }



    /**
     * Allows the employee to change their password after verifying their current password.
     *
     * @param caller The user requesting the password change (must be the same as 'self').
     * @param self The employee whose password is being changed.
     */
    @Override
    public void changePassword(User caller, Employee self) {
        try {
            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: You may only change your own password.");
            }

            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine().trim();

            // Verify current password
            if (!self.verifyPassword(currentPassword, caller)) {
                System.out.println("Incorrect current password. Password change aborted.");
                return;
            }

            String newPassword;
            while (true) {
                System.out.print("Enter new password: ");
                newPassword = scanner.nextLine().trim();
                if (newPassword.isEmpty()) {
                    System.out.println("Password cannot be empty. Please try again.");
                } else {
                    break;
                }
            }

            self.setEmpPassword(newPassword, caller); // Update the password
            System.out.println("Password updated successfully.");

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }


    /**
     * Returns the contract details of the employee.
     *
     * @param caller The user requesting the contract details (must be a manager or the employee himself).
     * @param self The employee whose contract details are being retrieved.
     * @return A string representing the contract details.
     * @throws SecurityException if the caller is not authorized.
     */
    public String getContractDetails(User caller, Employee self) {
        if (!caller.isManager() && !caller.isSameEmployee(self)) {
            throw new SecurityException("Access denied: Only HR managers or the employee can view contract details.");
        }
        return self.getContract(caller).toString();
    }

    /**
     * Displays the personal details of the employee.
     *
     * @param caller The user requesting the view (must be a manager or the employee himself).
     * @param self The employee whose personal details are being viewed.
     */
    @Override
    public void viewPersonalDetails(User caller, Employee self) {
        if (!caller.isManager() && !caller.isSameEmployee(self)) {
            throw new SecurityException("Access denied: Only HR managers or the employee can view personal details.");
        }
        System.out.println("\n--- Employee Personal Details ---");
        System.out.println(self);  // uses toString()
    }

    /**
     * Displays the employment contract details of the employee.
     *
     * @param caller The user requesting the view (must be authorized).
     * @param self The employee whose contract details are being displayed.
     */
    @Override
    public void viewContractDetails(User caller, Employee self) {
        // Authorization check is inside getContractDetails
        System.out.println("\n--- Employment Contract Details ---");
        System.out.println(getContractDetails(caller, self));
    }

    /**
     * Returns a formatted list of all relevant roles assigned to the employee.
     *
     * @param caller The user requesting the roles (must be a manager or the employee himself).
     * @param self The employee whose roles are being retrieved.
     * @return A string representing the list of relevant roles.
     * @throws SecurityException if the caller is not authorized.
     */
    public String getAvailableRoles(User caller, Employee self) {
        if (!caller.isManager() && !caller.isSameEmployee(self)) {
            throw new SecurityException("Access denied: Only HR managers or the employee can view roles.");
        }

        StringBuilder sb = new StringBuilder("\n--- Relevant Roles ---\n");
        List<Role> roles = self.getRelevantRoles(caller);

        if (roles.isEmpty()) {
            sb.append("No roles assigned.");
        } else {
            for (int i = 0; i < roles.size(); i++) {
                Role role = roles.get(i);
                sb.append((i + 1)).append(". Role number ")
                        .append(role.getRoleNumber())
                        .append(" - ").append(role.getDescription())
                        .append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Displays the relevant roles assigned to the employee.
     *
     * @param caller The user requesting the view.
     * @param self The employee whose roles are being displayed.
     */
    @Override
    public void viewAvailableRoles(User caller, Employee self) {
        try {
            System.out.println(getAvailableRoles(caller, self));
        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }

}
