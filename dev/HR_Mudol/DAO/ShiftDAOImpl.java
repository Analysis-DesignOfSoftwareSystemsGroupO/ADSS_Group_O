package HR_Mudol.DAO;

import HR_Mudol.DTO.ShiftDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAOImpl implements IShiftDAO {
    private final Connection conn;

    public ShiftDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ShiftDTO shift) throws SQLException {
        String sql = "INSERT INTO shifts (shiftID, day, type, status, shiftManager) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shift.getShiftID());
            stmt.setString(2, shift.getDay());
            stmt.setString(3, shift.getType());
            stmt.setString(4, shift.getStatus());
            stmt.setInt(5, shift.getShiftManagerId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(ShiftDTO shift) throws SQLException {
        String sql = "UPDATE shifts SET day = ?, type = ?, status = ?, shiftManager = ? WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shift.getDay());
            stmt.setString(2, shift.getType());
            stmt.setString(3, shift.getStatus());
            stmt.setInt(4, shift.getShiftManagerId());
            stmt.setInt(5, shift.getShiftID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int shiftID) throws SQLException {
        String sql = "DELETE FROM shifts WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftID);
            stmt.executeUpdate();
        }
    }

    @Override
    public ShiftDTO get(int shiftID) throws SQLException {
        String sql = "SELECT * FROM shifts WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ShiftDTO(
                        rs.getInt("shiftID"),
                        rs.getString("day"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("shiftManager")
                );
            }
        }
        return null;
    }

    @Override
    public List<ShiftDTO> getAll() throws SQLException {
        List<ShiftDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM shifts";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ShiftDTO(
                        rs.getInt("shiftID"),
                        rs.getString("day"),
                        rs.getString("type"),
                        rs.getString("status"),
                        rs.getInt("shiftManager")
                ));
            }
        }
        return list;
    }
}
