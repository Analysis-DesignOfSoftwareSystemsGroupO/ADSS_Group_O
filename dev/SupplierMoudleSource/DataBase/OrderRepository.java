package SupplierMoudleSource.DataBase;

import SupplierMoudleSource.DAO.OrderDAO;
import SupplierMoudleSource.Domain.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private Map<String, List<Order>> orders;
    private OrderDAO orderDAO;

    //singleton database
    private static OrderRepository orderRepository = null;
    public static OrderRepository getInstance() {
        if (orderRepository == null) {
            orderRepository = new OrderRepository();
        }
        return orderRepository;
    }

    private OrderRepository(){
        orders = new HashMap<>();
        orderDAO = new OrderDAO();
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
        orderDAO.addOrder();
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
