package HR_Mudol.DAO;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DataBase.PostgresConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAOImpl extends BaseDAO implements IBranchDAO {

    public BranchDAOImpl() throws SQLException {
        super();
    }

    @Override
    public List<BranchDTO> getAll() {
        List<BranchDTO> branches = new ArrayList<>();
        String sql = "SELECT branchID, name, district FROM Branches";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BranchDTO dto = new BranchDTO(
                        rs.getInt("branchID"),
                        rs.getString("name"),
                        rs.getString("district"),
                        null, // employees
                        null, // roles
                        null  // weeks
                );
                branches.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to fetch branches from database", e);
        }

        return branches;
    }

    @Override
    public void insert(BranchDTO dto) throws SQLException {
        String sql = "INSERT INTO Branches (branchID, name, district) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dto.getBranchID());
            stmt.setString(2, dto.getName());
            stmt.setString(3, dto.getDistrict());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Failed to insert new branch into database", e);
        }
    }

}
