package SupplierMoudleSource.DAO;

import dto.SuppliedItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DataBase.PostgresConnection.getConnection;

public class agreementdao {
    public void addagreement(String branchid, String supplierid){
        String sql = "insert into agreement (branchid, supplierid) values(?,?)";
        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, branchid);
            ps.setString(2, supplierid);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addproducttoagreement(String branchid, String supplierid, SuppliedItemDTO suppliedItemDTO) throws SQLException {
        if (!checkagreement(branchid, supplierid)){
            throw new SQLException("Agreement not exist");
        }
        String sql = "insert into suppliedItem (branchid, supplierid, productid, price) values(?,?,?,?)";
        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, branchid);
            ps.setString(2, supplierid);
            ps.setString(3, suppliedItemDTO.product.productID);
            ps.setInt(4, suppliedItemDTO.suppliedItemPrice);
            ps.executeUpdate();
        }
    }



    private boolean checkagreement(String branchid, String supplierid) throws SQLException {
        String sql = "select * from agreement where branchid = ? and supplierid = ?";
        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, branchid);
            ps.setString(2, supplierid);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }
    }
}
