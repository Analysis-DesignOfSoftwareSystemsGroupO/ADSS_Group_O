package HR_Mudol.DAO;

import HR_Mudol.DTO.ShiftDTO;
import HR_Mudol.domain.Status;

import java.sql.SQLException;
import java.util.List;

public interface IShiftDAO {

    void insert(ShiftDTO shift) throws SQLException;
    void update(ShiftDTO shift) throws SQLException;
    void delete(int shiftID) throws SQLException;
    ShiftDTO get(int shiftID) throws SQLException;
    List<ShiftDTO> getAll() throws SQLException;

    void insertEmpToShift(int brunchID, int empID, int shiftID, int roleNumber);

    void removeEmpFromShift(int shiftID, int empNum);

    void decrementOrRemove(int branchID, int shiftID, int roleNumber);

    void insertOrIncrementRequiredRole(int branchID, int shiftID, int roleNumber, int count);

    void updateStatus(int shiftId, String newStatus);

    boolean isEmployeeAssignedToShift(int empId, int shiftId);

    Status getShiftStatus(int shiftId);
}
