package HR_Mudol.Service.EmployeeService;

import HR_Mudol.domain.*;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.repository.EmployeeRepository;

import java.util.*;

public class EmployeeService implements IEmployeeService {

    private Scanner scanner;
    private EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
        this.scanner = new Scanner(System.in);
    }

    public EmployeeService(EmployeeRepository employeeRepo, Scanner scanner) {
        this.employeeRepo = employeeRepo;
        this.scanner = scanner;
    }

    @Override
    public void viewMyShifts(User caller, int empId, Week currentWeek) {
        Employee self = employeeRepo.getById(empId);

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
        for (Shift shift : currentWeek.getShifts()) {
            if (shift.getEmployees().contains(self)) {
                System.out.println("- " + shift);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No assigned shifts found for the upcoming week.");
        }
    }

    @Override
    public void submitConstraint(User caller, int empId, Week currentWeek) {
        Employee self = employeeRepo.getById(empId);

        if (!caller.isSameEmployee(self)) throw new SecurityException("Employees may only submit constraints for themselves.");
        if (!currentWeek.isConstraintSubmissionOpen()) {
            self.lockWeeklyConstraints(caller);
            System.out.println("Constraint submission is now closed.");
            System.out.println("You are now submitting constraints for the new upcoming week.");
        }

        int dayShiftLimit = self.getMinDayShift(caller);
        int eveningShiftLimit = self.getMinEveninigShift(caller);
        List<Constraint> submittedConstraints = new ArrayList<>();

        System.out.println("Now starting constraint submission for MORNING shifts:");
        handleShiftTypeConstraints(caller, self, ShiftType.MORNING, dayShiftLimit, submittedConstraints);

        System.out.println("Now starting constraint submission for EVENING shifts:");
        handleShiftTypeConstraints(caller, self, ShiftType.EVENING, eveningShiftLimit, submittedConstraints);

        long morningCount = submittedConstraints.stream().filter(c -> c.getType() == ShiftType.MORNING).count();
        long eveningCount = submittedConstraints.stream().filter(c -> c.getType() == ShiftType.EVENING).count();
        System.out.println("Finished submitting constraints for the week.");
        System.out.println("Total constraints submitted: " + submittedConstraints.size());
        System.out.println(" - Morning shifts: " + morningCount);
        System.out.println(" - Evening shifts: " + eveningCount);
    }

    private int handleShiftTypeConstraints(User caller, Employee self, ShiftType type, int shiftLimit, List<Constraint> submittedConstraints) {
        int shiftCount = 0;
        for (WeekDay day : WeekDay.values()) {
            if ((type == ShiftType.EVENING && (day == WeekDay.FRIDAY || day == WeekDay.SATURDAY)) ||
                    (type == ShiftType.MORNING && day == WeekDay.SATURDAY)) {
                continue;
            }

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

                while (true) {
                    System.out.println("Do you want to use a sick day or a day off? (sick/off/cancel)");
                    String choice = scanner.nextLine().trim().toLowerCase();

                    if (choice.equals("sick") && self.getSickDays(caller) > 0) {
                        self.setSickDays(caller, self.getSickDays(caller) - 1);
                        choiceUsed = "sick";
                        shiftCount--;
                        System.out.println("One sick day used.");
                        break;
                    } else if (choice.equals("off") && self.getDaysOff(caller) > 0) {
                        self.setDaysOff(caller, self.getDaysOff(caller) - 1);
                        choiceUsed = "off";
                        shiftCount--;
                        System.out.println("One day off used.");
                        break;
                    } else if (choice.equals("cancel")) {
                        System.out.println("Constraint cancelled.");
                        shiftCount--;
                        return shiftCount;
                    } else {
                        System.out.println("Invalid or unavailable option. Try again.");
                    }
                }
            }

            if (choiceUsed != null) {
                explanation += " (used " + (choiceUsed.equals("sick") ? "sick day" : "day off") + ")";
            }

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
    public void changePassword(User caller, int empId) {
        try {
            Employee self = employeeRepo.getById(empId);
            if (self == null) {
                System.out.println("Employee not found.");
                return;
            }

            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied: You may only change your own password.");
            }

            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine().trim();

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

            self.setEmpPassword(newPassword, caller);
            System.out.println("Password updated successfully.");

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @Override
    public void viewContractDetails(User caller, int empId) {
        try {
            Employee self = employeeRepo.getById(empId);
            if (self == null) {
                System.out.println("Employee not found.");
                return;
            }

            if (!caller.isManager() && !caller.isSameEmployee(self)) {
                throw new SecurityException("Access denied.");
            }

            System.out.println("\n--- Employment Contract Details ---");
            System.out.println(self.getContract(caller).toString());

        } catch (SecurityException se) {
            System.out.println("Access denied: " + se.getMessage());
        } catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }



    @Override
    public void updateConstraint(User caller, int empId, Week currentWeek) {
        try {
            Employee self = employeeRepo.getById(empId);
            if (self == null) {
                System.out.println("Employee not found.");
                return;
            }

            if (!caller.isSameEmployee(self)) {
                throw new SecurityException("Employees may only edit their own constraints.");
            }

            while (true) {
                ShiftType selectedType = null;

                // Select type
                while (selectedType == null) {
                    System.out.println("Which shift type do you want to edit constraints for? (morning/evening) or 'back' to exit:");
                    String typeChoice = scanner.nextLine().trim().toLowerCase();

                    switch (typeChoice) {
                        case "morning" -> selectedType = ShiftType.MORNING;
                        case "evening" -> selectedType = ShiftType.EVENING;
                        case "back" -> {
                            System.out.println("Returning to main menu.");
                            return;
                        }
                        default -> System.out.println("Invalid input. Please enter 'morning', 'evening' or 'back'.");
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

                    if (action.equals("3")) break;
                    if (!action.equals("1") && !action.equals("2")) {
                        System.out.println("Invalid input. Please choose 1, 2, or 3.");
                        continue;
                    }

                    if (relevantList.isEmpty()) {
                        System.out.println("No constraints left to modify or remove.");
                        break;
                    }

                    int index = -1;
                    while (true) {
                        System.out.print("Enter number of constraint to modify/remove: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                            if (index < 0 || index >= relevantList.size()) {
                                System.out.println("Invalid selection. Please choose a valid number.");
                            } else break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a number.");
                        }
                    }

                    Constraint selected = relevantList.get(index);

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

                        if (selected.getExplanation().contains("(used sick day)"))
                            newExplanation += " (used sick day)";
                        else if (selected.getExplanation().contains("(used day off)"))
                            newExplanation += " (used day off)";

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

                        if (relevantList.isEmpty()) {
                            System.out.println("No more constraints left for " + selectedType + " shifts.");
                            break;
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
    public void viewPersonalDetails(User caller, int employeeId) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied: Only HR managers can view other employees' personal details.");
        }

        Employee employee = employeeRepo.getById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\n--- Employee Personal Details ---");
        System.out.println(employee); // uses Employee.toString()
    }
    @Override
    public void viewMyConstraints(User caller, int employeeId) {
        Employee employee = employeeRepo.getById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        viewMyConstraints(caller, employee.getEmpId()); // קריאה לגרסה הקיימת שמקבלת Employee
    }
    @Override
    public void viewAvailableRoles(User caller, int employeeId) {
        Employee employee = employeeRepo.getById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        viewAvailableRoles(caller, employee.getEmpId()); // שימוש במתודה הקיימת
    }







}
