package SupplierMoudleSource.DAO;

import SupplierMoudleSource.DTO.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static SupplierMoudleSource.DataBase.PostgresConnection.getConnection;

public class AgreementDAO {
    public void addAgreement(String branchid, String supplierid) throws Exception {
        String sql = "insert into supplierinventorydb.agreement (branchid, supplierid) values(?,?)";
        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Agreement already exists");
        }
    }

    public void addProductToAgreement(String branchid, String supplierid, SuppliedItemDTO suppliedItemDTO) throws SQLException {
        String sql = "insert into supplierinventorydb.productinagreement (branchid, supplierid, productid, price) values(?,?,?,?)";
        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.setInt(3, Integer.parseInt(suppliedItemDTO.product.productID));
            ps.setInt(4, suppliedItemDTO.suppliedItemPrice);
            ps.executeUpdate();
        }
    }

    public void removeProductFromAgreement(String branchid, String supplierid, String productID) throws SQLException {
        String sql = "delete from supplierinventorydb.productinagreement where branchid = ? and supplierid = ? and productid = ?" ;
        String sql2 = "delete from supplierinventorydb.discount where branchid = ? and supplierid = ? and productid = ?" ;

        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.setInt(3, Integer.parseInt(productID));
            ps.executeUpdate();
            ps = con.prepareStatement(sql2);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.setInt(3, Integer.parseInt(productID));
            ps.executeUpdate();
        }
    }

    public void removeAgreement(String branchid, String supplierid) throws SQLException {
        String sqlAgreement = "delete from supplierinventorydb.agreement where branchid = ? and supplierid = ?";
        String sqlProductInAgreement = "delete from supplierinventorydb.productinagreement where branchid = ? and supplierid = ?";
        String sqlDiscount = "delete from supplierinventorydb.discount where branchid = ? and supplierid = ?";
        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sqlAgreement);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.executeUpdate();
            ps = con.prepareStatement(sqlProductInAgreement);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.executeUpdate();
            ps = con.prepareStatement(sqlDiscount);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ps.executeUpdate();
        }
    }

    public void addDiscountToAgreement(String branchID, String supplierID, DiscountDTO discount) throws SQLException {
        String sql = "insert into supplierinventorydb.discount (branchID, supplierID, productid, discountamount, quantity) values(?,?,?,?,?)";
        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(branchID));
            ps.setInt(2, Integer.parseInt(supplierID));
            ps.setInt(3, Integer.parseInt(discount.suppliedItemid));
            ps.setInt(4, discount.discount);
            ps.setInt(5, discount.quantity);
            ps.executeUpdate();
        }
    }

    public void editDiscountFromAgreement(String branchID, String supplierID, DiscountDTO discount) throws Exception {
        String sqlCheck = "Select * from supplierinventorydb.discount where branchid = ? and supplierid = ? and productid = ?";
        String sql = "UPDATE discount SET quantity = ?, discountamount = ? WHERE branchid = ? AND supplierid = ? AND productid = ?";
        try (Connection con = getConnection()){
            PreparedStatement ps = con.prepareStatement(sqlCheck);
            ps.setInt(1, Integer.parseInt(branchID));
            ps.setInt(2, Integer.parseInt(supplierID));
            ps.setInt(3, Integer.parseInt(discount.suppliedItemid));
            ResultSet rs = ps.executeQuery();
            if (!rs.next()){
                throw new Exception("Discount does not exist");
            }

            ps = con.prepareStatement(sql);
            ps.setInt(1, discount.quantity);
            ps.setInt(2, discount.discount);
            ps.setInt(3, Integer.parseInt(branchID));
            ps.setInt(4, Integer.parseInt(supplierID));
            ps.setInt(5, Integer.parseInt(discount.suppliedItemid));
            ps.executeUpdate();
        }
    }

    public List<AgreementDTO> getAllAgreement() throws SQLException {
        String sql = "select * from supplierinventorydb.agreement";
        List<AgreementDTO> agreements = new ArrayList<>();
        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String branchid = Integer.toString(rs.getInt("branchid"));
                String supplierid = Integer.toString(rs.getInt("supplierid"));
                AgreementDTO agreementDTO = getAgreement(branchid, supplierid);
                agreements.add(agreementDTO);
            }
            return agreements;
        }
    }
    public AgreementDTO getAgreement(String branchId, String supplierId) throws SQLException {
        //get all products in agreement
        String getAllProductInAgreementSql = "Select * from supplierinventorydb.productinagreement where branchid = ? and supplierid = ?";
        String getProductSql = "Select * from supplierinventorydb.product where id = ?";
        String getAllDiscountSql = "Select * from supplierinventorydb.discount where branchid = ? and supplierid = ?";

        List<SuppliedItemDTO> suppliedItemDTOList = new ArrayList<>();
        List<DiscountDTO> discountDTOList = new ArrayList<>();


        try (Connection connection = getConnection()){
            //get all products in agreement
            PreparedStatement ps = connection.prepareStatement(getAllProductInAgreementSql);
            ps.setInt(1, Integer.parseInt(branchId));
            ps.setInt(2, Integer.parseInt(supplierId));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int price = rs.getInt("price");
                int productId = rs.getInt("productid");
                PreparedStatement ps1 = connection.prepareStatement(getProductSql);
                ps1.setInt(1, productId);
                ResultSet rs1 = ps1.executeQuery();
                ProductDTO productDTO = null;
                if(rs1.next()){
                    String productName = rs1.getString("name");
                    String productManfacturer = rs1.getString("manufacturer");
                    int shelfLife = rs1.getInt("shelflifedays");
                    productDTO = new ProductDTO(Integer.toString(productId), productName, productManfacturer, shelfLife);
                }
                SuppliedItemDTO suppliedItemDTO = new SuppliedItemDTO(price, productDTO);
                suppliedItemDTOList.add(suppliedItemDTO);
            }

            //get all discounts in agreement
            PreparedStatement ps2 = connection.prepareStatement(getAllDiscountSql);
            ps2.setInt(1, Integer.parseInt(branchId));
            ps2.setInt(2, Integer.parseInt(supplierId));
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                int productId = rs2.getInt("productid");
                int quantity = rs2.getInt("quantity");
                int discountAmount = rs2.getInt("discountamount");
                DiscountDTO discountDTO = new DiscountDTO(Integer.toString(productId), quantity, discountAmount);
                discountDTOList.add(discountDTO);
            }
        }

        return new AgreementDTO(supplierId, branchId, suppliedItemDTOList, discountDTOList);
    }


    public boolean checkAgreement(String branchid, String supplierid) throws SQLException {
        String sql = "select * from supplierinventorydb.agreement where branchid = ? and supplierid = ?";
        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(branchid));
            ps.setInt(2, Integer.parseInt(supplierid));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
            return false;
        }
    }

    public List<AgreementDTO> getAllAgreementForConstantOrder(String branchID, List<SupplierDTO> suppliersDTOList) throws SQLException {
        List<AgreementDTO> agreements = new ArrayList<>();
        String sql = "select * from supplierinventorydb.agreement where branchid = ? and supplierid = ?";

        try (Connection connection = getConnection()){
            PreparedStatement ps = connection.prepareStatement(sql);
            for (SupplierDTO supplierDTO : suppliersDTOList) {
                ps.setInt(1, Integer.parseInt(branchID));
                ps.setInt(2, Integer.parseInt(supplierDTO.getSupplierID()));  // ← זה חשוב!

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        AgreementDTO agreementDTO = getAgreement(branchID, supplierDTO.getSupplierID());
                        agreements.add(agreementDTO);
                    }
                }
            }
        }
        return agreements;
    }
}