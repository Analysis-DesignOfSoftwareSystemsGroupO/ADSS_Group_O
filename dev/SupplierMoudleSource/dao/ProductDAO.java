package SupplierMoudleSource.dao;

import DTO.ProductDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DataBase.PostgresConnection.getConnection;

public class ProductDAO {

    public String addProduct(String productName, String productManufacturer, int shelfLife) throws SQLException {
        String sql = "INSERT INTO supplierinventorydb.product (name, manufacturer, shelfLifeDays) VALUES (?, ?, ?)";

        String productID;
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, productName);
            pstmt.setString(2, productManufacturer);
            pstmt.setInt(3, shelfLife);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            productID = rs.getString(1);
        }
        return productID;
    }

    public ProductDTO getProduct(String id) throws SQLException {
        String sql = "SELECT * FROM product WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            String pID = null;
            String pName = null;
            String pManufacturer = null;
            int shelfLifeDays = 0;
            if (rs.next()) {
                pID = rs.getString(1);
                pName = rs.getString(2);
                pManufacturer = rs.getString(3);
                shelfLifeDays = rs.getInt(4);
            }
            ProductDTO pDTO = new ProductDTO(pID, pName, pManufacturer, shelfLifeDays);
            return pDTO;
        }
    }
}
