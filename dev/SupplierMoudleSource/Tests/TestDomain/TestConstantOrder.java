package SupplierMoudleSource.Tests.TestDomain;

import MainService.SupplierInventoryService;
import SupplierMoudleSource.DTO.ConstantOrderDTO;
import SupplierMoudleSource.DataBase.LoadData.LoadData;
import SupplierMoudleSource.Domain.ConstantOrder;
import SupplierMoudleSource.Service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static SupplierMoudleSource.DataBase.DatabaseInitializer.createSupplierTables;
import static SupplierMoudleSource.DataBase.DatabaseInitializer.dropAllSupplierTable;
import static SupplierMoudleSource.DataBase.PostgresConnection.getConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestConstantOrder {
    private static SupplierInventoryService supplierInventoryService;
    private static OrderService orderService;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        supplierInventoryService = new SupplierInventoryService();
        orderService = new OrderService();
        dropAllSupplierTable();
        createSupplierTables();
        LoadData loadData = new LoadData();
        try {
            loadData.LoadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * that test creating order for tomorrow and shipped the order!
     */
    @Test
    void testConstantOrder() throws Exception {
        DayOfWeek dayOfWeek = LocalDate.now().plusDays(1).getDayOfWeek();
        String day = dayOfWeek.name();
        String formatDayTomorrow = day.substring(0, 1) + day.substring(1).toLowerCase();

        ConstantOrderDTO constantOrderDTO = supplierInventoryService.createRequirementToConstantOrder("1","6", formatDayTomorrow);
        constantOrderDTO = supplierInventoryService.addProductToOrder(constantOrderDTO, "16", 5);
        constantOrderDTO = supplierInventoryService.addProductToOrder(constantOrderDTO, "13", 1);
        supplierInventoryService.finishOrder(constantOrderDTO);
        orderService.scheduleDailyOrderCheck();

        try{
            String sql = "SELECT * FROM supplierinventorydb.constantorders where branchid=? and supplierid=?";
            try (Connection connection = getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, 1); //branch id = 1
                preparedStatement.setInt(2, 6);  // supplier id = 3
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    assertTrue(false, "constant order is not found"); //fail the test if not found
                }
            }

            String sql2 = "SELECT * FROM supplierinventorydb.productsinorder where supplieditemid=? and quantity=?";
            try (Connection connection = getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql2);
                preparedStatement.setInt(1, 16); // productID
                preparedStatement.setInt(2, 5); // quantity
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    assertTrue(false, "product in order is not found");
                }

                int orderId = rs.getInt("orderid");
                String sql3 = "SELECT * FROM supplierinventorydb.order where id=?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql3);
                preparedStatement2.setInt(1,orderId);
                ResultSet rs2 = preparedStatement2.executeQuery();
                if(!rs2.next()) { //order id issue
                    assertTrue(false, "orderID not exist: " + orderId);
                }
                int branchid = rs2.getInt("branchid");
                Date date = rs2.getDate("date");
                int supplierid = rs2.getInt("supplierid");
                assertEquals(1, branchid, "correct branch id");
                assertEquals(6, supplierid, "correct supplier id");
                LocalDate expectedDate = LocalDate.now();  // today's date
                LocalDate actualDate = date.toLocalDate();
                assertEquals(expectedDate, actualDate);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
