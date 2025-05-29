package HR_Mudol.DAO;

import HR_Mudol.DTO.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {
    private final Connection conn;

    public UserDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(UserDTO user) throws SQLException {
        String sql = "INSERT INTO users (user, level) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getLevel());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(UserDTO user) throws SQLException {
        String sql = "UPDATE users SET level = ? WHERE user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getLevel());
            stmt.setInt(2, user.getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    public UserDTO get(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserDTO(rs.getInt("user"), rs.getString("level"));
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
                list.add(new UserDTO(rs.getInt("user"), rs.getString("level")));
            }
        }
        return list;
    }

    @Override
    public boolean exists(int empId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE user = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }


}
