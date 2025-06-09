package HR_Mudol.DAO;

import HR_Mudol.DTO.ConstraintDTO;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConstraintDAOImpl extends BaseDAO implements IConstraintDAO {

    public ConstraintDAOImpl() throws SQLException {
        super();
    }

    @Override
    public void insert(ConstraintDTO c) throws SQLException {
        String sql = "INSERT INTO constraints (empID, WeekDay, ShiftType, explanation, date_created) VALUES (?, ?, ?, ?, CURRENT_DATE)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, c.getEmpID());
            stmt.setString(2, c.getDay());
            stmt.setString(3, c.getType());
            stmt.setString(4, c.getExplanation());
            stmt.executeUpdate();
        }
    }


    @Override
    public void delete(long empID, String day, String type) throws SQLException {
        String sql = "DELETE FROM constraints WHERE empID = ? AND WeekDay = ? AND ShiftType = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empID);
            stmt.setString(2, day);
            stmt.setString(3, type);
            stmt.executeUpdate();
        }
    }


    @Override
    public List<ConstraintDTO> getByEmployee(long empID) throws SQLException {
        List<ConstraintDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM constraints WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empID);
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
    public ConstraintDTO getConstraint(long empId, WeekDay day, ShiftType type) {
        String sql = "SELECT explanation FROM constraints WHERE empID = ? AND WeekDay = ? AND ShiftType = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empId);
            stmt.setString(2, day.name());
            stmt.setString(3, type.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ConstraintDTO(empId, rs.getString("explanation"), day.name(), type.name());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch constraint", e);
        }
        return null;
    }


    @Override
    public void update(long empId, ConstraintDTO dto) {
        String sql = "UPDATE constraints SET explanation = ? WHERE empID = ? AND WeekDay = ? AND ShiftType = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dto.getExplanation());
            stmt.setLong(2, empId);
            stmt.setString(3, dto.getDay());
            stmt.setString(4, dto.getType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update constraint", e);
        }
    }

    @Override
    public List<ConstraintDTO> getWeeklyConstraints(long empId) {
        String sql = "SELECT * FROM constraints WHERE empID = ?";
        return fetchConstraints(empId, sql);
    }

    @Override
    public List<ConstraintDTO> getMorningConstraints(long empId) {
        String sql = "SELECT * FROM constraints WHERE empID = ? AND ShiftType = 'MORNING'";
        return fetchConstraints(empId, sql);
    }

    @Override
    public List<ConstraintDTO> getEveningConstraints(long empId) {
        String sql = "SELECT * FROM constraints WHERE empID = ? AND ShiftType = 'EVENING'";
        return fetchConstraints(empId, sql);
    }

    @Override
    public List<ConstraintDTO> getLockedConstraints(long empId) {
        String sql = """
            SELECT * FROM constraints
            WHERE empID = ?
              AND date_created < (
                date_trunc('week', CURRENT_DATE) + interval '4 days' + interval '12 hours'
                - CASE 
                    WHEN EXTRACT(DOW FROM CURRENT_DATE) < 4 THEN interval '7 days'
                    ELSE interval '0'
                  END
              );
        """;
        return fetchConstraints(empId, sql);
    }

    private List<ConstraintDTO> fetchConstraints(long empId, String sql) {
        List<ConstraintDTO> result = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new ConstraintDTO(
                        empId,
                        rs.getString("explanation"),
                        rs.getString("WeekDay"),
                        rs.getString("ShiftType")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch constraints", e);
        }
        return result;
    }

}
