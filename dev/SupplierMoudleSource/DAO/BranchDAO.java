package SupplierMoudleSource.DAO;

import SupplierMoudleSource.Domain.Branch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DataBase.PostgresConnection.getConnection;

public class BranchDAO {
    public BranchDAO(){

    }

    public void addBranch(String branchCity, String branchAddress) throws SQLException {
        if (isBranchExist(branchCity, branchAddress)) {
            return;
        }
        String sql = "INSERT INTO supplierinventorydb.branch (city, address) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, branchCity);
            pstmt.setString(2, branchAddress);
            pstmt.executeUpdate();
        }
    }

    private boolean isBranchExist(String branchCity, String branchAddress) throws SQLException {
        String sql = "Select * from supplierinventorydb.branch where city = ? and address = ?";
        try (Connection con = getConnection()){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, branchCity);
            pstmt.setString(2, branchAddress);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        }

    }


}

