package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl extends BaseDAO implements IRoleDAO {

    private final EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();

    public RoleDAOImpl() throws SQLException {
        super();
    }

    @Override
    public void insert(RoleDTO dto) throws SQLException {
        String sql = "INSERT INTO Roles (description) VALUES (?) ON CONFLICT (description) DO NOTHING";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dto.getDescription());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                dto.setRoleNumber(generatedId); // אם נוסף חדש – נקבל את ה-ID
            }
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

    public void assignEmployeeToRole(long empID, int roleNumber) {
        String sql = "INSERT INTO EmployeeRole (empID, roleNumber) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empID);
            stmt.setInt(2, roleNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to assign employee to role", e);
        }
    }

    public void removeEmployeeFromRole(long empID, int roleNumber) {
        String sql = "DELETE FROM EmployeeRole WHERE empID = ? AND roleNumber = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empID);
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
                String description = rs.getString("description");

                // טען את כל העובדים עם התפקיד הזה דרך טבלת קישור
                List<EmployeeDTO> relevantEmployees = new ArrayList<>();
                String employeeSql = "SELECT empID FROM EmployeeRole WHERE roleNumber = ?";
                try (PreparedStatement empStmt = conn.prepareStatement(employeeSql)) {
                    empStmt.setInt(1, roleNumber);
                    ResultSet empRs = empStmt.executeQuery();
                    while (empRs.next()) {
                        long empId = empRs.getLong("empID");
                        EmployeeDTO empDto = employeeDAO.getById(empId);
                        if (empDto != null) {
                            relevantEmployees.add(empDto);
                        }
                    }
                }

                return new RoleDTO(roleNumber, description, relevantEmployees);
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
                int roleNumber = rs.getInt("roleNumber");
                String description = rs.getString("description");

                // טען עובדים רלוונטיים לתפקיד הזה
                List<EmployeeDTO> relevantEmployees = new ArrayList<>();
                String empSql = "SELECT empID FROM EmployeeRole WHERE roleNumber = ?";
                try (PreparedStatement empStmt = conn.prepareStatement(empSql)) {
                    empStmt.setInt(1, roleNumber);
                    ResultSet empRs = empStmt.executeQuery();
                    while (empRs.next()) {
                        long empId = empRs.getLong("empID");
                        EmployeeDTO empDTO = employeeDAO.getById(empId);
                        if (empDTO != null) {
                            relevantEmployees.add(empDTO);
                        }
                    }
                }

                roles.add(new RoleDTO(roleNumber, description, relevantEmployees));
            }
        }
        return roles;
    }


    @Override
    public List<RoleDTO> getRolesByEmpId(long empId) {
        List<RoleDTO> roles = new ArrayList<>();
        String sql = "SELECT r.roleNumber, r.description " +
                "FROM Roles r " +
                "JOIN EmployeeRole er ON r.roleNumber = er.roleNumber " +
                "WHERE er.empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, empId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int roleNumber = rs.getInt("roleNumber");
                String description = rs.getString("description");

                // טען את כל העובדים שמקושרים לתפקיד הזה
                List<EmployeeDTO> relevantEmployees = new ArrayList<>();
                String empSql = "SELECT empID FROM EmployeeRole WHERE roleNumber = ?";
                try (PreparedStatement empStmt = conn.prepareStatement(empSql)) {
                    empStmt.setInt(1, roleNumber);
                    ResultSet empRs = empStmt.executeQuery();
                    while (empRs.next()) {
                        long relEmpId = empRs.getLong("empID");
                        EmployeeDTO empDTO = employeeDAO.getById(relEmpId);
                        if (empDTO != null) {
                            relevantEmployees.add(empDTO);
                        }
                    }
                }

                roles.add(new RoleDTO(roleNumber, description, relevantEmployees));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get roles for employee ID: " + empId, e);
        }
        return roles;
    }

    @Override
    public RoleDTO getByDescription(String description) throws SQLException {
        String sql = "SELECT roleNumber, description FROM Roles WHERE LOWER(description) = LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, description);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int roleNumber = rs.getInt("roleNumber");
                String desc = rs.getString("description");

                // טען את כל העובדים שמקושרים לתפקיד הזה
                List<EmployeeDTO> relevantEmployees = new ArrayList<>();
                String empSql = "SELECT empID FROM EmployeeRole WHERE roleNumber = ?";
                try (PreparedStatement empStmt = conn.prepareStatement(empSql)) {
                    empStmt.setInt(1, roleNumber);
                    ResultSet empRs = empStmt.executeQuery();
                    while (empRs.next()) {
                        long empId = empRs.getLong("empID");
                        EmployeeDTO empDTO = employeeDAO.getById(empId);
                        if (empDTO != null) {
                            relevantEmployees.add(empDTO);
                        }
                    }
                }

                return new RoleDTO(roleNumber, desc, relevantEmployees);
            }
        }
        return null;
    }


    @Override
    public void deleteByDescription(String description) throws SQLException {
        String sql = "DELETE FROM Roles WHERE LOWER(description) = LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<EmployeeDTO> getEmployeesForRole(int roleNumber, int branchID) {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT e.* FROM Employees e " +
                "JOIN EmployeeRole er ON e.empID = er.empID " +
                "WHERE er.roleNumber = ? AND e.branchID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleNumber);
            stmt.setInt(2, branchID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Date rawDate = rs.getDate("empStartDate");
                LocalDate empStartDate = (rawDate != null) ? rawDate.toLocalDate() : LocalDate.now();

                employees.add(new EmployeeDTO(
                        rs.getLong("empID"),
                        rs.getString("empName"),
                        rs.getString("empPassword"),
                        rs.getString("empBankAccount"),
                        rs.getInt("empSalary"),
                        empStartDate,
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employees for role and branch", e);
        }

        return employees;
}




}
