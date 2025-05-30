package HR_Mudol.DAO;

import HR_Mudol.DTO.*;
import HR_Mudol.DataBase.PostgresConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements IRoleDAO {
    private final Connection conn;

    public RoleDAOImpl() throws SQLException {
        this.conn = PostgresConnection.getConnection();
    }

    @Override
    public void insert(RoleDTO dto) {
        String sql = "INSERT INTO Roles (roleNumber, description) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getRoleNumber());
            stmt.setString(2, dto.getDescription());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert new role to DB", e);
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
        String sql = "DELETE FROM EmployeeRoles WHERE empID = ? AND roleNumber = ?";
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
        String sql = "SELECT DISTINCT e.* FROM Employees e JOIN EmployeeRoles er ON e.empID = er.empID";

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
                        rs.getInt("maxDayShifts"),
                        rs.getInt("maxEveningShifts"),
                        rs.getInt("sickDays"),
                        rs.getInt("vacationDays")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee DTOs with roles", e);
        }

        return employees;
    }

    @Override
    public List<RoleDTO> getAll() {
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch roles", e);
        }

        return roles;
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
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch role by number", e);
        }
    }

    @Override
    public List<Integer> getAllEmployeeIDsWithRoles() {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT DISTINCT empID FROM EmployeeRoles";

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
    public void delete(int roleNumber) throws SQLException {
        String sql = "DELETE FROM roles WHERE roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleNumber);
            stmt.executeUpdate();
        }
    }


}
