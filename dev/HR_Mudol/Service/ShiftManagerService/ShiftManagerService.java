package HR_Mudol.Service.ShiftManagerSystem;

import HR_Mudol.domain.ShiftController;
import HR_Mudol.domain.*;

import java.util.List;
import java.util.Scanner;

/**
 * System for shift managers to manage employees' assignments to shifts.
 * Provides functionality to add/remove employees and transfer cancellation cards.
 */
public class ShiftManagerService implements IShiftManagerService {

    private Week currentWeek;
    private Branch branch;
    private ShiftController shiftController;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for ShiftManagerSystem.
     *
     * @param currentWeek  the week object containing shifts
     * @param branch       the branch this system manages
     * @param shiftController the shift manager operating the system
     */
    public ShiftManagerService(Week currentWeek, Branch branch, ShiftController shiftController) {
        this.currentWeek = currentWeek;
        this.branch = branch;
        this.shiftController = shiftController;
    }

    /**
     * Allows a shift manager or general manager to remove an employee from a shift.
     *
     * @param caller the user requesting the operation
     */
    @Override
    public void removeEmployeeFromShift(User caller) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can remove employees from shifts.");
            return;
        }

        Shift shift = chooseShift();
        if (shift == null) return;

        // בדיקה אם הקורא הוא המנהל משמרת
        if (!isShiftManagerOfShift(caller, shift)) {
            System.out.println("Access denied. You are not the shift manager of this shift.");
            return;
        }

        shiftController.removeEmployeeFromShift(caller, shift);
    }

    /**
     * Allows a shift manager or general manager to add an employee to a shift.
     *
     * @param caller the user requesting the operation
     */
    @Override
    public void addEmployeeToShift(User caller) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can add employees to shifts.");
            return;
        }

        Shift shift = chooseShift();
        if (shift == null) return;

        // בדיקה אם הקורא הוא המנהל משמרת
        if (!isShiftManagerOfShift(caller, shift)) {
            System.out.println("Access denied. You are not the shift manager of this shift.");
            return;
        }

        // הצגת רשימת עובדים
        System.out.println("Available employees:");
        for (int i = 0; i < branch.getEmployees().size(); i++) {
            Employee e = branch.getEmployees().get(i);
            System.out.println((i + 1) + ". " + e.getEmpName() + " (ID: " + e.getEmpId() + ")");
        }

        System.out.print("Select employee to add: ");
        String empInput = scanner.nextLine().trim();
        int empIndex;

        try {
            empIndex = Integer.parseInt(empInput) - 1;
            if (empIndex < 0 || empIndex >= branch.getEmployees().size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Employee toAdd = branch.getEmployees().get(empIndex);

        // הצגת תפקידי משמרת רלוונטיים
        if (shift.getNotOccupiedRoles().isEmpty()) {
            System.out.println("All roles are already assigned in this shift.");
            return;
        }

        shiftController.printShift(caller, shift); // אפשר להדפיס מידע בסיסי

        Role role = chooseRoleFromList(shift.getNotOccupiedRoles());
        if (role == null) return;

        shiftController.assignEmployeeToShift(caller, shift, toAdd, role);
    }

    /**
     * Allows a shift manager to transfer a cancellation card (simplified for now).
     *
     * @param caller the user requesting the operation
     */
    @Override
    public void transferCancellationCard(User caller) {
        if (!caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can transfer the cancellation card.");
            return;
        }

        System.out.println("Item canceled.");
    }

    // --------- Helper methods ---------

    /**
     * Prompts the user to choose a shift by selecting day and type.
     *
     * @return the selected Shift object, or null if not found
     */
    private Shift chooseShift() {
        try {
            WeekDay selectedDay = chooseDay();
            if (selectedDay == null) return null;

            ShiftType selectedType = chooseShiftType();
            if (selectedType == null) return null;

            Shift selectedShift = findShift(selectedDay, selectedType);
            if (selectedShift == null) {
                System.out.println("No such shift exists.");
                return null;
            }

            return selectedShift;

        } catch (Exception e) {
            System.out.println("Error choosing shift: " + e.getMessage());
            return null;
        }
    }

    /**
     * Finds a shift in the current week by day and type.
     *
     * @param day  the day of the shift
     * @param type the type of the shift (morning/evening)
     * @return the matching Shift, or null if not found
     */
    private Shift findShift(WeekDay day, ShiftType type) {
        for (Shift shift : currentWeek.getShifts()) {
            if (shift.getDay() == day && shift.getType() == type) {
                return shift;
            }
        }
        return null;
    }

    /**
     * Prompts the user to select a day of the week.
     *
     * @return the selected WeekDay enum, or null if invalid input
     */
    private WeekDay chooseDay() {
        System.out.println("Select day of the week :");
        WeekDay[] days = WeekDay.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }

        String input = scanner.nextLine().trim();
        try {
            int choice = Integer.parseInt(input);
            if (choice < 1 || choice > days.length) {
                System.out.println("Invalid day selection.");
                return null;
            }
            return days[choice - 1];
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    /**
     * Prompts the user to select a shift type (Morning/Evening).
     *
     * @return the selected ShiftType, or null if invalid input
     */
    private ShiftType chooseShiftType() {
        System.out.println("Select shift type:");
        System.out.println("1. Morning");
        System.out.println("2. Evening");

        String input = scanner.nextLine().trim();
        return switch (input) {
            case "1" -> ShiftType.MORNING;
            case "2" -> ShiftType.EVENING;
            default -> {
                System.out.println("Invalid shift type.");
                yield null;
            }
        };
    }

    /**
     * Prompts the user to choose a role from the available roles.
     *
     * @param roles list of available roles
     * @return the selected Role, or null if invalid input
     */
    private Role chooseRoleFromList(List<Role> roles) {
        System.out.println("Available roles for this shift:");
        for (int i = 0; i < roles.size(); i++) {
            Role r = roles.get(i);
            System.out.println((i + 1) + ". " + r.getDescription());
        }

        System.out.print("Choose role number: ");
        String input = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > roles.size()) {
                System.out.println("Invalid role number.");
                return null;
            }
            return roles.get(index - 1);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    /**
     * Checks if the caller is the shift manager assigned to the given shift.
     *
     * @param caller the user making the request
     * @param shift  the shift in question
     * @return true if the caller is the shift manager, false otherwise
     */
    private boolean isShiftManagerOfShift(User caller, Shift shift) {
        if (shift.getShiftManager()==null)
        {
            return false;
        }
        return caller.getUser().getEmpId()==(shift.getShiftManager().getEmpId());
    }
}
