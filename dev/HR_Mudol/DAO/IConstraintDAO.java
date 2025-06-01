package HR_Mudol.DAO;

import HR_Mudol.DTO.ConstraintDTO;
import java.sql.SQLException;
import HR_Mudol.domain.*;
import java.util.List;

public interface IConstraintDAO {
    void insert(ConstraintDTO constraint) throws SQLException;
    void delete(int empID, String day, String type) throws SQLException;

    ConstraintDTO getConstraint(int empId, WeekDay day, ShiftType type);
    List<ConstraintDTO> getByEmployee(int empID) throws SQLException;
    List<ConstraintDTO> getAll() throws SQLException;
    void update(int empId, ConstraintDTO dto);
}
