package DataBase;

import Domain.Order;
import Domain.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDataBase {
    private Map<String, List<Order>> orders;

    //singleton database
    private static OrderDataBase orderDataBase = null;
    public static OrderDataBase getInstance() {
        if (orderDataBase == null) {
            orderDataBase = new OrderDataBase();
        }
        return orderDataBase;
    }
    private OrderDataBase(){
        orders = new HashMap<>();
    }


    public void addOrder(String supplierID, Order order) {
        if (supplierID == null || order == null){
            return;
        }
        if (!orders.containsKey(supplierID)) {
            List<Order> orderList = new ArrayList<>();
            orderList.add(order);
            orders.put(supplierID, orderList);
        }
        else {
            List<Order> orderList = orders.get(supplierID);
            orderList.add(order);
            orders.put(supplierID, orderList);
        }
    }

    public List<Order> getOrdersBySupplier(String supplierID) {
        if (supplierID == null){
            throw new NullPointerException();
        }
        if (!orders.containsKey(supplierID)) {
            throw new NullPointerException("Supplier Have No Orders");
        }
        List<Order> copyOrders = orders.get(supplierID);
        return copyOrders;
    }

    public void removeOrder(String supplierId, Order order) {
        orders.get(supplierId).remove(order);
        if (orders.get(supplierId).isEmpty()){
            orders.remove(supplierId);
        }
    }
}
