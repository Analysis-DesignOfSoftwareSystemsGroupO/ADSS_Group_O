package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeRepositoryDAO {
    void add(EmployeeDTO employee) throws SQLException;
    void update(EmployeeDTO employee) throws SQLException;
    void delete(int empID) throws SQLException;
    EmployeeDTO get(int empID) throws SQLException;
    List<EmployeeDTO> getAll() throws SQLException;
}
