package SupplierMoudleSource.DAO;

import DTO.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DataBase.PostgresConnection.getConnection;

public class SupplierDAO {
    public String addSupplier(String supplierName, BankDTO bank, PaymentMethodDTO paymentMethod, DeliveryDTO delivery,
                              InformationContactDTO informationContactDTO) {

        int supplierId;

        // Step 1: Insert supplier and get generated ID
        String supplierSql = "INSERT INTO supplierinventorydb.supplier (name, paymentmethod, deliverymethod) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(supplierSql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, supplierName);
            pstmt.setString(2, paymentMethod.getPaymentMethod());
            pstmt.setString(3, delivery.getDeliveryWay());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                supplierId = rs.getInt("id");
            } else {
                throw new SQLException("Failed to retrieve generated supplier ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting supplier", e);
        }


        //add to bank table
        String sql = "INSERT INTO supplierinventorydb.bank (supplierid, bankaccountnumber, banknumber, bankbranch) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, supplierId);
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


            pstmt.setInt(1, supplierId);
            pstmt.setString(2, informationContactDTO.getContactName());
            pstmt.setString(3, informationContactDTO.getContactPhone());
            pstmt.setString(4, informationContactDTO.getTitle());
            pstmt.addBatch();


            pstmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to insert contact information", e);
        }

        // Repeat similar blocks for `bank`, `delivery`, `informationContacts`, etc.
        return Integer.toString(supplierId);
    }

    public boolean productExist(String supplierId, String productId) throws SQLException {
        String sql = "select * from supplierinventorydb.productofsupplier where supplierid = ? and productid = ?";
        try (Connection connection = getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(supplierId));
            pstmt.setInt(2, Integer.parseInt(productId));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        }


    }


    public void removeSupplier(String supplierID){
        String removeSupplierSql = "DELETE FROM supplierinventorydb.supplier WHERE id=?";

        try (Connection connection = getConnection()){
            PreparedStatement pstmt = connection.prepareStatement(removeSupplierSql);
            pstmt.setInt(1, Integer.parseInt(supplierID));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SupplierDTO getSupplier(String supplierID) throws SQLException {
        String name = "", delivery = "", paymentMethod = "";

        // Step 1: Get supplier basic info
        String getSql = "SELECT * FROM supplierinventorydb.supplier WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(getSql)) {

            pstmt.setInt(1, Integer.parseInt(supplierID));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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
        String bankSql = "SELECT * FROM supplierinventorydb.bank WHERE supplierid = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(bankSql)) {

            pstmt.setInt(1, Integer.parseInt(supplierID));
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
        String catalogSql = "SELECT * FROM supplierinventorydb.productofsupplier WHERE supplierid = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(catalogSql)) {

            pstmt.setInt(1, Integer.parseInt(supplierID));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int price = rs.getInt("price");
                int productId = rs.getInt("productid");

                // Get product details
                String productSql = "SELECT * FROM supplierinventorydb.product WHERE id = ?";
                try (PreparedStatement productStmt = con.prepareStatement(productSql)) {
                    productStmt.setInt(1, productId);
                    ResultSet rs2 = productStmt.executeQuery();
                    if (rs2.next()) {
                        String pname = rs2.getString("name");
                        String manufacturer = rs2.getString("manufacturer");
                        int shelfLifeDays = rs2.getInt("shelflifedays");
                        String productString = Integer.toString(productId);
                        ProductDTO product = new ProductDTO(productString, pname, manufacturer, shelfLifeDays);
                        SuppliedItemDTO suppliedItem = new SuppliedItemDTO(price, product);
                        supplyProducts.put(productString, suppliedItem);
                    }
                }
            }
        }catch (SQLException e) {

        }
        return new SupplierDTO(supplierID, name, bankDTO, paymentMethodDTO, deliveryDTO, informationContacts, supplyProducts);
    }


    public List<SupplierDTO> getAllSuppliers() throws SQLException {
        List<SupplierDTO> suppliers = new ArrayList<>();
        String sql = "SELECT id FROM supplierinventorydb.supplier";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int supplierId = rs.getInt("id");
                SupplierDTO supplier = getSupplier(Integer.toString(supplierId));
                suppliers.add(supplier);
            }
        }

        return suppliers;
    }

    public   List<InformationContactDTO> getInformationContacts(String supplierID) throws SQLException {
        List<InformationContactDTO> informationContacts = new ArrayList<>();
        String contactSql = "SELECT * FROM supplierinventorydb.informationcontact WHERE supplierid = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(contactSql)) {

            pstmt.setInt(1, Integer.parseInt(supplierID));
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


    public void addProduct(String supplierid, SuppliedItemDTO suppliedItemDTO) throws Exception {
        String sql = "INSERT INTO supplierinventorydb.productofsupplier (productid, supplierid, price) VALUES (?, ?, ?)";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(suppliedItemDTO.product.productID));
            pstmt.setInt(2, Integer.parseInt(supplierid));
            pstmt.setInt(3, suppliedItemDTO.suppliedItemPrice);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            throw new Exception("Product already exists");
        }
    }

    public void editInformationCotact(String supplierid, InformationContactDTO informationContactDTO) throws SQLException {
        String sql = "UPDATE supplierinventorydb.informationcontact SET title=?, contactphone=? WHERE contactname=? and supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, informationContactDTO.getTitle());
            pstmt.setString(2, informationContactDTO.getContactPhone());
            pstmt.setString(3, informationContactDTO.getContactName());
            pstmt.setInt(4, Integer.parseInt(supplierid));
        }

    }

    public void addInformationContact(String supplierid, InformationContactDTO informationContactDTO) throws Exception {
        String checksql = "SELECT * FROM supplierinventorydb.informationcontact where supplierid = ? and contactname = ?";
        String sql = "INSERT into supplierinventorydb.informationcontact (supplierid, contactname, contactphone, title) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(checksql);
            pstmt.setInt(1, Integer.parseInt(supplierid));
            pstmt.setString(2, informationContactDTO.getContactName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                throw new Exception("Contact name already exists");
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(supplierid));
            pstmt.setString(2, informationContactDTO.getContactName());
            pstmt.setString(3, informationContactDTO.getContactPhone());
            pstmt.setString(4, informationContactDTO.getTitle());
            pstmt.executeUpdate();
        }
    }

    public void editSupplierName(String supplierid, String name) throws SQLException {
        String sql = "UPDATE supplierinventorydb.supplier SET name=? WHERE id=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(supplierid));
            pstmt.executeUpdate();
        }
    }

    public void editBank(String supplierid, BankDTO bankDTO) throws SQLException {
        String sql = "UPDATE supplierinventorydb.bank SET bankbranch=?, banknumber=?, bankaccountnumber=? WHERE supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, bankDTO.getBankBranch());
            pstmt.setString(2, bankDTO.getBankNumber());
            pstmt.setString(3, bankDTO.getBankAccount());
            pstmt.setInt(4, Integer.parseInt(supplierid));
            pstmt.executeUpdate();
        }
    }

    public void editDelivery(String supplierid, DeliveryDTO deliveryDTO) throws SQLException {
        String sql = "UPDATE supplierinventorydb.supplier SET deliverymethod=? WHERE supplierid=?";
        try (Connection con = getConnection();){
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, deliveryDTO.getDeliveryWay());
            pstmt.setInt(2, Integer.parseInt(supplierid));
            pstmt.executeUpdate();
        }
    }

}