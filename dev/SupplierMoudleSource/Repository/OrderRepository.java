package SupplierMoudleSource.Repository;

import DTO.ConstantOrderDTO;
import DTO.OrderDTO;
import DTO.requirementToConstantOrderDTO;
import SupplierMoudleSource.DAO.OrderDAO;
import SupplierMoudleSource.Domain.ConstantOrder;
import SupplierMoudleSource.Domain.Order;

import java.sql.SQLException;
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


    public void createNewOrder(String supplierID, Order order) throws SQLException {
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

    //returns closed orders by supplier id
    public List<OrderDTO> getOrdersBySupplier(String supplierID) throws SQLException {
        if (supplierID == null){
            throw new NullPointerException();
        }
        if (orderDAO.getOrdersBySupplierID(supplierID).isEmpty()) {
            throw new NullPointerException("Supplier Have No Orders");
        }
        return orderDAO.getOrdersBySupplierID(supplierID);
    }

    public void removeUnclosedOrder(String supplierId, Order order) {
        orders.get(supplierId).remove(order);
        if (orders.get(supplierId).isEmpty()){
            orders.remove(supplierId);
        }
    }

    public void saveOrder(Order order) throws SQLException {
        orderDAO.addOrder(order.getOrderDTO());
    }

    public List<Order> getUnclosedOrdersBySupplier(String supplierID){
        if (supplierID == null){
            throw new NullPointerException("Supplier ID is null");
        }
        List<Order> allOrders = orders.get(supplierID);
        List<Order> unclosedOrders = new ArrayList<>();

        if (allOrders != null) {
            for (Order order : allOrders) {
                if (!order.isOrderClosed()) {
                    unclosedOrders.add(order);
                }
            }
        }

        return unclosedOrders;
    }


    public void closeConstantOrder(ConstantOrder constantOrder) {
        if (constantOrder == null){
            throw new NullPointerException("Constant Order is null");
        }
        ConstantOrderDTO constantOrderDTO = constantOrder.getConstantOrderDTO();
        orderDAO.saveConstantOrder(constantOrderDTO);
    }


    public List<requirementToConstantOrderDTO> getRequirementToConstantOrderDTOByDay(String dayOfWeek) throws SQLException {
        return orderDAO.getRequirementToConstantOrderDTO(dayOfWeek);
    }

    public List<requirementToConstantOrderDTO> getRequirementToConstantOrderDTOById(String name, String manufacturer) throws Exception {
        return orderDAO.getRequirementToConstantOrderDTOById(name, manufacturer);
    }
}
