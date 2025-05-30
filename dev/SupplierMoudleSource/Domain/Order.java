package SupplierMoudleSource.Domain;

import DTO.*;
import SupplierMoudleSource.Repository.*;

import java.util.*;

public class Order {
    private static int idCounter = 0;
    private final String orderID;
    private final Date orderDate;
    private int totalPrice;
    private final Agreement agreement;
    private Map<SuppliedItem, Integer> suppliedItems;
    private Boolean orderClosed = false;
    private final Branch branch;

    public Order(Agreement agreement, Branch branch) {
        if (agreement == null || branch == null) {
            throw new NullPointerException("Agreement or Branch is null");
        }
        if (!Objects.equals(agreement.getBranchID(), branch.getBranchID())) {
            throw new IllegalArgumentException("Agreement branch IDs do not match");
        }
        this.branch = branch;
        this.agreement = agreement;
        this.suppliedItems = new HashMap<SuppliedItem, Integer>();
        this.orderDate = new Date();
        this.orderID = String.valueOf(generateOrderID());
    }

    private static int generateOrderID() {
        return ++idCounter;
    }


    // Order constructor -> gets orderDTO and create Order
    public Order(OrderDTO orderDTO) throws Exception {
        if (orderDTO == null) {
            throw new IllegalArgumentException("orderDTO is null");
        }

        Branch branch = new Branch(BranchesRepository.getInstance().getBranch(orderDTO.getBranchID()));
        BranchDTO branchDTO = branch.getBranchDTO();
        SupplierDTO supplierDTO = SupplierRepository.getInstance().getSupplier(orderDTO.getSupplierID());
        AgreementDTO agreementDTO = AgreementRepository.getInstance().getAgreement(branchDTO.getBranchID(), supplierDTO.getSupplierID());
        Agreement agreement = new Agreement(branchDTO, supplierDTO, agreementDTO);

        if (agreement == null) {
            throw new NullPointerException("Agreement is missing in memory");
        }

        Order order = new Order(agreement, branch);
        order.totalPrice = orderDTO.getTotalPrice();
        order.orderClosed = true;

        for (Map.Entry<SuppliedItemDTO, Integer> entry : orderDTO.getSuppliedItems().entrySet()) {
            SuppliedItemDTO itemDTO = entry.getKey();
            int quantity = entry.getValue();

            Product product = new Product(
                    itemDTO.product.productID,
                    itemDTO.product.productName,
                    itemDTO.product.productManufacturer,
                    itemDTO.product.shelfLifeDays
            );

            SuppliedItem item = new SuppliedItem(itemDTO.suppliedItemPrice, product);
            order.suppliedItems.put(item, quantity);
        }
    }


    //adds item to an order
    public void addItemToOrder(String itemId, int quantity) throws Exception {
        if (itemId == null || itemId.isEmpty() || orderID == null || quantity <= 0) {
            throw new NullPointerException("Product ID cannot be null or empty || Quantity cannot be less than 1");
        }

        if (this.orderClosed){
            throw new Exception("Order is closed");
        }

        for (SuppliedItem item : suppliedItems.keySet()) {
            if (item.getSuppliedItemID().equals(itemId)) {
                suppliedItems.put(item, quantity + suppliedItems.get(item));
                this.totalPrice = this.getTotalPrice();
                return;
            }
        }

        for (SuppliedItem item : agreement.getSupplierItemsList()){
            if (item.getSuppliedItemID().equals(itemId)) {
                suppliedItems.put(item, quantity);
                this.totalPrice = this.getTotalPrice();
                return;
            }
            throw new Exception("Invalid item, " + itemId + " doesnt exist in the agreement, enter valid ID");
        }
    }
    // returns total price of the order
    public int getTotalPrice() {
        int totalPrice = 0;
        List<Discount> discounts = agreement.getDiscounts();
        for (SuppliedItem item : suppliedItems.keySet()) {
            totalPrice += item.getSuppliedItemPrice() * suppliedItems.get(item);
            for (Discount discount : discounts) {
                if (discount.getSuppliedItem().equals(item) && suppliedItems.get(item) >= discount.getQuantity() ) {
                    totalPrice -= discount.getDiscount();
                }
            }
        }
        return totalPrice;
    }

    //displays an order
    public void displayOrder() {
        System.out.println("Order Number: " + this.orderID);
        System.out.println("Order Date: " + this.orderDate);
        System.out.println("Order Address: " + this.getBranch().getBranchCity() + ", " + this.getBranch().getBranchAddress());
        System.out.println("Total Price: " + this.totalPrice + "₪");
        System.out.println("Items: ");
        for (SuppliedItem item : suppliedItems.keySet()) {
            System.out.println("Item id: " + item.getSuppliedItemID() + ", Name: " + item.getProduct().getProductName() +
                    " price: " + item.getSuppliedItemPrice() + "₪");
            System.out.println("\tquantity: " + this.suppliedItems.get(item));
        }
        System.out.println("*********************************************************");
    }

    public Boolean isOrderClosed() {
        return orderClosed;
    }

    public String getOrderID() {
        return orderID;
    }

    public void closeOrder() {
        if (totalPrice <= 0) {
            throw new NullPointerException("Cannot finish an order with an empty cart");
        }
        orderClosed = true;
    }

    public Branch getBranch() {
        return branch;
    }

    public List<SuppliedItem> getSuppliedItems() {
        return new ArrayList<>(suppliedItems.keySet());
    }


    public OrderDTO getOrderDTO(){
        Map<SuppliedItemDTO, Integer> suppliedItemsMap = new HashMap<>();
        for (SuppliedItem item : suppliedItems.keySet()) {
            suppliedItemsMap.put(item.getSuppliedItemDTO(), suppliedItems.get(item));
        }
        return new OrderDTO(this.orderID, this.orderDate, this.totalPrice, suppliedItemsMap, this.branch.getBranchID(), this.agreement.getSupplierID());
    }
}