package SupplierMoudleSource.Service;

import DTO.AgreementDTO;
import DTO.BranchDTO;
import DTO.OrderDTO;
import DTO.SupplierDTO;
import SupplierMoudleSource.Domain.Agreement;
import SupplierMoudleSource.Domain.Branch;
import SupplierMoudleSource.Domain.Supplier;
import SupplierMoudleSource.Repository.AgreementRepository;
import SupplierMoudleSource.Repository.BranchesRepository;
import SupplierMoudleSource.Repository.OrderRepository;
import SupplierMoudleSource.Domain.Order;
import SupplierMoudleSource.Repository.SupplierRepository;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private static OrderRepository orderRepository = OrderRepository.getInstance() ;
    private static AgreementRepository agreementRepository = AgreementRepository.getInstance();
    private static BranchesRepository branchesRepository = BranchesRepository.getInstance();
    private static SupplierRepository supplierRepository = SupplierRepository.getInstance();

    //creates a new order, returns orderId as a string
    public String createOrder(String branchId, String supplierId) throws Exception {
       if(branchId == null || supplierId == null) {
           throw new NullPointerException("branchId and supplierId cannot be null");
       }
        AgreementDTO agreementDTO = agreementRepository.getAgreement(branchId, supplierId);
        if (agreementDTO == null) {
            throw new NullPointerException("Agreement Is Not Found");
        }
        else if (agreementDTO.getSupplierItemsList().isEmpty()){
            throw new NullPointerException("Agreement has no items");
        }
        BranchDTO branchDTO = branchesRepository.getBranch(branchId);
        SupplierDTO supplierDTO = supplierRepository.getSupplier(supplierId);
        Branch branch = new Branch(branchDTO);
        Agreement agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);
        Order newOrder = new Order(agreement, branch);
        orderRepository.addNewOrder(agreement.getSupplierID(), newOrder);
        return newOrder.getOrderID();
    }

    public boolean isOrderEmpty(String supplierID,String OrderID) throws SQLException {
        List<OrderDTO> orders = orderRepository.getOrdersBySupplier(supplierID);
        for (OrderDTO order : orders) {
            if (order.getOrderID().equals(OrderID)) {
                return (order.getTotalPrice() == 0);
            }
        }
        return true;
    }

    //deletes an order if the user decides to cancel order while in the making
    public void deleteConcurrentOrder(String supplierID, String OrderID) throws Exception {
        List<Order> orders = orderRepository.getUnclosedOrdersBySupplier(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(OrderID)) {
                if (!order.isOrderClosed()) {
                    orderRepository.removeUnclosedOrder(supplierID, order);
                    return;
                }
                else {
                    throw new Exception("Order is already closed");
                }
            }
            throw new Exception("Order not found");
        }
    }

    //prints the order for the user to view while making the order
    public void viewConcurrentOrder(String supplierID, String OrderID) throws Exception {
        List<Order> order = orderRepository.getUnclosedOrdersBySupplier(supplierID);
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
        List<Order> orders = orderRepository.getUnclosedOrdersBySupplier(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(orderID)) {
                order.addItemToOrder(productId, quantity);
            }
        }
    }

    //function that receives supplier id and prints all the past closed orders
    public void viewPastOrders(String supplierId) throws Exception {
        if (supplierId == null) {
            throw new NullPointerException("Supplier ID is null");
        }
        List<OrderDTO> ordersDTO = orderRepository.getOrdersBySupplier(supplierId);
        for (OrderDTO orderDTO : ordersDTO) {
            Order order = new Order(orderDTO);
            order.displayOrder();
        }
    }

    //method that finishes an orders (its point is to check if the order is empty, if it is throw an exception)
    public void finishOrder(String supplierID, String orderID) throws Exception {
        if (orderID == null || supplierID == null) {
            throw new NullPointerException("Order ID or Supplier ID is null");
        }
        for (OrderDTO orderDTO : orderRepository.getOrdersBySupplier(supplierID)) {
            if (orderDTO.getOrderID().equals(orderID)) {
                Order order = new Order(orderDTO);
                order.closeOrder();
            }
        }
    }

    //getters of branch id and supplier id given an order id
    public String getBranchId(String supplierID, String orderId) throws SQLException {
        if (supplierID == null || supplierID.isEmpty() || orderId == null || orderId.isEmpty()) {
            throw new NullPointerException("Supplier ID and Order ID is null");
        }
        List<OrderDTO> ordersDTO = orderRepository.getOrdersBySupplier(supplierID);
        for (OrderDTO orderDTO : ordersDTO) {
            if (orderDTO.getOrderID().equals(orderId)) {
                return orderDTO.getBranchID();
            }
        }
        return null;
    }

    public void getsPossibleConstantOrdersForBranch(String branchID) throws Exception {
        if (branchID == null || branchID.isEmpty()) {
            throw new NullPointerException("Branch ID is null");
        }
        List<SupplierDTO> supplierDTOS = supplierRepository.getAllConstantDeliverySuppliers();

    }
}
