package SupplierMoudleSource.Service;

import SupplierMoudleSource.DTO.*;
import SupplierMoudleSource.Domain.Agreement;
import SupplierMoudleSource.Domain.Branch;
import SupplierMoudleSource.Domain.ConstantOrder;
import SupplierMoudleSource.Domain.Order;
import SupplierMoudleSource.Repository.*;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderService {
    private static OrderRepository orderRepository = OrderRepository.getInstance() ;
    private static AgreementRepository agreementRepository = AgreementRepository.getInstance();
    private static BranchesRepository branchesRepository = BranchesRepository.getInstance();
    private static SupplierRepository supplierRepository = SupplierRepository.getInstance();
    private static ProductRepository productRepository = ProductRepository.getInstance();

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
        orderRepository.createNewOrder(agreement.getSupplierID(), newOrder);
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
        if (ordersDTO.isEmpty()) {
            throw new Exception("Supplier does not have orders");
        }
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
        for (Order order : orderRepository.getUnclosedOrdersBySupplier(supplierID)) {
            if (order.getOrderID().equals(orderID)) {
                order.closeOrder();
                try {
                    orderRepository.saveOrder(order);
                    return;
                } catch (Exception e) {
                    throw new Exception("Order not found");
                }
            }
        }
    }

        public void createImmediateOrder(String branchID, String productName, String manufacturer, int quantity) throws Exception {
            String productID = productRepository.getProduct(productName, manufacturer).getProductID();
            List<Agreement> agreement = castAgreementDTOStoAgreement(agreementRepository.getAllAgreement()); //get all agreements
            Agreement bestAgreement = null;
            int minPrice = -1;
            for (Agreement a : agreement) {
                if (a.getBranchID().equals(branchID)) {
                    int curPrice = a.getPriceForProduct(productID, quantity);
                    if (curPrice != -1 && curPrice < minPrice ) {
                        minPrice = curPrice;
                        bestAgreement = a;
                    }
                }
            }
            if (minPrice == -1) {
                throw new Exception("No immediate order found");
            }
            Order order = new Order(bestAgreement, new Branch(branchesRepository.getBranch(branchID)));
            order.addItemToOrder(productID, quantity);
            order.closeOrder();
            orderRepository.saveOrder(order);
        }



    private List <Agreement> castAgreementDTOStoAgreement(List<AgreementDTO> agreementDTOS) throws Exception {
        List<Agreement> agreements = new ArrayList<Agreement>();
        for (AgreementDTO agreementDTO : agreementDTOS) {
            agreements.add(castAgreementDTOtoAgreement(agreementDTO));
        }
        return agreements;
    }

    private Agreement castAgreementDTOtoAgreement(AgreementDTO agreementDTO) throws Exception {
        BranchDTO branchDTO = branchesRepository.getBranch(agreementDTO.getBranchId());
        SupplierDTO supplierDTO = supplierRepository.getSupplier(agreementDTO.getSupplierID());
        return new Agreement(branchDTO, supplierDTO, agreementDTO );
    }

    public void scheduleDailyOrderCheck() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            LocalDate today = LocalDate.now();
            DayOfWeek dayOfWeek = today.getDayOfWeek();
            try {
                createAllOrdersForToday(dayOfWeek.toString().substring(0, 3)); // your real logic
            } catch (Exception e) {
                throw new RuntimeException("Error while creating all constant orders ", e);
            }
        };

        long delay = getDelayUntilMidnightInMillis(); //todo change for local class time
        long period = TimeUnit.DAYS.toMillis(1); // 24 hours

        scheduler.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    }
    private static long getDelayUntilMidnightInMillis() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Duration duration = Duration.between(now, nextMidnight);
        return duration.toMillis();
    }

    private void createAllOrdersForToday(String dayOfWeek) throws Exception {
        List<requirementToConstantOrderDTO> requirementToConstantOrderDTOS = orderRepository.getRequirementToConstantOrderDTOByDay(dayOfWeek);
        for (requirementToConstantOrderDTO requirementDTO : requirementToConstantOrderDTOS) {
            Agreement agreement = castAgreementDTOtoAgreement(agreementRepository.getAgreement(requirementDTO.getBranchID(),
                                                                requirementDTO.getSupplierID()));

            Branch branch = new Branch(branchesRepository.getBranch(requirementDTO.getBranchID()));
            Order order = new Order(agreement, branch);
            for (SuppliedItemDTO suppliedItemDTO : requirementDTO.getSuppliedItems().keySet()){
                order.addItemToOrder(suppliedItemDTO.product.productID, requirementDTO.getSuppliedItems().get(suppliedItemDTO));
            }
            order.closeOrder();
            orderRepository.saveOrder(order);
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
        List<AgreementDTO> agreementDTOS = agreementRepository.getAllAgreementForConstantOrder(branchID, supplierDTOS);
        for (AgreementDTO agreementDTO : agreementDTOS) {
            BranchDTO branchDTO = branchesRepository.getBranch(agreementDTO.getBranchId());
            SupplierDTO supplierDTO = supplierRepository.getSupplier(agreementDTO.getSupplierID());
            if (branchDTO == null){
                throw new Exception("Branch does not exist");
            }
            if (supplierDTO == null){
                throw new Exception("Supplier does not exist");
            }
            Agreement agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);
            System.out.println(agreement);
        }
    }

    public ConstantOrderDTO createRequirementToConstantOrder(String branchID, String supplierID, String day) throws Exception {
        if (branchID == null || branchID.isEmpty()) {
            throw new NullPointerException("Branch ID is null");
        }
        if (supplierID == null || supplierID.isEmpty()) {
            throw new NullPointerException("Supplier ID is null");
        }
        AgreementDTO agreementDTO = agreementRepository.getAgreement(branchID, supplierID);
        if (agreementDTO == null) {
            throw new NullPointerException("Agreement Is Not Found");
        }
        else if (agreementDTO.getSupplierItemsList().isEmpty()){
            throw new NullPointerException("Agreement has no items");
        }
        SupplierDTO supplierDTO = supplierRepository.getSupplier(agreementDTO.getSupplierID());
        BranchDTO branchDTO = branchesRepository.getBranch(agreementDTO.getBranchId());
        Agreement agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);
        ConstantOrder newConstantOrder = new ConstantOrder(agreement, day);
        return newConstantOrder.getConstantOrderDTO();
    }

    //adds a product to the constant order, use doesProductExistsInAgreementFunc in AgreementService
    public void addProductToConstantOrder(ConstantOrderDTO constantOrderDTO, String productId, int quantity) throws Exception {
       ConstantOrder constantOrder = new ConstantOrder(constantOrderDTO);
        constantOrder.addItemToOrder(productId, quantity);
    }

    public void closeConstantOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        ConstantOrder constantOrder = new ConstantOrder(constantOrderDTO);
        constantOrder.closeConstantOrder();
    }

    public void displayConstantOrder(ConstantOrderDTO constantOrderDTO) throws Exception {
        ConstantOrder constantOrder = new ConstantOrder(constantOrderDTO);
        System.out.println(constantOrder);
    }
}
