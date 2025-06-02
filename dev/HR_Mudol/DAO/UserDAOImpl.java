package HR_Mudol.DAO;

import HR_Mudol.DTO.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl extends BaseDAO implements IUserDAO {

    public UserDAOImpl() throws SQLException {
        super();
    }

    @Override
    public void insert(UserDTO user) throws SQLException {
        String sql = "INSERT INTO users (userID, level) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getLevel());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(UserDTO user) throws SQLException {
        String sql = "UPDATE users SET level = ? WHERE userID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getLevel());
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(long userId) throws SQLException {
        String sql = "DELETE FROM users WHERE userID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public UserDTO get(long userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE userID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserDTO(rs.getLong("userID"), rs.getString("level"));
            }
        }
        return null;
    }

    @Override
    public List<UserDTO> getAll() throws SQLException {
        List<UserDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new UserDTO(rs.getInt("userID"), rs.getString("level")));
            }
        }
        return list;
    }

    @Override
    public boolean exists(long userId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE userID = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
