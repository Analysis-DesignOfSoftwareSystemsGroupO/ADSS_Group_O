package SupplierMoudleSource.dao;

import dto.BranchDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DataBase.PostgresConnection.getConnection;

public class branchdao {

    public void addBranch(String  branchCity, String branchAddress) throws SQLException {
        String sql = "INSERT INTO supplierinventorydb.branch (city, address) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, branchCity);
            pstmt.setString(2, branchAddress);

            pstmt.executeUpdate();
            System.out.println("✅ Branch added successfully.");
        }
    }

    public BranchDTO getBranch(String branchCity, String branchAddress) throws SQLException {
        String sql = "SELECT * FROM supplierinventorydb.branch WHERE city = ? AND address = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, branchCity);
            pstmt.setString(2, branchAddress);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id");
                String city = rs.getString("city");
                String address = rs.getString("address");

                return new BranchDTO(id, city, address);
            } else {
                return null; // or throw custom NotFoundException
            }
        }
    }
}
