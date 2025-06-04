package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl extends BaseDAO implements IEmployeeDAO {


    public EmployeeDAOImpl() throws SQLException {
        super();
    }


    @Override
    public void insert(EmployeeDTO dto, int brunchID) {
        String sql = "INSERT INTO Employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate, " +
                "minDayShift, minEveningShift, sickDays, daysOff, branchID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (empID) DO NOTHING";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, dto.getEmployeeId());
            stmt.setString(2, dto.getFullName());
            stmt.setString(3, dto.getPassword());
            stmt.setString(4, dto.getBankAccount());
            stmt.setInt(5, dto.getSalary());
            stmt.setDate(6, java.sql.Date.valueOf(dto.getStartDate()));
            stmt.setInt(7, dto.getMinDayShift());
            stmt.setInt(8, dto.getMinEveningShift());
            stmt.setInt(9, dto.getSickDays());
            stmt.setInt(10, dto.getDaysOff());
            stmt.setInt(11, brunchID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert employee", e);
        }
    }

    @Override
    public void update(EmployeeDTO emp) throws SQLException {
        String sql = "UPDATE Employees SET empName = ?, empPassword = ?, empBankAccount = ?, empSalary = ?, empStartDate = ?, minDayShift = ?, minEveningShift = ?, sickDays = ?, daysOff = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emp.getFullName());
            stmt.setString(2, emp.getPassword());
            stmt.setString(3, emp.getBankAccount());
            stmt.setInt(4, emp.getSalary());
            stmt.setDate(5, Date.valueOf(emp.getStartDate()));
            stmt.setInt(6, emp.getMinDayShift());
            stmt.setInt(7, emp.getMinEveningShift());
            stmt.setInt(8, emp.getSickDays());
            stmt.setInt(9, emp.getDaysOff());
            stmt.setLong(10, emp.getEmployeeId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void archive(int empId) throws SQLException {
        String insertSQL = "INSERT INTO Archived_Employees (empID, archiveDate) SELECT empID, CURRENT_DATE FROM Employees WHERE empID = ?";
        String deleteSQL = "DELETE FROM Employees WHERE empID = ?";

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
        String sql = "UPDATE Employees SET empBankAccount = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newBankAccount);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateSalary(int empId, int newSalary) throws SQLException {
        String sql = "UPDATE Employees SET empSalary = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newSalary);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateMinDayShift(int empId, int newMinDayShift) throws SQLException {
        String sql = "UPDATE Employees SET minDayShift = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newMinDayShift);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateMinEveningShift(int empId, int newMinEveningShift) throws SQLException {
        String sql = "UPDATE Employees SET minEveningShift = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newMinEveningShift);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateSickDays(int empId, int newSickDays) throws SQLException {
        String sql = "UPDATE Employees SET sickDays = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newSickDays);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateDaysOff(int empId, int daysOff) throws SQLException {
        String sql = "UPDATE Employees SET daysOff = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, daysOff);
            stmt.setInt(2, empId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updatePassword(long empId, String newPassword) {
        String sql = "UPDATE Employees SET empPassword = ? WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setLong(2, empId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update password", e);
        }
    }

    @Override
    public EmployeeDTO getById(long employeeId) {
        String sql = "SELECT * FROM Employees WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Date rawDate = rs.getDate("empStartDate");
                LocalDate empStartDate = (rawDate != null) ? rawDate.toLocalDate() : LocalDate.now(); // או LocalDate.of(2000, 1, 1) אם את רוצה ערך ברירת מחדל אחר

                return new EmployeeDTO(
                        rs.getInt("empID"),
                        rs.getString("empName"),
                        rs.getString("empPassword"),
                        rs.getString("empBankAccount"),
                        rs.getInt("empSalary"),
                        empStartDate,
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
            throw new RuntimeException("Failed to fetch employees", e);
        }
        return result;
    }

    @Override
    public boolean exists(long employeeId) {
        String sql = "SELECT 1 FROM Employees WHERE empID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check employee existence", e);
        }
    }

    @Override
    public List<EmployeeDTO> getAllByBranch(int branchId) throws SQLException {
        List<EmployeeDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE branchID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new EmployeeDTO(
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
        }

        return result;
    }
    @Override
    public boolean isEmployeeInBranch(int empId, int branchId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Employees WHERE empID = ? AND branchID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.setInt(2, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

}
