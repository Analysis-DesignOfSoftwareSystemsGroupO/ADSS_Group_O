package HR_Mudol.DAO;

import HR_Mudol.DTO.EmploymentContractDTO;
import HR_Mudol.DataBase.PostgresConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmploymentContractDAOImpl extends BaseDAO implements IEmploymentContractDAO {

    public EmploymentContractDAOImpl() throws SQLException {
        super();
    }

    @Override
    public void insert(EmploymentContractDTO contract) throws SQLException {
        String sql = "INSERT INTO EmploymentContracts (minDayShift, minEveningShift, sickDays, daysOff, ownerID) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contract.getMinDayShift());
            stmt.setInt(2, contract.getMinEveningShift());
            stmt.setInt(3, contract.getSickDays());
            stmt.setInt(4, contract.getDaysOff());
            stmt.setInt(5, contract.getOwnerId());
            stmt.executeUpdate();
        }
    }

    @Override
    public EmploymentContractDTO findByEmpId(int ownerId) throws SQLException {
        String sql = "SELECT * FROM EmploymentContracts WHERE ownerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new EmploymentContractDTO(
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff"),
                        rs.getInt("ownerID")
                );
            }
        }
        return null;
    }

    @Override
    public void update(EmploymentContractDTO contract) throws SQLException {
        String sql = "UPDATE EmploymentContracts SET minDayShift = ?, minEveningShift = ?, sickDays = ?, daysOff = ? WHERE ownerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contract.getMinDayShift());
            stmt.setInt(2, contract.getMinEveningShift());
            stmt.setInt(3, contract.getSickDays());
            stmt.setInt(4, contract.getDaysOff());
            stmt.setInt(5, contract.getOwnerId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int ownerId) throws SQLException {
        String sql = "DELETE FROM EmploymentContracts WHERE ownerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<EmploymentContractDTO> getAll() throws SQLException {
        List<EmploymentContractDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM EmploymentContracts";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new EmploymentContractDTO(
                        rs.getInt("minDayShift"),
                        rs.getInt("minEveningShift"),
                        rs.getInt("sickDays"),
                        rs.getInt("daysOff"),
                        rs.getInt("ownerID")
                ));
            }
        }
        return list;
    }
}
