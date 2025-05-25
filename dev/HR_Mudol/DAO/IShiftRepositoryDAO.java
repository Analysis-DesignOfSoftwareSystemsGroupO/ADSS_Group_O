package HR_Mudol.DAO;

import HR_Mudol.DTO.ShiftDTO;

import java.sql.SQLException;
import java.util.List;

public interface IShiftRepositoryDAO {
    void add(ShiftDTO shift) throws SQLException;
    void update(ShiftDTO shift) throws SQLException;
    void delete(int shiftID) throws SQLException;
    ShiftDTO get(int shiftID) throws SQLException;
    List<ShiftDTO> getAll() throws SQLException;
}
