package HR_Mudol.DAO;

import HR_Mudol.DTO.RoleDTO;

import java.sql.SQLException;
import java.util.List;

public interface IRoleDAO {
    void insert(RoleDTO role) throws SQLException;
    void update(RoleDTO role) throws SQLException;
    void delete(int roleNumber) throws SQLException;
    RoleDTO get(int roleNumber) throws SQLException;
    List<RoleDTO> getAll() throws SQLException;
}
