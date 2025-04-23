package Service;

import DataBase.BranchesDataBase;
import DataBase.OrderDataBase;
import DataBase.SuppliersDataBase;
import Domain.Agreement;
import Domain.Branch;
import Domain.Order;

import java.util.List;

public class OrderService {
    private static OrderDataBase orderDataBase = OrderDataBase.getInstance() ;
    private static SuppliersDataBase suppliersDataBase = SuppliersDataBase.getInstance();
    private static BranchesDataBase branchesDataBase = BranchesDataBase.getInstance();
    //creates a new order, returns orderId as a string
    public String createOrder(String branchId, String supplierId) {
        try {
            Branch branch = branchesDataBase.getBranch(branchId);
            Agreement agreement = suppliersDataBase.getAgreement(branch.getBranchID(), supplierId);
            if (agreement == null) {
                throw new Exception("Agreement not found");
            }
            Order newOrder = new Order(agreement, branch);
            orderDataBase.addOrder(agreement.getSupplierID(), newOrder);
            return newOrder.getOrderID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isOrderEmpty(String supplierID,String OrderID) {
        List<Order> orders = orderDataBase.getOrdersBySupplier(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(OrderID)) {
                return (order.getTotalPrice() == 0);
            }
        }
        return true;
    }

    //deletes an order if the user decides to cancel order while in the making
    public void deleteOrder(String supplierID, String OrderID) {
        List<Order> orders = orderDataBase.getOrdersBySupplier(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(OrderID)) {
                if (!order.isOrderClosed()) {
                    orderDataBase.removeOrder(supplierID, order);
                }
                else{
                    System.out.println("Order is already closed");
                }
            }
        }
    }

    //prints the order for the user to view while making the order
    public void viewOrder(String supplierID, String OrderID) throws Exception {
        List<Order> order = orderDataBase.getOrdersBySupplier(supplierID);
        for (Order o : order) {
            if (o.getOrderID().equals(OrderID)) {
                o.displayOrder();
                return;
            }
        }
        throw new Exception("Order does not exist");

    }

    //adds a product to the order, use doesProductExistsInAgreementFunc in AgreementService
    public void addProductToOrder(String supplierID, String orderID, String productId, int quantity) throws Exception {
        List<Order> orders = orderDataBase.getOrdersBySupplier(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(orderID)) {
                order.addItemToOrder(productId, quantity);
            }
        }

    }

    //function that receives supplier id and prints all the past orders
    public void viewPastOrders(String supplierId){
        if (supplierId == null) {
            throw new NullPointerException("Supplier ID is null");
        }
        List<Order> orders = orderDataBase.getOrdersBySupplier(supplierId);
        for (Order order : orders) {
            order.displayOrder();
        }
    }

    //method that finishes an orders (its point is to check if the order is empty, if it is throw an exception)
    public void finishOrder(String supplierID, String orderID) {
        if (orderID == null || supplierID == null) {
            throw new NullPointerException("Order ID is null");
        }
        for (Order order : orderDataBase.getOrdersBySupplier(supplierID)) {
            if (order.getOrderID().equals(orderID)) {
                order.closeOrder();
            }
        }
    }

    //getters of branch id and supplier id given an order id
    public String getBranchId(String supplierID, String orderId){
        if (supplierID == null || supplierID.isEmpty() || orderId == null || orderId.isEmpty()) {
            throw new NullPointerException("Supplier ID and Order ID is null");
        }
        List<Order> orders = orderDataBase.getOrdersBySupplier(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(orderId)) {
                return order.getBranch().getBranchID();
            }
        }
        return null;
    }


}
