package SupplierMoudleSource.DAO;

import SupplierMoudleSource.Domain.Branch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static DataBase.PostgresConnection.getConnection;

public class BranchDAO {

    public void addBrunch(String branchCity, String branchAddress) throws SQLException {
        String sql = "INSERT INTO supplierinventorydb.branch (city, address) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, branchCity);
            pstmt.setString(2, branchAddress);
            pstmt.executeUpdate();
            System.out.println("✅ Branch added successfully.");
        }
    }

    public BranchDAO() throws SQLException {
    }
}

