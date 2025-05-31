package SupplierMoudleSource.DAO;

import SupplierMoudleSource.DTO.ProductDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static SupplierMoudleSource.DataBase.PostgresConnection.getConnection;

public class ProductDAO {

    public String addProduct(String productName, String productManufacturer, int shelfLife) throws SQLException {
        String checkSql = "SELECT * from supplierinventorydb.product where name=? and manufacturer=? and shelflifedays=?";
        String sql = "INSERT INTO supplierinventorydb.product (name, manufacturer, shelfLifeDays) VALUES (?, ?, ?)";
        String productID;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(checkSql);
             PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // First query (check)
            ps.setString(1, productName);
            ps.setString(2, productManufacturer);
            ps.setInt(3, shelfLife);

            ResultSet checkResult = ps.executeQuery();
            if (checkResult.next()) {
                return Integer.toString(checkResult.getInt(1));
            }

            // Insert new product
            pstmt.setString(1, productName);
            pstmt.setString(2, productManufacturer);
            pstmt.setInt(3, shelfLife);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            productID = Integer.toString(rs.getInt(1));
        }

        return productID;
    }

    public ProductDTO getProduct(String id) throws SQLException {
        String sql = "SELECT * FROM supplierinventorydb.product WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();

            int pID = 0;
            String pName = null;
            String pManufacturer = null;
            int shelfLifeDays = 0;
            if (rs.next()) {
                pID = rs.getInt(1);
                pName = rs.getString(2);
                pManufacturer = rs.getString(3);
                shelfLifeDays = rs.getInt(4);
            }
            ProductDTO pDTO = new ProductDTO(Integer.toString(pID), pName, pManufacturer, shelfLifeDays);
            return pDTO;
        }
    }

    public ProductDTO getProduct(String productName, String manufacturer) throws SQLException {
        String sql = "SELECT * FROM supplierinventorydb.product WHERE name = ? and manufacturer = ?";
        try (Connection con = getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, productName);
            pstmt.setString(2, manufacturer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ProductDTO(Integer.toString(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getInt(4));
            }
            return null;
        }
    }
}
