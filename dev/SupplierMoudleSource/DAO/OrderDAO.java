package SupplierMoudleSource.DAO;

import SupplierMoudleSource.DTO.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static SupplierMoudleSource.DataBase.PostgresConnection.getConnection;

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

            pstmt.setInt(1, Integer.parseInt(orderID));
            pstmt.setDate(2, new java.sql.Date(orderDate.getTime()));
            pstmt.setInt(3, totalPrice);
            pstmt.setInt(4, Integer.parseInt(branchID));
            pstmt.setInt(5, Integer.parseInt(supplierID));
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
                pstmt2.setInt(2, Integer.parseInt(orderID));
                pstmt2.setInt(3, Integer.parseInt(suppliedItem.product.productID));
                pstmt2.executeUpdate();
            }
        }
    }

    public OrderDTO getOrder(String orderID) throws SQLException {
        Date oDate = null;
        int oTotalPrice = 0;
        String oBranchID = "";
        String oSupplierID = "";
        int pID = -1;
        String pName = "";
        String pManufacturer = "";
        int shelfLifeDays = 0;
        int siPrice = 0;
        OrderDTO oDTO = null;

        String sql = "SELECT * FROM supplierinventorydb.order WHERE id = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, orderID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                oDate = rs.getDate("date");
                oTotalPrice = rs.getInt("totalPrice");
                oBranchID = Integer.toString(rs.getInt("branchID"));
                oSupplierID = Integer.toString(rs.getInt("supplierID"));
            }

            String sql2 = "SELECT * FROM supplierinventorydb.productsInOrder WHERE id = ?";

            Map<SuppliedItemDTO, Integer> suppliedItems = new HashMap<>();

            try (Connection con2 = getConnection();
                 PreparedStatement pstmt2 = con2.prepareStatement(sql2)) {

                pstmt2.setInt(1, Integer.parseInt(orderID));
                ResultSet rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    Integer quantity = rs2.getInt("quantity");
                    String suppliedItemID = Integer.toString(rs2.getInt("suppliedItemID"));

                    String sql3 = "SELECT * FROM supplierinventorydb.product WHERE id = ?";
                    try (Connection con3 = getConnection();
                         PreparedStatement pstmt3 = con3.prepareStatement(sql3)) {

                        pstmt3.setInt(1, Integer.parseInt(suppliedItemID));
                        ResultSet rs3 = pstmt3.executeQuery();


                        if (rs3.next()) {
                            pID = rs3.getInt("id");
                            pName = rs3.getString("name");
                            pManufacturer = rs3.getString("manufacturer");
                            shelfLifeDays = rs3.getInt("shelfLifeDays");
                        }
                        ProductDTO pDTO = new ProductDTO(Integer.toString(pID), pName, pManufacturer, shelfLifeDays);


                        String sql4 = "SELECT * FROM supplierinventorydb.productinagreement WHERE productid = ? AND branchid = ? AND supplierID = ?";

                        try (Connection con4 = getConnection();
                             PreparedStatement pstmt4 = con.prepareStatement(sql4)) {

                            pstmt4.setInt(1, pID);
                            pstmt4.setInt(2, Integer.parseInt(oBranchID));
                            pstmt4.setInt(3,  Integer.parseInt(oSupplierID));

                            ResultSet rs4 = pstmt4.executeQuery();
                            if (rs4.next()) {
                                siPrice = rs4.getInt("price");
                            }
                            SuppliedItemDTO supItemDTO = new SuppliedItemDTO(siPrice, pDTO);
                            suppliedItems.put(supItemDTO, quantity);
                        }
                    }
                }
            }
            oDTO = new OrderDTO(orderID, oDate, oTotalPrice, suppliedItems, oBranchID, oSupplierID);
        }
        return oDTO;
    }

    public List<OrderDTO> getOrdersBySupplierID(String supplierID) throws SQLException {
        if (supplierID == null || supplierID.isEmpty()) {
            throw new SQLException("Supplier ID is null or empty");
        }
        List<OrderDTO> ordersBySupplierDTOList = new ArrayList<>();
        String sql = "SELECT * FROM supplierinventorydb.order WHERE supplierid = ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(supplierID));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderID = rs.getInt("id");
                Date oDate = rs.getDate("date");
                int oTotalPrice = rs.getInt("totalPrice");
                int oBranchID = rs.getInt("branchID");

                Map<SuppliedItemDTO, Integer> suppliedItems = new HashMap<>();

                String sql2 = "SELECT * FROM supplierinventorydb.productsinorder WHERE orderid = ?";
                try (Connection con2 = getConnection();
                     PreparedStatement pstmt2 = con2.prepareStatement(sql2)) {
                    pstmt2.setInt(1, orderID);
                    ResultSet rs2 = pstmt2.executeQuery();

                    while (rs2.next()) {
                        int quantity = rs2.getInt("quantity");
                        int suppliedItemID = rs2.getInt("suppliedItemID");

                        String sql3 = "SELECT * FROM supplierinventorydb.product WHERE id = ?";
                        try (Connection con3 = getConnection();
                             PreparedStatement pstmt3 = con3.prepareStatement(sql3)) {
                            pstmt3.setInt(1, suppliedItemID);
                            ResultSet rs3 = pstmt3.executeQuery();

                            if (rs3.next()) {
                                int id = rs3.getInt("id");
                                String name = rs3.getString("name");
                                String manufacturer = rs3.getString("manufacturer");
                                int shelfLife = rs3.getInt("shelfLifeDays");

                                ProductDTO productDTO = new ProductDTO(Integer.toString(id), name, manufacturer, shelfLife);

                                int price = 0;
                                String sql4 = "SELECT price FROM supplierinventorydb.productinagreement WHERE productid = ? AND supplierid = ? AND branchid = ?";

                                try (Connection con4 = getConnection();
                                     PreparedStatement pstmt4 = con4.prepareStatement(sql4)) {

                                    pstmt4.setInt(1, id);
                                    pstmt4.setInt(2, Integer.parseInt(supplierID));
                                    pstmt4.setInt(3, oBranchID);

                                    ResultSet rs4 = pstmt4.executeQuery();
                                    if (rs4.next()) {
                                        price = rs4.getInt("price");
                                    }
                                }
                                SuppliedItemDTO suppliedItem = new SuppliedItemDTO(price, productDTO);
                                suppliedItems.put(suppliedItem, quantity);
                            }
                        }
                    }
                }
                OrderDTO orderDTO = new OrderDTO(Integer.toString(orderID), oDate, oTotalPrice, suppliedItems,
                        Integer.toString(oBranchID), supplierID);
                ordersBySupplierDTOList.add(orderDTO);
            }
        }
        return ordersBySupplierDTOList;
    }

    public void saveConstantOrder(ConstantOrderDTO constantOrderDTO) {
        if (constantOrderDTO == null) {
            throw new NullPointerException("Constant Order is null");
        }
        String sql = "INSERT INTO supplierinventorydb.constantorders (branchid , supplierid, supplieditemid, quantity, dayofweek) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String branchId = constantOrderDTO.getBranchID();
            String supplierId = constantOrderDTO.getSupplierID();
            String dayOfWeek = constantOrderDTO.getDayOfWeek();

            for (Map.Entry<SuppliedItemDTO, Integer> entry : constantOrderDTO.getSuppliedItems().entrySet()) {
                SuppliedItemDTO item = entry.getKey();
                Integer quantity = entry.getValue();

                if (item == null || item.product == null || item.product.productID == null || quantity == null) {
                    throw new IllegalArgumentException("Invalid item or quantity in constant order");
                }
                ps.setInt(1, Integer.parseInt(branchId));
                ps.setInt(2, Integer.parseInt(supplierId));
                ps.setInt(3, Integer.parseInt(item.product.productID));  // supplieditemid = product id
                ps.setInt(4, quantity);
                ps.setString(5, dayOfWeek);
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save constant order: " + e.getMessage(), e);
        }
    }

    public List<requirementToConstantOrderDTO> getRequirementToConstantOrderDTO(String dayOfWeek) throws SQLException {
        String sql = "SELECT * FROM supplierinventorydb.constantorders WHERE dayofweek = ? ORDER BY supplierid, branchid";
        String getSql = "SELECT * FROM supplierinventorydb.productinagreement WHERE branchid = ? AND supplierid = ? AND suppliediteid = ?";
        String getProductSql = "SELECT * FROM supplierinventorydb.product WHERE id = ?";
        List<requirementToConstantOrderDTO> orderDTOList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, dayOfWeek);
            ResultSet rs = pstmt.executeQuery();

            int prevBranchId = -1;
            int prevSupplierId = -1;
            Map<SuppliedItemDTO, Integer> suppliedItems = new HashMap<>();

            while (rs.next()) {
                int branchId = rs.getInt("branchid");
                int supplierId = rs.getInt("supplierid");
                int quantity = rs.getInt("quantity");
                int suppliedItemId = rs.getInt("supplieditemid");

                // If we moved to a new group
                if ((branchId != prevBranchId || supplierId != prevSupplierId) && !suppliedItems.isEmpty()) {
                    orderDTOList.add(new requirementToConstantOrderDTO(Integer.toString(prevBranchId),
                            Integer.toString(prevSupplierId), suppliedItems, dayOfWeek));
                    suppliedItems = new HashMap<>();
                }

                // Get price from productInAgreement
                PreparedStatement statement = connection.prepareStatement(getSql);
                statement.setInt(1, branchId);
                statement.setInt(2, supplierId);
                statement.setInt(3, suppliedItemId);
                ResultSet rs1 = statement.executeQuery();

                int price = 0;
                if (rs1.next()) {
                    price = rs1.getInt("price");
                }

                // Get product info
                statement = connection.prepareStatement(getProductSql);
                statement.setInt(1, suppliedItemId);
                ResultSet rs2 = statement.executeQuery();

                String productName = "";
                String productManufacturer = "";
                int shelfLife = 0;

                if (rs2.next()) {
                    productName = rs2.getString("productName");
                    productManufacturer = rs2.getString("manufacturer");
                    shelfLife = rs2.getInt("shelflifedays");
                }

                ProductDTO product = new ProductDTO(Integer.toString(suppliedItemId), productName, productManufacturer, shelfLife);
                SuppliedItemDTO suppliedItem = new SuppliedItemDTO(price, product);
                suppliedItems.put(suppliedItem, quantity);

                prevBranchId = branchId;
                prevSupplierId = supplierId;
            }

            // Add the last group
            if (!suppliedItems.isEmpty()) {
                orderDTOList.add(new requirementToConstantOrderDTO(Integer.toString(prevBranchId),
                        Integer.toString(prevSupplierId), suppliedItems, dayOfWeek));
            }

        }

        return orderDTOList;
    }

    public List<requirementToConstantOrderDTO> getRequirementToConstantOrderDTOById(String productName, String manufacturer) throws Exception {
        String getProductSql = "Select * from supplierinventorydb.product where name = ? and manufacturer = ?";
        String getFromSuppliedItem = "Select * from supplierinventorydb.productinagreement where productid = ?";
        String getConstantOrderSql = "Select * from supplierinventorydb.constantorders where supplieditemid = ?";
        List<requirementToConstantOrderDTO> orderDTOList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(getProductSql);
            pstmt.setString(1, productName);
            pstmt.setString(2, manufacturer);
            ResultSet rs = pstmt.executeQuery();
            ProductDTO productDTO;
            String productId;

            if (rs.next()) {
                productId = Integer.toString(rs.getInt(1));
                productDTO = new ProductDTO(productId, productName, manufacturer, rs.getInt("shelflifedays"));
            } else { // case when no products are in constant order
                throw new Exception("Product doesnt exist");
            }

            pstmt = connection.prepareStatement(getFromSuppliedItem);
            pstmt.setInt(1, Integer.parseInt(productId));
            ResultSet rs2 = pstmt.executeQuery();

            while (rs2.next()) { //get all products that are in a constant order
                int branchid = rs2.getInt("branchid");
                int supplierid = rs2.getInt("supplierid");
                int price = rs2.getInt("price");

                SuppliedItemDTO suppliedItem = new SuppliedItemDTO(price, productDTO);
                Map<SuppliedItemDTO, Integer> suppliedItems = new HashMap<>();

                PreparedStatement pstmt2 = connection.prepareStatement(getConstantOrderSql);
                pstmt2.setInt(1, Integer.parseInt(productId));
                ResultSet rs3 = pstmt2.executeQuery();
                while (rs3.next()) {
                    int quantity = rs3.getInt("quantity");
                    suppliedItems.put(suppliedItem, quantity);
                    String dayofWeek = rs3.getString("dayofweek");
                    orderDTOList.add(new requirementToConstantOrderDTO(Integer.toString(branchid), Integer.toString(supplierid),
                            suppliedItems, dayofWeek));
                }
            }
        }

        return orderDTOList;
    }

    public void updateExistingConstantOrder(String branchId, String supplierId, String productName, String manufacturer, int newQuantity) throws Exception
    {
        String getProductIdSql = "Select * from supplierinventorydb.product where name = ? and manufacturer = ?";
        String updateNewQuantitySql = "Update supplierinventorydb.constantorders set quantity = ? where productid = ?";
        try (Connection connection = getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(getProductIdSql);
            pstmt.setString(1, productName);
            pstmt.setString(2, manufacturer);
            ResultSet rs = pstmt.executeQuery();
            int productId;
            if (rs.next()) {
                productId = rs.getInt("id");
            } else {
                throw new Exception("Product does not exist");
            }
            pstmt = connection.prepareStatement(updateNewQuantitySql);
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        }
    }
}
