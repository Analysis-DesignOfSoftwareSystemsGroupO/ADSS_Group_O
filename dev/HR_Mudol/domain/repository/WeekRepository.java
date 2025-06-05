package HR_Mudol.domain.repository;
import HR_Mudol.DAO.ShiftDAOImpl;
import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Objects.Shift;
import HR_Mudol.domain.Objects.Week;
import HR_Mudol.domain.*;
import java.util.LinkedList;
import java.util.List;


public class WeekRepository {
    private final List<Week> weeks = new LinkedList<>();
    private final ShiftDAOImpl shiftDAO;

    public WeekRepository(ShiftDAOImpl shiftDAO) {
        this.shiftDAO = shiftDAO;
    }

    public void add(Week week) {
        weeks.add(week);
    }

    public List<Week> getAll() {
        return new LinkedList<>(weeks);
    }

    /**
     * Insert an employee to a shift inside a week - to DB
     */
    public void insertEmployeeToShift(int brunchID, int empID, int shiftID, int roleNumber) {
        shiftDAO.insertEmpToShift(brunchID, empID, shiftID, roleNumber);
    }

    public void removeEmployeeFromShift(int empNum, int shiftID) {
        shiftDAO.removeEmpFromShift(shiftID, empNum);
    }

    public void removeRoleFromShift(int branchID, int shiftID, int roleNumber) {
        shiftDAO.decrementOrRemove(branchID, shiftID, roleNumber);
    }

    public void addOrUpdateRequiredRole(int branchID, int shiftID, int roleNumber, int count) {
        shiftDAO.insertOrIncrementRequiredRole(branchID, shiftID, roleNumber, count);
    }

    public void updateShiftStatus(int shiftId, Status newStatus) {
        shiftDAO.updateStatus(shiftId, newStatus.name());
    }

    public void deleteShift(int shiftId) {
        shiftDAO.delete(shiftId);
    }

    public boolean isEmployeeAssignedToShift(int empId, int shiftId) {
        return shiftDAO.isEmployeeAssignedToShift(empId, shiftId);
    }

    public Status getShiftStatus(int shiftId) {
        return shiftDAO.getShiftStatus(shiftId);
    }

    public Shift getShiftById(int shiftId) {
        for (Week week : weeks) {
            for (Shift shift : week.getShifts()) {
                if (shift.getShiftID() == shiftId) {
                    return shift;
                }
            }
        }
        return null;
    }
    public WeekDTO getCurrentWeekDTO() {
        if (weeks.isEmpty()) {
            throw new IllegalStateException("No weeks available");
        }

        Week current = weeks.get(weeks.size() - 1);
        List<ShiftDTO> shiftDTOs = current.getShifts().stream()
                .map(shift -> new ShiftDTO(
                        shift.getShiftID(),
                        shift.getDay().name(),
                        shift.getType().name(),
                        shift.getStatus().name(), // assuming getStatus() returns enum
                        shift.getShiftManagerId() // assuming such getter exists
                ))
                .toList();

        return new WeekDTO(current.getConstraintDeadline(), shiftDTOs);
    }
}