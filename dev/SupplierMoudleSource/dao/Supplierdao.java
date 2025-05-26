package SupplierMoudleSource.dao;

import SupplierMoudleSource.Domain.PaymentMethod;
import SupplierMoudleSource.Domain.Supplier;
import dto.BankDTO;
import dto.DeliveryDTO;
import dto.InformationContactDTO;
import dto.SuppliedItemDTO;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static DataBase.PostgresConnection.getConnection;

public class Supplierdao {
    public String addSupplier(String supplierName, BankDTO bank, String paymentMethod, DeliveryDTO delivery,
                            List<InformationContactDTO> informationContacts, List<SuppliedItemDTO> supplyProducts) {

        String supplierId;

        // Step 1: Insert supplier and get generated ID
        String supplierSql = "INSERT INTO supplier (name, paymentmethod) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(supplierSql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, supplierName);
            pstmt.setString(2, paymentMethod);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                supplierId = rs.getString(1);
            } else {
                throw new SQLException("Failed to retrieve generated supplier ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting supplier", e);
        }

        // Step 2: Use supplierId in subsequent inserts
        for (SuppliedItemDTO supply : supplyProducts) {
            // For example: insert into productcatalog
            String insertProductCatalogSql = "INSERT INTO productcatalog (productid, supplierid, price) VALUES (?, ?, ?)";
            try (Connection con = getConnection();
                 PreparedStatement pstmt = con.prepareStatement(insertProductCatalogSql)) {
                pstmt.setString(1, supply.product.productID);      // replace with your method
                pstmt.setString(2, supplierId);
                pstmt.setInt(3, supply.suppliedItemPrice);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting productcatalog for supplier", e);
            }
        }
        // Repeat similar blocks for `bank`, `delivery`, `informationContacts`, etc.
        return supplierId;
    }

    public void removeSupplier(String supplierID){
        String removeSupplierSql = "DELETE FROM supplier WHERE supplierID=?";

        try (Connection connection = getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(removeSupplierSql);
            pstmt.setString(1, supplierID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSuppliers(String supplierID){
        String getql = "SELECT * FROM supplier WHERE supplierID=?";

    }

    public List<Supplier> getAllSuppliers(){

    }


}
