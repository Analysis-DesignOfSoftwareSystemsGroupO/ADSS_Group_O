package HR_Mudol.DAO;

import HR_Mudol.DTO.*;
import java.sql.SQLException;
import java.util.List;

public interface IRoleDAO {
    void insert(RoleDTO role) throws SQLException;
    void updateDescription(RoleDTO dto);
    void assignEmployeeToRole(int empID, int roleNumber);
    void removeEmployeeFromRole(int empID, int roleNumber);
    List<EmployeeDTO> getAllEmployeeDTOsWithRoles();
    RoleDTO getByNumber(int roleNumber);

    void delete(int roleNumber) throws SQLException;

    List<RoleDTO> getAll() throws SQLException;
}
