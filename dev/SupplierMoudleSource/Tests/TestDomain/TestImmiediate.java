package SupplierMoudleSource.Tests.TestDomain;
import SupplierMoudleSource.DataBase.LoadData.LoadData;
import SupplierMoudleSource.Service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static SupplierMoudleSource.DataBase.DatabaseInitializer.createSupplierTables;
import static SupplierMoudleSource.DataBase.DatabaseInitializer.dropAllSupplierTable;
import static SupplierMoudleSource.DataBase.PostgresConnection.getConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestImmiediate
{
    private static OrderService orderService;
    @BeforeAll
    static void before()  {
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

    @Test
    public void addImmidiateOrder(){
        try {
            orderService.createImmediateOrder("3", "Bamba", "Osem", 15);
            String sql = "SELECT * FROM supplierinventorydb.productsinorder where supplieditemid=? and quantity=?";
            try (Connection connection = getConnection()){
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,1); //set baba osem id
                preparedStatement.setInt(2,15); //select correct quantity
                ResultSet rs = preparedStatement.executeQuery();
                if(!rs.next()){
                    assertTrue(false, "order not found"); //fail the test if not found
                }

                int orderId = rs.getInt("orderid");
                String sql2 = "SELECT * FROM supplierinventorydb.order where id=?";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setInt(1,orderId);
                ResultSet rs2 = preparedStatement2.executeQuery();
                if(!rs2.next()){ //order id issue
                    assertTrue(false, "bad orderid, " + orderId);
                }
                int branchid = rs2.getInt("branchid");
                Date date = rs2.getDate("date");
                int supplierid = rs2.getInt("supplierid");
                assertEquals(3, branchid);
                assertEquals(3, supplierid, "cheapest supplier");
                LocalDate expectedDate = LocalDate.now();  // today's date
                LocalDate actualDate = date.toLocalDate();
                assertEquals(expectedDate, actualDate);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
