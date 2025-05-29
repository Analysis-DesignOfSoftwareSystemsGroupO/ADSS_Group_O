package HR_Mudol.DAO;

import HR_Mudol.DTO.ConstraintDTO;
import HR_Mudol.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConstraintDAOImpl implements IConstraintDAO {
    private final Connection conn;

    public ConstraintDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(ConstraintDTO c) throws SQLException {
        String sql = "INSERT INTO constraints (empID, WeekDay, ShiftType, explanation) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getEmpID());
            stmt.setString(2, c.getDay());
            stmt.setString(3, c.getType());
            stmt.setString(4, c.getExplanation());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int empID, String day, String type) throws SQLException {
        String sql = "DELETE FROM constraints WHERE empID = ? AND WeekDay = ? AND ShiftType = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empID);
            stmt.setString(2, day);
            stmt.setString(3, type);
            stmt.executeUpdate();
        }
    }


    @Override
    public List<ConstraintDTO> getByEmployee(int empID) throws SQLException {
        List<ConstraintDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM constraints WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ConstraintDTO(
                        rs.getInt("empID"),
                        rs.getString("explanation"),
                        rs.getString("WeekDay"),
                        rs.getString("ShiftType")
                ));
            }
        }
        return list;
    }

    @Override
    public List<ConstraintDTO> getAll() throws SQLException {
        List<ConstraintDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM constraints";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ConstraintDTO(
                        rs.getInt("empID"),
                        rs.getString("explanation"),
                        rs.getString("WeekDay"),
                        rs.getString("ShiftType")
                ));
            }
        }
        return list;
    }

    @Override
    public ConstraintDTO getConstraint(int empId, WeekDay day, ShiftType type) {
        String sql = "SELECT explanation FROM Constraints WHERE empNum = ? AND WeekDay = ? AND ShiftType = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setString(2, day.name());
            stmt.setString(3, type.name());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String explanation = rs.getString("explanation");
                return new ConstraintDTO(empId, explanation, day.name(), type.name());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch constraint", e);
        }

        return null;
    }


}
