package HR_Mudol.Service.ShiftManagerService;

import HR_Mudol.domain.*;
import HR_Mudol.domain.Controllers.IShiftController;
import HR_Mudol.domain.Objects.*;

import java.util.List;
import java.util.Scanner;

/**
 * System for shift managers to manage employees' assignments to shifts.
 * Provides functionality to add/remove employees and transfer cancellation cards.
 */
public class ShiftManagerService implements IShiftManagerService {

    private final Week currentWeek;
    private final Branch branch;
    private final IShiftController shiftController;
    private final Scanner scanner = new Scanner(System.in);

    public ShiftManagerService(Week currentWeek, Branch branch, IShiftController shiftController) {
        this.currentWeek = currentWeek;
        this.branch = branch;
        this.shiftController = shiftController;
    }

    @Override
    public void removeEmployeeFromShift(User caller) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can remove employees from shifts.");
            return;
        }

        Shift shift = chooseShift();
        if (shift == null) return;

        if (!isShiftManagerOfShift(caller, shift)) {
            System.out.println("Access denied. You are not the shift manager of this shift.");
            return;
        }

        shiftController.removeEmployeeFromShift(caller, shift);
    }

    @Override
    public void addEmployeeToShift(User caller) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can add employees to shifts.");
            return;
        }

        Shift shift = chooseShift();
        if (shift == null) return;

        if (!isShiftManagerOfShift(caller, shift)) {
            System.out.println("Access denied. You are not the shift manager of this shift.");
            return;
        }

        System.out.println("Available employees:");
        List<Employee> employees = branch.getEmployeeRepo().getAll();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            System.out.println((i + 1) + ". " + e.getEmpName() + " (ID: " + e.getEmpId() + ")");
        }

        System.out.print("Select employee to add: ");
        String empInput = scanner.nextLine().trim();
        int empIndex;

        try {
            empIndex = Integer.parseInt(empInput) - 1;
            if (empIndex < 0 || empIndex >= employees.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        Employee toAdd = employees.get(empIndex);

        if (shift.getNotOccupiedRoles().isEmpty()) {
            System.out.println("All roles are already assigned in this shift.");
            return;
        }

        shiftController.printShift(caller, shift);

        Role role = chooseRoleFromList(shift.getNotOccupiedRoles());
        if (role == null) return;

        shiftController.assignEmployeeToShift(caller, shift, toAdd, role);
    }

    @Override
    public void transferCancellationCard(User caller) {
        if (!caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can transfer the cancellation card.");
            return;
        }

        System.out.println("Item canceled.");
    }

    private Shift chooseShift() {
        try {
            WeekDay selectedDay = chooseDay();
            if (selectedDay == null) return null;

            ShiftType selectedType = chooseShiftType();
            if (selectedType == null) return null;

            return currentWeek.getShifts().stream()
                    .filter(s -> s.getDay() == selectedDay && s.getType() == selectedType)
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            System.out.println("Error choosing shift: " + e.getMessage());
            return null;
        }
    }

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

    private boolean isShiftManagerOfShift(User caller, Shift shift) {
        return shift.getShiftManager() != null &&
                caller.getUser().getEmpId() == shift.getShiftManager().getEmpId();
    }
}
