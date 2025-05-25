package HR_Mudol.DAO;

import HR_Mudol.DTO.RoleDTO;

import java.sql.SQLException;
import java.util.List;

public class RoleRepositoryDAOImpl implements IRoleRepositoryDAO {

    private final IRoleDAO dao;
    private int currentMaxId;

    public RoleRepositoryDAOImpl(IRoleDAO dao) throws SQLException {
        this.dao = dao;
        this.currentMaxId = dao.getAll().stream()
                .mapToInt(RoleDTO::getRoleNumber)
                .max()
                .orElse(0);
    }

    @Override
    public void add(RoleDTO role) throws SQLException {
        RoleDTO autoRole = new RoleDTO(++currentMaxId, role.getDescription());
        dao.insert(autoRole);
    }

    @Override
    public void update(RoleDTO role) throws SQLException {
        dao.update(role);
    }

    @Override
    public void delete(int roleNumber) throws SQLException {
        dao.delete(roleNumber);
    }

    @Override
    public RoleDTO get(int roleNumber) throws SQLException {
        return dao.get(roleNumber);
    }

    @Override
    public List<RoleDTO> getAll() throws SQLException {
        return dao.getAll();
    }
}
