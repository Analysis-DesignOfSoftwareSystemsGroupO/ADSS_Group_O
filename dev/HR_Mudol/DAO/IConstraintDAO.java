package HR_Mudol.DAO;

import HR_Mudol.DTO.ConstraintDTO;
import java.sql.SQLException;
import HR_Mudol.domain.*;
import java.util.List;

public interface IConstraintDAO {
    void insert(ConstraintDTO constraint) throws SQLException;
    void delete(long empID, String day, String type) throws SQLException;

    ConstraintDTO getConstraint(long empId, WeekDay day, ShiftType type);
    List<ConstraintDTO> getByEmployee(long empID) throws SQLException;
    List<ConstraintDTO> getAll() throws SQLException;
    void update(long empId, ConstraintDTO dto);

    List<ConstraintDTO> getWeeklyConstraints(long empId);

    List<ConstraintDTO> getMorningConstraints(long empId);

    List<ConstraintDTO> getEveningConstraints(long empId);

    List<ConstraintDTO> getLockedConstraints(long empId);
}
