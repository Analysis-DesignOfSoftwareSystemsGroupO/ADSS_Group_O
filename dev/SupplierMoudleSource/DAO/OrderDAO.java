package SupplierMoudleSource.DAO;

import DTO.OrderDTO;
import DTO.ProductDTO;
import DTO.SuppliedItemDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static DataBase.PostgresConnection.getConnection;

public class OrderDAO {

    public void addOrder(OrderDTO order) throws SQLException {
        String sql = "INSERT INTO supplierinventorydb.order (id, date, totalPrice, branchID, supplierID) VALUES (?, ?, ?, ?, ?)";

        String orderID = order.getOrderID();
        Date orderDate = order.getOrderDate();
        int totalPrice = order.getTotalPrice();
        String branchID = order.getBranchID();
        String supplierID = order.getSupplierID();

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, orderID);
            pstmt.setDate(2, (java.sql.Date) orderDate);
            pstmt.setInt(3, totalPrice);
            pstmt.setString(4, branchID);
            pstmt.setString(5, supplierID);
            pstmt.executeUpdate();
        }
        String sql2 = "INSERT INTO supplierinventorydb.productsinorder (quantity, orderID, suppliedItemID) VALUES (?, ?, ?)";
        Map<SuppliedItemDTO, Integer> suppliedItems = order.getSuppliedItems();

        try (Connection con = getConnection();
        PreparedStatement pstmt2 = con.prepareStatement(sql2)) {
            for (Map.Entry<SuppliedItemDTO, Integer> entry : suppliedItems.entrySet()) {
                SuppliedItemDTO suppliedItem = entry.getKey();
                Integer quantity = entry.getValue();
                pstmt2.setInt(1, quantity);
                pstmt2.setString(2, orderID);
                pstmt2.setString(3, suppliedItem.suppliedItemID);
                pstmt2.executeUpdate();
            }
        }
    }

    public OrderDTO getOrder(String orderID) throws SQLException {
        Date oDate = null;
        int oTotalPrice = 0;
        String oBranchID = "";
        String oSupplierID = "";
        String pID = "";
        String pName = "";
        String pManufacturer = "";
        int shelfLifeDays = 0;
        int siPrice = 0;
        OrderDTO oDTO = null;

        String sql = "SELECT * FROM order WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, orderID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                oDate = rs.getDate("date");
                oTotalPrice = rs.getInt("totalPrice");
                oBranchID = rs.getString("branchID");
                oSupplierID = rs.getString("supplierID");
            }

            String sql2 = "SELECT * FROM productsInOrder WHERE id = ?";

            Map<SuppliedItemDTO, Integer> suppliedItems = new HashMap<>();

            try (Connection con2 = getConnection();
                 PreparedStatement pstmt2 = con2.prepareStatement(sql2)) {

                pstmt2.setString(1, orderID);
                ResultSet rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    Integer quantity = rs2.getInt("quantity");
                    String suppliedItemID = rs2.getString("suppliedItemID");

                    String sql3 = "SELECT * FROM product WHERE id = ?";
                    try (Connection con3 = getConnection();
                         PreparedStatement pstmt3 = con3.prepareStatement(sql3)) {

                        pstmt3.setString(1, suppliedItemID);
                        ResultSet rs3 = pstmt3.executeQuery();


                        if (rs3.next()) {
                            pID = rs3.getString("id");
                            pName = rs3.getString("name");
                            pManufacturer = rs3.getString("manufacturer");
                            shelfLifeDays = rs3.getInt("shelfLifeDays");
                        }
                        ProductDTO pDTO = new ProductDTO(pID, pName, pManufacturer, shelfLifeDays);


                        String sql4 = "SELECT * FROM suppliedItem WHERE productid = ? AND branchid = ? AND supplierID = ?";

                        try (Connection con4 = getConnection();
                             PreparedStatement pstmt4 = con.prepareStatement(sql4)) {

                            pstmt4.setString(1, pID);
                            pstmt4.setString(2, oBranchID);
                            pstmt4.setString(3, oSupplierID);

                            ResultSet rs4 = pstmt4.executeQuery();
                            if (rs4.next()) {
                                siPrice = rs4.getInt("price");
                            }
                            SuppliedItemDTO supItemDTO = new SuppliedItemDTO(siPrice, pDTO, suppliedItemID);
                            suppliedItems.put(supItemDTO, quantity);
                        }
                    }
                }
            }
            oDTO = new OrderDTO(orderID, oDate, oTotalPrice, suppliedItems, oBranchID, oSupplierID);
        }
        return oDTO;
    }

}
