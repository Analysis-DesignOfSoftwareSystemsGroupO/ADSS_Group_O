package HR_Mudol.DAO;

import HR_Mudol.DTO.EmploymentContractDTO;

import java.sql.SQLException;
import java.util.List;

public interface IEmploymentContractDAO {
    void insert(EmploymentContractDTO dto) throws SQLException;
    void update(EmploymentContractDTO dto) throws SQLException;
    void delete(int empId) throws SQLException;
    EmploymentContractDTO findByEmpId(int empId) throws SQLException;
    List<EmploymentContractDTO> getAll() throws SQLException;
}
