package HR_Mudol.DAO;

import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.Status;
import HR_Mudol.domain.WeekDay;

import java.sql.SQLException;
import java.util.List;

public interface IShiftDAO {

    void insert(ShiftDTO shift) throws SQLException;
    void update(ShiftDTO shift) throws SQLException;
    void delete(int shiftID) throws SQLException;
    ShiftDTO get(int shiftID) throws SQLException;
    List<ShiftDTO> getAll() throws SQLException;

    void insertEmpToShift(int brunchID, long empID, int shiftID, int roleNumber);

    void removeEmpFromShift(int shiftID, long empNum);

    void decrementOrRemove(int branchID, int shiftID, int roleNumber);

    void insertOrIncrementRequiredRole(int branchID, WeekDay day, ShiftType type, int roleNumber, int count);

    void updateStatus(int shiftId, String newStatus);

    boolean isEmployeeAssignedToShift(long empId, int shiftId);

    Status getShiftStatus(int shiftId);

    List<ShiftDTO> getCurShiftsByBranch(int branchId);

    //מחזיר את המשמרות של שבוע הבא- אלו שעוד לא שובצו
    List<ShiftDTO> getNextShiftsByBranch(int branchId);

    void insertShift(ShiftDTO shift, int branchId);

    void insertShiftsForNextWeek(int branchId);
}
