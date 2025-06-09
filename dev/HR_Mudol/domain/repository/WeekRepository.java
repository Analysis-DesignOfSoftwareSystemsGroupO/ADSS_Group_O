package HR_Mudol.domain.repository;
import HR_Mudol.DAO.ShiftDAOImpl;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.DTO.WeekDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.Shift;
import HR_Mudol.domain.Objects.Week;
import HR_Mudol.domain.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<ShiftDTO> getAllShift(int branchid) {
        this.shiftDAO.insertShiftsForNextWeek(branchid);
         return this.shiftDAO.getNextShiftsByBranch(branchid);
    }

    /**
     * Insert an employee to a shift inside a week - to DB
     */
    public void insertEmployeeToShift(int brunchID, long empID, int shiftID, int roleNumber) {
        shiftDAO.insertEmpToShift(brunchID, empID, shiftID, roleNumber);
    }

    public void removeEmployeeFromShift(long empID, int shiftID) {
        shiftDAO.removeEmpFromShift(shiftID, empID);
    }

    public void removeRoleFromShift(int branchID, int shiftID, int roleNumber) {
        shiftDAO.decrementOrRemove(branchID, shiftID, roleNumber);
    }

    public void addOrUpdateRequiredRole(int branchID, WeekDay day ,ShiftType type, int roleNumber, int count) {
        shiftDAO.insertOrIncrementRequiredRole(branchID, day , type, roleNumber, count);
    }

    public void updateShiftStatus(int shiftId, Status newStatus) {
        shiftDAO.updateStatus(shiftId, newStatus.name());
    }

    public void deleteShift(int shiftId) {
        shiftDAO.delete(shiftId);
    }

    public boolean isEmployeeAssignedToShift(long empId, int shiftId) {
        return shiftDAO.isEmployeeAssignedToShift(empId, shiftId);
    }

    public Status getShiftStatus(int shiftId) {
        return shiftDAO.getShiftStatus(shiftId);
    }

    public ShiftDTO getShiftById(int shiftId) throws SQLException {

        return shiftDAO.get(shiftId);

    }

    public WeekDTO getCurrentWeekDTO(int branchId) {
        List<ShiftDTO> shiftDTOs = shiftDAO.getCurShiftsByBranch(branchId);
        return new WeekDTO(null, shiftDTOs);
    }

    public WeekDTO getNextWeekDTO(int branchId) {
        List<ShiftDTO> shiftDTOs = shiftDAO.getNextShiftsByBranch(branchId);

        if (shiftDTOs.isEmpty())
        {
            shiftDAO.insertShiftsForNextWeek(branchId);
            shiftDTOs = shiftDAO.getNextShiftsByBranch(branchId);
        }
        // חישוב יום חמישי הקרוב
        LocalDate today = LocalDate.now();
        int daysUntilThursday = DayOfWeek.THURSDAY.getValue() - today.getDayOfWeek().getValue();
        if (daysUntilThursday < 0) {
            daysUntilThursday += 7; // עבור יום חמישי הבא אם היום אחרי חמישי
        }
        LocalDate nextThursday = today.plusDays(daysUntilThursday);

        // קביעת השעה 12:00
        LocalDateTime constraintDeadline = nextThursday.atTime(12, 0);

        return new WeekDTO(constraintDeadline, shiftDTOs);
    }

    public void saveShift(ShiftDTO shift, int branchID)  {
        shiftDAO.insertShift(shift, branchID);
    }

    public void assignedShiftM(long empId, int shiftId){
        shiftDAO.assignedShiftM(empId,shiftId);
    }

    public List<ShiftDTO> getShiftsInDateRange(BranchDTO branchDTO, LocalDate startDate, LocalDate endDate) throws SQLException {
        return shiftDAO.getShiftsInDateRange(branchDTO.getBranchID(),startDate,endDate);
    }
}