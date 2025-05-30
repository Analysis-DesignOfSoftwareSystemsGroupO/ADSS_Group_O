package HR_Mudol.DAO;

import HR_Mudol.DTO.EmploymentContractDTO;
import HR_Mudol.DataBase.PostgresConnection;

import java.sql.*;

public class EmploymentContractDAOImpl implements IEmploymentContractDAO {
    private final Connection conn;

    public EmploymentContractDAOImpl() throws SQLException {
        this.conn = PostgresConnection.getConnection();
    }

    @Override
    public void insert(EmploymentContractDTO dto) {
        String sql = "INSERT INTO EmploymentContracts (empId, minDayShift, minEveningShift, sickDays, daysOff) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getOwnerId());
            stmt.setInt(2, dto.getMinDayShift());
            stmt.setInt(3, dto.getMinEveningShift());
            stmt.setInt(4, dto.getSickDays());
            stmt.setInt(5, dto.getDaysOff());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert EmploymentContract", e);
        }
    }

    @Override
    public void update(EmploymentContractDTO dto) {
        String sql = "UPDATE EmploymentContracts SET minDayShift = ?, minEveningShift = ?, sickDays = ?, daysOff = ? WHERE empId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getMinDayShift());
            stmt.setInt(2, dto.getMinEveningShift());
            stmt.setInt(3, dto.getSickDays());
            stmt.setInt(4, dto.getDaysOff());
            stmt.setInt(5, dto.getOwnerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update EmploymentContract", e);
        }
    }

    @Override
    public void delete(int empId) {
        String sql = "DELETE FROM EmploymentContracts WHERE empId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete EmploymentContract", e);
        }
    }

    @Override
    public EmploymentContractDTO findByEmpId(int empId) {
        String sql = "SELECT * FROM EmploymentContracts WHERE empId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EmploymentContractDTO(
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff"),
                        rs.getInt("empId")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find EmploymentContract", e);
        }
        return null;
    }
}
