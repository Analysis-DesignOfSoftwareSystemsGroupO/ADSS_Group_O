package HR_Mudol.Service.EmployeeSystem;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Constraint;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.Week;
import HR_Mudol.domain.Shift;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeSystem implements IEmployeeSystem {

    private Scanner scanner = new Scanner(System.in);

    @Override
    // Updated method signature to receive Week as a parameter
    public void viewMyShifts(User caller, Employee self, Week currentWeek) {
        try {
            if (self == null) {
                System.out.println("Error: employee does not exist.");
                return;
            }

            if (currentWeek == null) {
                System.out.println("Error: current week is not available.");
                return;
            }

            System.out.println("Shifts for " + self.getEmpName() + ":");
            boolean found = false;

            // Iterate over the week's shifts to find ones assigned to this employee
            for (Shift shift : currentWeek.getShifts()) {
                if (shift.getEmployees().contains(self)) {
                    System.out.println("- " + shift);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No assigned shifts found for the upcoming week.");
            }

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }




    @Override
    public void submitConstraint(User caller, Employee self, Week currentWeek) {
        try {
            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Employees may only submit constraints for themselves.");
            }

            // בדיקת דדליין
            if (!currentWeek.isConstraintSubmissionOpen()) {
                System.out.println("Constraint submission is closed. Deadline was: " + currentWeek.getConstraintDeadline());
                return;
            }

            int dayShiftLimit = self.getMinDayShift(caller);
            int eveningShiftLimit = self.getMinEveninigShift(caller);

            List<Constraint> submittedConstraints = new ArrayList<>();

            System.out.println("Now starting constraint submission for MORNING shifts:");
            int dayShiftCount = handleShiftTypeConstraints(caller, self, ShiftType.MORNING, dayShiftLimit, submittedConstraints);

            System.out.println("Now starting constraint submission for EVENING shifts:");
            int eveningShiftCount = handleShiftTypeConstraints(caller, self, ShiftType.EVENING, eveningShiftLimit, submittedConstraints);

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


    private int handleShiftTypeConstraints(User caller, Employee self, ShiftType type, int shiftLimit, List<Constraint> submittedConstraints) {
        int shiftCount = 0;

        for (WeekDay day : WeekDay.values()) {
            if (day == WeekDay.FRIDAY && type == ShiftType.EVENING) continue;

            int remaining = Math.max(0, shiftLimit - shiftCount);
            System.out.println("You can still submit " + remaining + " constraint(s) for " + type + " shifts.");

            String answer;
            while (true) {
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

            if (exceeds) {
                System.out.println("Warning: You're exceeding your contract shift limits.");
                System.out.println("You have " + self.getSickDays(caller) + " sick days and " + self.getDaysOff(caller) + " days off remaining.");

                outerWhile:
                while (true) {
                    System.out.println("Do you want to use a sick day or a day off? (sick/off/cancel)");
                    String choice = scanner.nextLine().trim().toLowerCase();

                    switch (choice) {
                        case "sick":
                            int sickDays = self.getSickDays(caller);
                            if (sickDays > 0) {
                                self.setSickDays(caller, sickDays - 1);
                                choiceUsed = "sick";
                                System.out.println("One sick day used.");
                                shiftCount--;
                                break outerWhile;
                            } else {
                                System.out.println("No sick days left. Please choose another option.");
                                continue;
                            }
                        case "off":
                            int daysOff = self.getDaysOff(caller);
                            if (daysOff > 0) {
                                self.setDaysOff(caller, daysOff - 1);
                                choiceUsed = "off";
                                System.out.println("One day off used.");
                                shiftCount--;
                                break outerWhile;
                            } else {
                                System.out.println("No days off left. Please choose another option.");
                                continue;
                            }
                        case "cancel":
                            List<Constraint> localList = (type == ShiftType.MORNING)
                                    ? self.getMorningConstraints(caller)
                                    : self.getEveningConstraints(caller);

                            System.out.println("Constraint cancelled. Please choose a constraint to remove from the submitted ones:");
                            for (int i = 0; i < localList.size(); i++) {
                                Constraint c = localList.get(i);
                                System.out.println(i + 1 + ". " + c.getDay() + " (" + c.getType() + ") - " + c.getExplanation());
                            }
                            if (localList.isEmpty()) {
                                System.out.println("No constraints to remove.");
                                continue;
                            }
                            System.out.print("Enter number of constraint to remove: ");
                            try {
                                int index = Integer.parseInt(scanner.nextLine()) - 1;
                                if (index >= 0 && index < localList.size()) {
                                    Constraint toRemove = localList.remove(index);
                                    self.getWeeklyConstraints(caller).remove(toRemove);
                                    System.out.println("Removed constraint for " + toRemove.getDay() + " (" + toRemove.getType() + ").");
                                    if (toRemove.getType() == type) shiftCount--;
                                    if (toRemove.getExplanation().contains("(used sick day)")) {
                                        self.setSickDays(caller, self.getSickDays(caller) + 1);
                                        System.out.println("Sick day restored.");
                                    } else if (toRemove.getExplanation().contains("(used day off)")) {
                                        self.setDaysOff(caller, self.getDaysOff(caller) + 1);
                                        System.out.println("Day off restored.");
                                    }
                                    break outerWhile;
                                } else {
                                    System.out.println("Invalid number. Skipping removal.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Skipping removal.");
                            }
                            continue;
                        default:
                            System.out.println("Invalid choice. Please enter 'sick', 'off', or 'cancel'.");
                            continue;
                    }
                }
            }

            if (choiceUsed != null) {
                explanation += " (used " + (choiceUsed.equals("sick") ? "sick day" : "day off") + ")";
            }

            Constraint constraint = new Constraint(explanation, day, type);
            self.addNewConstraints(caller, constraint); // תמיד מוסיף ל-weekly
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




    private void printConstraintsList(List<Constraint> constraints, String title) {
        if (constraints.isEmpty()) {
            System.out.println("No " + title + " constraints found.");
            return;
        }
        System.out.println("Current " + title + " constraints:");
        for (int i = 0; i < constraints.size(); i++) {
            Constraint c = constraints.get(i);
            System.out.println((i + 1) + ". " + c.getDay() + " - " + c.getExplanation());
        }
    }





    @Override
    public void updateConstraint(User caller, Employee self, Week currentWeek) {
        try {
            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Employees may only edit their own constraints.");
            }

            // כל עוד המשתמש לא בוחר לצאת:
            while (true) {
                ShiftType selectedType = null;

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
                            return; // יוצא מ-updateConstraint
                        default:
                            System.out.println("Invalid input. Please enter 'morning', 'evening' or 'back'.");
                    }
                }

                List<Constraint> relevantList = (selectedType == ShiftType.MORNING)
                        ? self.getMorningConstraints(caller)
                        : self.getEveningConstraints(caller);

                if (relevantList.isEmpty()) {
                    System.out.println("No constraints found for " + selectedType + " shifts.");
                    continue;
                }

                while (true) {
                    printConstraintsList(relevantList, selectedType + " shift");

                    System.out.println("\nSelect an action:");
                    System.out.println("1. Update explanation of an existing constraint");
                    System.out.println("2. Remove an existing constraint");
                    System.out.println("3. Back to shift type selection");
                    String action = scanner.nextLine().trim();

                    if (action.equals("3")) {
                        break; // חוזר לשלב בחירת סוג משמרת
                    }

                    if (!action.equals("1") && !action.equals("2")) {
                        System.out.println("Invalid input. Please choose 1, 2, or 3.");
                        continue;
                    }

                    int index = -1;
                    while (true) {
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

                    // בדיקה אם כבר שובץ
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

                        if (selected.getExplanation().contains("(used sick day)")) {
                            newExplanation += " (used sick day)";
                        } else if (selected.getExplanation().contains("(used day off)")) {
                            newExplanation += " (used day off)";
                        }

                        selected.setExplanation(caller, self, newExplanation);
                        System.out.println("Explanation updated.");
                    } else {
                        relevantList.remove(index);
                        self.getWeeklyConstraints(caller).remove(selected);
                        System.out.println("Constraint removed.");

                        if (selected.getExplanation().contains("(used sick day)")) {
                            self.setSickDays(caller, self.getSickDays(caller) + 1);
                            System.out.println("Sick day restored.");
                        } else if (selected.getExplanation().contains("(used day off)")) {
                            self.setDaysOff(caller, self.getDaysOff(caller) + 1);
                            System.out.println("Day off restored.");
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









    @Override
    public void viewPersonalDetails(User caller, Employee self) {
        try {
            if (!caller.isManager() && !caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: Only HR managers or the employee can view personal details.");
            }

            System.out.println("\n--- Employee Personal Details ---");
            System.out.println(self);  // משתמש ב־toString()

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }


    @Override
    public void viewMyConstraints(User caller, Employee self) {
        try {
            if (!caller.isManager() && !caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: You may only view your own constraints.");
            }

            List<Constraint> morning = self.getMorningConstraints(caller);
            List<Constraint> evening = self.getEveningConstraints(caller);

            System.out.println("=== Submitted Constraints ===");

            if (morning.isEmpty() && evening.isEmpty()) {
                System.out.println("No constraints submitted.");
                return;
            }

            // שימוש בפונקציית העזר שלך
            printConstraintsList(morning, "MORNING shift");
            printConstraintsList(evening, "EVENING shift");

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }


    @Override
    public void changePassword(User caller, Employee self) {
        try {
            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: You may only change your own password.");
            }

            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine().trim();

            if (!self.verifyPassword(currentPassword,caller)) {
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

            self.setEmpPassword(newPassword, caller);
            System.out.println("Password updated successfully.");

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }



    @Override
    public void viewContractDetails(User caller, Employee self) {
        try {
            if (!caller.isManager() && !caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: Only HR managers or the employee can view contract details.");
            }

            System.out.println("\n--- Employment Contract Details ---");
            System.out.println(self.getContract(caller).toString());

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }



    public void viewAvailableRoles(User caller, Employee self) {
        try {
            if (!caller.isManager() && !caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: Only HR managers or the employee can view roles.");
            }

            self.printRelevantRoles();

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }



    // More functions will be implemented here step by step
}


