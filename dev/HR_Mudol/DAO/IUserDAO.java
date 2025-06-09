package HR_Mudol.DAO;

import HR_Mudol.DTO.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void insert(UserDTO user) throws SQLException;
    void update(UserDTO user) throws SQLException;
    void delete(long userId) throws SQLException;
    UserDTO get(long userId) throws SQLException;
    List<UserDTO> getAll() throws SQLException;
    boolean exists(long empId) throws SQLException;
}
