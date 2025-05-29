package HR_Mudol.DAO;

import HR_Mudol.DTO.FilledRoleDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilledRoleDAOImpl implements IFilledRoleDAO {
    private final Connection conn;

    public FilledRoleDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(FilledRoleDTO dto) {
        String sql = "INSERT INTO FilledRoles (empId, roleNumber) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getEmployeeId());
            stmt.setInt(2, dto.getRoleId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign employee to role", e);
        }
    }

    @Override
    public void delete(int shiftId, int empId, int roleId) {
        String sql = "DELETE FROM FilledRoles WHERE shiftID = ? AND empId = ? AND roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftId);
            stmt.setInt(2, empId);
            stmt.setInt(3, roleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FilledRoleDTO> findByShift(int shiftId) {
        List<FilledRoleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM FilledRoles WHERE shiftID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shiftId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new FilledRoleDTO(
                        rs.getInt("shiftID"),
                        rs.getInt("empId"),
                        rs.getInt("roleNumber")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<FilledRoleDTO> findByEmployee(int empId) {
        List<FilledRoleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM FilledRoles WHERE empId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new FilledRoleDTO(
                        rs.getInt("shiftID"),
                        rs.getInt("empId"),
                        rs.getInt("roleNumber")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<FilledRoleDTO> findAll() {
        List<FilledRoleDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM FilledRoles";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new FilledRoleDTO(
                        rs.getInt("shiftID"),
                        rs.getInt("empId"),
                        rs.getInt("roleNumber")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<FilledRoleDTO> getEmployees(int shiftId) {
        final String sql =
                "SELECT roleNumber, empId, shiftID " +
                        "FROM   FilledRole " +
                        "WHERE  shiftID = ?";

        List<FilledRoleDTO> employees = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, shiftId);          // מציבים את הפרמטר
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    employees.add(
                            new FilledRoleDTO(
                                    rs.getInt("roleNumber"),
                                    rs.getInt("empId"),
                                    rs.getInt("shiftID")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employees for shift " + shiftId, e);
        }
        return employees;
    }
}
