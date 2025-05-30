package SupplierMoudleSource.DAO;

import DTO.BranchDTO;
import SupplierMoudleSource.Domain.Branch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public BranchDTO getBranch(String branchId)throws SQLException {
        String sql = "Select * from supplierinventorydb.branch where id = ?";
        try (Connection con = getConnection()){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(branchId));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String city = rs.getString("city");
                String address = rs.getString("address");
                return new BranchDTO(branchId, city, address);
            }
            return null;
        }
    }

    public List<BranchDTO> getAllBranches() throws SQLException {
        String sql = "Select * from supplierinventorydb.branch";
        List<BranchDTO> list = new ArrayList<>();

        try (Connection connection = getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String city = rs.getString("city");
                String address = rs.getString("address");
                int id = rs.getInt("id");
                list.add(new BranchDTO(Integer.toString(id), city, address));
            }
            return list;

        }
    }
}