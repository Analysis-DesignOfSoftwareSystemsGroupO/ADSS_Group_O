/*package HR_Mudol.presentation;

import HR_Mudol.domain.*;
import java.util.LinkedList;
import java.util.List;

public class ShiftManager implements IShiftManager {

    private List<Shift> shifts; //all the shifts at the branch from day one

    public ShiftManager() {
        this.shifts = new LinkedList<>();
    }

    @Override
    public void assignEmployeeToShift(User caller, int shiftId, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        Shift shift = shifts.get(shiftId);
        shift.addEmployee(caller,employee);

        System.out.println("Employee " + employee.getEmpName() + " assigned to shift " + shiftId + ".");
    }

    @Override
    public void removeEmployeeFromShift(User caller, int shiftId, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        Shift shift = shifts.get(shiftId);
        if (shift == null) {
            System.out.println("Shift with ID " + shiftId + " does not exist.");
            return;
        }

        shift.removeEmployee(caller,employee);
        System.out.println("Employee " + employee.getEmpName() + " removed from shift " + shiftId + ".");
    }

    @Override
    public void printShift(User caller, int shiftId) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        Shift shift = shifts.get(shiftId);
        if (shift == null) {
            System.out.println("Shift with ID " + shiftId + " does not exist.");
            return;
        }

        System.out.println(shift.toString());
    }

    private Shift getShiftById(int shiftId) {
        for (Shift shift : shifts) {
            if (shift.getShiftId() == shiftId) {
                return shift;
            }
        }
        return null;
    }
}
