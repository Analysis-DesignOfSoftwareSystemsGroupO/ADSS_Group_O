package HR_Mudol.DAO;

import HR_Mudol.DTO.ConstraintDTO;

import java.sql.SQLException;
import java.util.List;

public interface IConstraintRepositoryDAO {
    void add(ConstraintDTO constraint) throws SQLException;
    void delete(int empID, String day, String type) throws SQLException;
    List<ConstraintDTO> getByEmployee(int empID) throws SQLException;
    List<ConstraintDTO> getAll() throws SQLException;
}
