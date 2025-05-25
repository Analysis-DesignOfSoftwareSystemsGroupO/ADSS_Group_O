package HR_Mudol.DAO;

import HR_Mudol.DTO.EmployeeDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements IEmployeeDAO {
    private final Connection conn;

    public EmployeeDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(EmployeeDTO emp) throws SQLException {
        String sql = "INSERT INTO employees (empID, empName, empPassword, empBankAccount, empSalary, empStartDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, emp.getEmployeeId());
            stmt.setString(2, emp.getFullName());
            stmt.setString(3, emp.getPassword());
            stmt.setString(4, emp.getBankAccount());
            stmt.setInt(5, emp.getSalary());
            stmt.setDate(6, Date.valueOf(emp.getStartDate()));
            stmt.executeUpdate();
        }

        // insert to contract table
        String contractSql = "INSERT INTO contract (empID, daysOff, sickDays, minEveningShift, minDayShift) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(contractSql)) {
            stmt.setInt(1, emp.getEmployeeId());
            stmt.setInt(2, emp.getDaysOff());
            stmt.setInt(3, emp.getSickDays());
            stmt.setInt(4, emp.getMinEveningShift());
            stmt.setInt(5, emp.getMinDayShift());
            stmt.executeUpdate();
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
    public void delete(int empID) throws SQLException {
        try (PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM contract WHERE empID = ?");
             PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM employees WHERE empID = ?")) {

            stmt1.setInt(1, empID);
            stmt1.executeUpdate();

            stmt2.setInt(1, empID);
            stmt2.executeUpdate();
        }
    }

    @Override
    public EmployeeDTO get(int empID) throws SQLException {
        String empSql = "SELECT * FROM employees WHERE empID = ?";
        String contractSql = "SELECT * FROM contract WHERE empID = ?";

        try (PreparedStatement empStmt = conn.prepareStatement(empSql);
             PreparedStatement conStmt = conn.prepareStatement(contractSql)) {

            empStmt.setInt(1, empID);
            conStmt.setInt(1, empID);

            ResultSet empRs = empStmt.executeQuery();
            ResultSet conRs = conStmt.executeQuery();

            if (empRs.next() && conRs.next()) {
                return new EmployeeDTO(
                        empRs.getInt("empID"),
                        empRs.getString("empName"),
                        empRs.getString("empPassword"),
                        empRs.getString("empBankAccount"),
                        empRs.getInt("empSalary"),
                        empRs.getDate("empStartDate").toLocalDate(),
                        conRs.getInt("minDayShift"),
                        conRs.getInt("minEveningShift"),
                        conRs.getInt("sickDays"),
                        conRs.getInt("daysOff")
                );
            }
        }

        return null;
    }

    @Override
    public List<EmployeeDTO> getAll() throws SQLException {
        List<EmployeeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM employees JOIN contract USING(empID)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new EmployeeDTO(
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
        return list;
    }
}
