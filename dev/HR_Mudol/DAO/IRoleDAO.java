package HR_Mudol.DAO;

import HR_Mudol.DTO.*;
import java.sql.SQLException;
import java.util.List;

public interface IRoleDAO {
    void insert(RoleDTO role) throws SQLException;
    void updateDescription(RoleDTO dto);
    void assignEmployeeToRole(long empID, int roleNumber);
    void removeEmployeeFromRole(long empID, int roleNumber);
    List<EmployeeDTO> getAllEmployeeDTOsWithRoles();
    RoleDTO getByNumber(int roleNumber);
    List<Integer> getAllEmployeeIDsWithRoles();
    List<RoleDTO> getAllByBranch(int branchId) throws SQLException;

    void delete(int roleNumber) throws SQLException;

    List<RoleDTO> getAll() throws SQLException;

    List<RoleDTO> getRolesByEmpId(long empId);

    RoleDTO getByDescription(String description) throws SQLException;

    void deleteByDescription(String description) throws SQLException;

    List<EmployeeDTO> getEmployeesForRole(int roleNumber);
}
