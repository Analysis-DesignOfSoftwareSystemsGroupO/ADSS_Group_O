package HR_Mudol.DAO;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DataBase.PostgresConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAOImpl implements IBranchDAO {
    private final Connection conn;

    public BranchDAOImpl() throws SQLException {
        this.conn = PostgresConnection.getConnection();
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
}
