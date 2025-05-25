package HR_Mudol.DAO;

import HR_Mudol.DTO.RoleDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements IRoleDAO {
    private final Connection conn;

    public RoleDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(RoleDTO role) throws SQLException {
        String sql = "INSERT INTO roles (roleNumber, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, role.getRoleNumber());
            stmt.setString(2, role.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(RoleDTO role) throws SQLException {
        String sql = "UPDATE roles SET description = ? WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getDescription());
            stmt.setInt(2, role.getRoleNumber());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int roleNumber) throws SQLException {
        String sql = "DELETE FROM roles WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleNumber);
            stmt.executeUpdate();
        }
    }

    @Override
    public RoleDTO get(int roleNumber) throws SQLException {
        String sql = "SELECT * FROM roles WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RoleDTO(rs.getInt("roleNumber"), rs.getString("description"));
            }
        }
        return null;
    }

    @Override
    public List<RoleDTO> getAll() throws SQLException {
        List<RoleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new RoleDTO(rs.getInt("roleNumber"), rs.getString("description")));
            }
        }
        return list;
    }
}
