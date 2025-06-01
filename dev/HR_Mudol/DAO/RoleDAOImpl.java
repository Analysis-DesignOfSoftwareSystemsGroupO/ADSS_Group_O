package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DataBase.PostgresConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl extends BaseDAO implements IRoleDAO {

    public RoleDAOImpl() throws SQLException {
        super();
    }

    @Override
    public void insert(RoleDTO dto) throws SQLException {
        String sql = "INSERT INTO Roles (roleNumber, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getRoleNumber());
            stmt.setString(2, dto.getDescription());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateDescription(RoleDTO dto) {
        String sql = "UPDATE Roles SET description = ? WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dto.getDescription());
            stmt.setInt(2, dto.getRoleNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update role description", e);
        }
    }

    @Override
    public void assignEmployeeToRole(int empID, int roleNumber) {
        String sql = "INSERT INTO EmployeeRole (empID, roleNumber) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empID);
            stmt.setInt(2, roleNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign employee to role", e);
        }
    }

    @Override
    public void removeEmployeeFromRole(int empID, int roleNumber) {
        String sql = "DELETE FROM EmployeeRole WHERE empID = ? AND roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empID);
            stmt.setInt(2, roleNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove employee from role", e);
        }
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeDTOsWithRoles() {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT DISTINCT e.* " +
                "FROM Employees e " +
                "JOIN EmployeeRole er ON e.empID = er.empID";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                employees.add(new EmployeeDTO(
                        rs.getInt("empID"),
                        rs.getString("empName"),
                        rs.getString("empPassword"),
                        rs.getString("empBankAccount"),
                        rs.getInt("empSalary"),
                        rs.getDate("empStartDate").toLocalDate(),
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee DTOs with roles", e);
        }

        return employees;
    }


    @Override
    public RoleDTO getByNumber(int roleNumber) {
        String sql = "SELECT * FROM Roles WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new RoleDTO(
                        rs.getInt("roleNumber"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch role", e);
        }
        return null;
    }

    @Override
    public List<Integer> getAllEmployeeIDsWithRoles() {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT DISTINCT empID FROM EmployeeRole";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("empID"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee IDs with roles", e);
        }
        return result;
    }

    @Override
    public List<RoleDTO> getAllByBranch(int branchId) throws SQLException {
        // Assuming there's a RequiredRoles table linking branchID and roleNumber
        String sql = "SELECT DISTINCT r.roleNumber, r.description FROM Roles r " +
                "JOIN RequiredRoles rr ON r.roleNumber = rr.roleNumber WHERE rr.branchID = ?";
        List<RoleDTO> roles = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                roles.add(new RoleDTO(
                        rs.getInt("roleNumber"),
                        rs.getString("description")
                ));
            }
        }
        return roles;
    }

    @Override
    public void delete(int roleNumber) throws SQLException {
        String sql = "DELETE FROM Roles WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleNumber);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<RoleDTO> getAll() throws SQLException {
        List<RoleDTO> roles = new ArrayList<>();
        String sql = "SELECT * FROM Roles";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new RoleDTO(
                        rs.getInt("roleNumber"),
                        rs.getString("description")
                ));
            }
        }
        return roles;
    }
}
