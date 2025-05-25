package HR_Mudol.DAO;

import HR_Mudol.DAO.IUserDAO;
import HR_Mudol.DTO.UserDTO;

import java.sql.SQLException;
import java.util.List;

public class UserRepositoryDAOImpl implements IUserRepositoryDAO {

    private final IUserDAO dao;

    public UserRepositoryDAOImpl(IUserDAO dao) {
        this.dao = dao;
    }

    @Override
    public void add(UserDTO user) throws SQLException {
        dao.insert(user);
    }

    @Override
    public void update(UserDTO user) throws SQLException {
        dao.update(user);
    }

    @Override
    public void delete(int userId) throws SQLException {
        dao.delete(userId);
    }

    @Override
    public UserDTO get(int userId) throws SQLException {
        return dao.get(userId);
    }

    @Override
    public List<UserDTO> getAll() throws SQLException {
        return dao.getAll();
    }
}
