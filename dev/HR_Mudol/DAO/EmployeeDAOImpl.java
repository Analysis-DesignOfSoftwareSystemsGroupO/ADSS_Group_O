package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements IEmployeeDAO {
    private final Connection conn;

    public EmployeeDAOImpl() throws SQLException {
        this.conn = DataBase.PostgresConnection.getConnection();
    }

    @Override
    public void insert(EmployeeDTO dto) {
        String sql = "INSERT INTO Employees (employeeId, fullName, password, bankAccount, salary, startDate, " +
                "minDayShift, minEveningShift, sickDays, daysOff) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getEmployeeId());
            stmt.setString(2, dto.getFullName());
            stmt.setString(3, dto.getPassword());
            stmt.setString(4, dto.getBankAccount());
            stmt.setInt(5, dto.getSalary());
            stmt.setDate(6, java.sql.Date.valueOf(dto.getStartDate()));
            stmt.setInt(7, dto.getMinDayShift());
            stmt.setInt(8, dto.getMinEveningShift());
            stmt.setInt(9, dto.getSickDays());
            stmt.setInt(10, dto.getDaysOff());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert employee", e);
        }
    }

    @Override
    public void update(EmployeeDTO emp) throws SQLException {
        String sql = "UPDATE employees SET empName = ?, empPassword = ?, empBankAccount = ?, empSalary = ?, empStartDate = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getFullName());
            stmt.setString(2, emp.getPassword());
            stmt.setString(3, emp.getBankAccount());
            stmt.setInt(4, emp.getSalary());
            stmt.setDate(5, Date.valueOf(emp.getStartDate()));
            stmt.setInt(6, emp.getEmployeeId());
            stmt.executeUpdate();
        }

        String contractSql = "UPDATE contract SET daysOff = ?, sickDays = ?, minEveningShift = ?, minDayShift = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(contractSql)) {
            stmt.setInt(1, emp.getDaysOff());
            stmt.setInt(2, emp.getSickDays());
            stmt.setInt(3, emp.getMinEveningShift());
            stmt.setInt(4, emp.getMinDayShift());
            stmt.setInt(5, emp.getEmployeeId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void archive(int empId) throws SQLException {
        String insertSQL = "INSERT INTO archived_employees SELECT * FROM employees WHERE id = ?";
        String deleteSQL = "DELETE FROM employees WHERE id = ?";

        try (
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)
        ) {
            insertStmt.setInt(1, empId);
            insertStmt.executeUpdate();

            deleteStmt.setInt(1, empId);
            deleteStmt.executeUpdate();
        }
    }

    @Override
    public void updateBankAccount(int empId, String newBankAccount) throws SQLException {
        String sql = "UPDATE employees SET bank_account = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newBankAccount);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateSalary(int empId, int newSalary) throws SQLException {
        String sql = "UPDATE employees SET salary = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newSalary);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateMinDayShift(int empId, int newMinDayShift) throws SQLException {
        String sql = "UPDATE employees SET min_day_shift = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newMinDayShift);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateMinEveningShift(int empId, int newMinEveningShift) throws SQLException {
        String sql = "UPDATE employees SET min_evening_shift = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newMinEveningShift);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateSickDays(int empId, int newSickDays) throws SQLException {
        String sql = "UPDATE employees SET sick_days = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newSickDays);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateDaysOff(int empId, int daysOff) throws SQLException {
        String sql = "UPDATE employees SET days_off = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, daysOff);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }


    @Override
    public EmployeeDTO getById(int employeeId) {
        String sql = "SELECT * FROM Employees WHERE employeeId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EmployeeDTO(
                        rs.getInt("employeeId"),
                        rs.getString("fullName"),
                        rs.getString("password"),
                        rs.getString("bankAccount"),
                        rs.getInt("salary"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employee", e);
        }
    }

    @Override
    public List<EmployeeDTO> getAll() {
        List<EmployeeDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM Employees";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new EmployeeDTO(
                        rs.getInt("employeeId"),
                        rs.getString("fullName"),
                        rs.getString("password"),
                        rs.getString("bankAccount"),
                        rs.getInt("salary"),
                        rs.getDate("startDate").toLocalDate(),
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch employees", e);
        }
        return result;
    }

    @Override
    public boolean exists(int employeeId) {
        String sql = "SELECT 1 FROM Employees WHERE employeeId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check employee existence", e);
        }
    }
}
