package SupplierMoudleSource.DAO;

import DTO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DataBase.PostgresConnection.getConnection;

public class Supplierdao {
    public String addSupplier(String supplierName, BankDTO bank, String paymentMethod, DeliveryDTO delivery,
                            List<InformationContactDTO> informationContacts, List<SuppliedItemDTO> supplyProducts) {

        String supplierId;

        // Step 1: Insert supplier and get generated ID
        String supplierSql = "INSERT INTO supplier (name, paymentmethod, deliverymethod) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(supplierSql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, supplierName);
            pstmt.setString(2, paymentMethod);
            pstmt.setString(3, delivery.getDeliveryWay());
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
        //add to product catalog all product
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

        //add to bank table
        String sql = "INSERT INTO supplierinventorydb.bank (supplierid, bankaccountnumber, banknumber, bankbranch) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, supplierId);
            pstmt.setString(2, bank.getBankAccount());
            pstmt.setString(3, bank.getBankNumber());
            pstmt.setString(4, bank.getBankBranch());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to insert bank information", e);
        }


        //add to information contact
        sql = "INSERT INTO supplierinventorydb.informationcontact " +
                "(supplierid, contactname, contactphone, title) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            for (InformationContactDTO contact : informationContacts) {
                pstmt.setString(1, supplierId);
                pstmt.setString(2, contact.getContactName());
                pstmt.setString(3, contact.getContactPhone());
                pstmt.setString(4, contact.getTitle());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to insert contact information", e);
        }

        // Repeat similar blocks for `bank`, `delivery`, `informationContacts`, etc.
        return supplierId;
    }

    public boolean productexist(String supplierId, String productId) throws SQLException {
        String sql = "select * from productcatalog where supplierid = ? and productid = ?";
        try (Connection connection = getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, supplierId);
            pstmt.setString(2, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        }


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

    public SupplierDTO getSupplier(String supplierID) throws SQLException {
        String id = "", name = "", delivery = "", paymentMethod = "";

        // Step 1: Get supplier basic info
        String getSql = "SELECT * FROM supplier WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(getSql)) {

            pstmt.setString(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getString("id");
                name = rs.getString("name");
                delivery = rs.getString("deliverymethod");
                paymentMethod = rs.getString("paymentmethod");
            } else {
                return null; // supplier not found
            }
        }

        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO(paymentMethod);
        DeliveryDTO deliveryDTO = new DeliveryDTO(delivery);

        // Step 2: Get bank info
        BankDTO bankDTO = null;
        String bankSql = "SELECT * FROM bank WHERE supplierid = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(bankSql)) {

            pstmt.setString(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String bankBranch = rs.getString("bankbranch");
                String bankNumber = rs.getString("banknumber");
                String bankAccountNumber = rs.getString("bankaccountnumber");
                bankDTO = new BankDTO(bankAccountNumber, bankNumber, bankBranch, supplierID);
            }
        }

        // Step 3: Get contact list
        List<InformationContactDTO> informationContacts = getInformationContacts(supplierID);


        // Step 4: Get product catalog
        HashMap<String, SuppliedItemDTO> supplyProducts = new HashMap<>();
        String catalogSql = "SELECT * FROM productcatalog WHERE supplierid = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(catalogSql)) {

            pstmt.setString(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int price = rs.getInt("price");
                String productId = rs.getString("productid");

                // Get product details
                String productSql = "SELECT * FROM product WHERE id = ?";
                try (PreparedStatement productStmt = con.prepareStatement(productSql)) {
                    productStmt.setString(1, productId);
                    ResultSet rs2 = productStmt.executeQuery();
                    if (rs2.next()) {
                        String pname = rs2.getString("name");
                        String manufacturer = rs2.getString("manafacturer");
                        int shelfLifeDays = rs2.getInt("shelflifedays");

                        ProductDTO product = new ProductDTO(productId, pname, manufacturer, shelfLifeDays);
                        SuppliedItemDTO suppliedItem = new SuppliedItemDTO(price, product, productId);
                        supplyProducts.put(productId, suppliedItem);
                    }
                }
            }
        }
        return new SupplierDTO(supplierID, bankDTO, paymentMethodDTO, deliveryDTO, informationContacts, supplyProducts);
    }


    public List<SupplierDTO> getAllSuppliers() throws SQLException {
        List<SupplierDTO> suppliers = new ArrayList<>();
        String sql = "SELECT id FROM supplier";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String supplierId = rs.getString("id");
                SupplierDTO supplier = getSupplier(supplierId);
                suppliers.add(supplier);
            }
        }

        return suppliers;
    }

    public   List<InformationContactDTO> getInformationContacts(String supplierID) throws SQLException {
        List<InformationContactDTO> informationContacts = new ArrayList<>();
        String contactSql = "SELECT * FROM informationcontact WHERE supplierid = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(contactSql)) {

            pstmt.setString(1, supplierID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String contactName = rs.getString("contactname");
                String contactPhone = rs.getString("contactphone");
                String title = rs.getString("title");
                informationContacts.add(new InformationContactDTO(contactName, contactPhone, title));
            }
        }
        return informationContacts;
    }  // Step 3: Get contact list


    public void addproduct(String supplierid, SuppliedItemDTO suppliedItemDTO) throws SQLException {
        String sql = "INSERT INTO productcatalog (productid, supplierid, price) VALUES (?, ?, ?)";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, suppliedItemDTO.product.productID);
            pstmt.setString(2, supplierid);
            pstmt.setInt(3, suppliedItemDTO.suppliedItemPrice);
            pstmt.executeUpdate();
        }
    }

    public void editinformationcotact(String supplierid, InformationContactDTO informationContactDTO) throws SQLException {
        String sql = "UPDATE informationcontact SET title=?, contactphone=? WHERE contactname=? and supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, informationContactDTO.getTitle());
            pstmt.setString(2, informationContactDTO.getContactPhone());
            pstmt.setString(3, informationContactDTO.getContactName());
            pstmt.setString(4, supplierid);
        }

    }

    public void addinformationcontact(String supplierid, InformationContactDTO informationContactDTO) throws SQLException {
        String sql = "INSERT into informationcontact (supplier, contactname, contactphone, title) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, supplierid);
            pstmt.setString(2, informationContactDTO.getContactName());
            pstmt.setString(3, informationContactDTO.getContactPhone());
            pstmt.setString(4, informationContactDTO.getTitle());
            pstmt.executeUpdate();
        }
    }

    public void editsuppliername(String supplierid, String name) throws SQLException {
        String sql = "UPDATE supplier SET suppliername=? WHERE supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, supplierid);
            pstmt.executeUpdate();
        }
    }

    public void editbank(String supplierid, BankDTO bankDTO) throws SQLException {
        String sql = "UPDATE bank SET bankbranch=?, banknumber=?, bankaccountnumber=? WHERE supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bankDTO.getBankBranch());
            pstmt.setString(2, bankDTO.getBankNumber());
            pstmt.setString(3, bankDTO.getBankAccount());
            pstmt.setString(4, supplierid);
            pstmt.executeUpdate();
        }
    }

    public void editdelivery(String supplierid, DeliveryDTO deliveryDTO) throws SQLException {
        String sql = "UPDATE supplier SET deliverymethod=? WHERE supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, deliveryDTO.getDeliveryWay());
            pstmt.setString(2, supplierid);
            pstmt.executeUpdate();
        }
    }

}



