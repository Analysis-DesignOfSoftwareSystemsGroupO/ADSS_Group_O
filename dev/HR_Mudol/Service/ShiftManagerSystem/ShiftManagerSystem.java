/*package HR_Mudol.Service.ShiftManagerSystem;

import HR_Mudol.Service.ManagerSystem.ShiftManager;
import HR_Mudol.domain.*;

import java.util.List;
import java.util.Scanner;

public class ShiftManagerSystem implements IShiftManagerSystem {

    private Week currentWeek;
    private Branch branch;
    private ShiftManager shiftManager;
    private Scanner scanner = new Scanner(System.in);

    public ShiftManagerSystem(Week currentWeek, Branch branch, ShiftManager shiftManager) {
        this.currentWeek = currentWeek;
        this.branch = branch;
        this.shiftManager = shiftManager;
    }

    @Override
    public void removeEmployeeFromShift(User caller) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can remove employees from shifts.");
            return;
        }

        try {
            WeekDay selectedDay = chooseDay();
            if (selectedDay == null) return;

            ShiftType selectedType = chooseShiftType();
            if (selectedType == null) return;

            Shift selectedShift = findShift(selectedDay, selectedType);
            if (selectedShift == null || selectedShift.getEmployees().isEmpty()) {
                System.out.println("No employees found in the selected shift.");
                return;
            }

            List<Employee> employees = selectedShift.getEmployees();
            System.out.println("Employees in the selected shift:");
            for (int j = 0; j < employees.size(); j++) {
                System.out.println((j + 1) + ". " + employees.get(j).getEmpName() + " (ID: " + employees.get(j).getEmpId() + ")");
            }

            System.out.print("Select employee to remove: ");
            int empIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (empIndex < 0 || empIndex >= employees.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Employee toRemove = employees.get(empIndex);
            shiftManager.removeEmployeeFromShift(caller, selectedShift, toRemove);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void addEmployeeToShift(User caller) {
        System.out.println("DEBUG: caller = " + caller.getUser().getEmpName());
        System.out.println("DEBUG: level = " + caller.getLevel());
        System.out.println("DEBUG: isShiftManager = " + caller.isShiftManager());
        System.out.println("DEBUG: isManager = " + caller.isManager());

        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can add employees to shifts.");
            return;
        }

        try {
            WeekDay selectedDay = chooseDay();
            if (selectedDay == null) return;

            ShiftType selectedType = chooseShiftType();
            if (selectedType == null) return;

            Shift selectedShift = findShift(selectedDay, selectedType);
            if (selectedShift == null) {
                System.out.println("No such shift exists.");
                return;
            }

            List<Employee> employees = branch.getEmployees();
            System.out.println("Available employees:");
            for (int j = 0; j < employees.size(); j++) {
                System.out.println((j + 1) + ". " + employees.get(j).getEmpName() + " (ID: " + employees.get(j).getEmpId() + ")");
            }

            System.out.print("Select employee to add: ");
            int empIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (empIndex < 0 || empIndex >= employees.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Employee toAdd = employees.get(empIndex);
            if (selectedShift.getEmployees().contains(toAdd)) {
                System.out.println("Employee is already assigned to this shift.");
                return;
            }

            shiftManager.assignEmployeeToShift(caller, selectedShift, toAdd);

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void transferCancellationCard(User caller) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can transfer the cancellation card.");
            return;
        }


        System.out.println("item canceled");
    }

    private Shift findShift(WeekDay day, ShiftType type) {
        for (Shift shift : currentWeek.getShifts()) {
            if (shift.getDay() == day && shift.getType() == type) {
                return shift;
            }
        }
        return null;
    }

    private WeekDay chooseDay() {
        System.out.println("Select day of the week:");
        int i = 1;
        for (WeekDay day : WeekDay.values()) {
            System.out.println(i++ + ". " + day);
        }
        int dayChoice = Integer.parseInt(scanner.nextLine().trim());
        if (dayChoice < 1 || dayChoice > WeekDay.values().length) {
            System.out.println("Invalid day selection.");
            return null;
        }
        return WeekDay.values()[dayChoice - 1];
    }

    private ShiftType chooseShiftType() {
        System.out.println("Select shift type:");
        System.out.println("1. Morning");
        System.out.println("2. Evening");
        int typeChoice = Integer.parseInt(scanner.nextLine().trim());
        return switch (typeChoice) {
            case 1 -> ShiftType.MORNING;
            case 2 -> ShiftType.EVENING;
            default -> null;
        };
    }
}
*/