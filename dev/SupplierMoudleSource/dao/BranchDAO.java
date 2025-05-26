package SupplierMoudleSource.dao;

import DTO.BranchDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static DataBase.PostgresConnection.getConnection;

public class BranchDAO {

    public void addBranch(BranchDTO branch) throws SQLException {
        String sql = "INSERT INTO supplierinventorydb.branch (city, address) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, branch.getCity());
            pstmt.setString(2, branch.getAddress());

            pstmt.executeUpdate();
            System.out.println("✅ Branch added successfully.");
        }
    }
}
