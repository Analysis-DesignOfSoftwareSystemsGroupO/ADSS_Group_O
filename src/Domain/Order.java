package Domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private static int idCounter = 0;
    private final String orderID;
    private final Date orderDate;
    private int totalPrice;
    private final Agreement agreement;
    private List<SuppliedItem> suppliedItems;
    private Boolean orderClosed = false;
    private Branch branch;

    public Order(Agreement agreement, Branch branch) {
        if (agreement == null || branch == null) {
            throw new NullPointerException("Agreement or branch is null");
        }
        this.branch = branch;
        this.agreement = agreement;
        this.suppliedItems = new ArrayList<>();
        this.orderDate = new Date();
        this.orderID = String.valueOf(generateOrderID());
    }

    public Order(Order other) {
        this.orderID = other.orderID;
        this.orderDate = other.orderDate;
        this.totalPrice = other.totalPrice;
        this.agreement = other.agreement;
        this.suppliedItems = other.suppliedItems;
    }

    private static int generateOrderID() {
        return ++idCounter;
    }


    public Boolean addItemToOrder(String itemID, int quantity) {
        if (itemID == null || itemID.isEmpty() || orderID == null || quantity <= 0) {
            throw new NullPointerException("Product ID cannot be null or empty || Quantity cannot be less than 1");
        }
        for (SuppliedItem item : agreement.getSupplierItemsList()){
            int discountPercentage = 0;
            if (item.getSuppliedItemID().equals(itemID)) {
                for (Discount discount : agreement.getDiscounts()) {
                    if (discount.getSuppliedItem().equals(item) && quantity >= discount.getQuantity()) {
                        discountPercentage = discount.getDiscount();
                        break;
                    }
                }
                int discountedPrice = item.getSuppliedItemPrice() - (item.getSuppliedItemPrice() * discountPercentage / 100);
                for (int i = 0; i < quantity; i++) {
                    suppliedItems.add(item);
                    totalPrice += discountedPrice;
                }
                return true;
            }
        }
        return false;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void displayOrder() {
        System.out.println("Order Number: " + this.orderID);
        System.out.println("Order Date: " + this.orderDate);
        System.out.println("Total Price: " + this.totalPrice);
        System.out.println("Items: ");
        for (SuppliedItem item : suppliedItems) {
            System.out.println(item.getSuppliedItemID() + ": " + item.getSuppliedItemPrice());
        }
    }

    public Boolean isOrderClosed() {
        return orderClosed;
    }

    public String getOrderID() {
        return orderID;
    }

    public void closeOrder() {
        if (totalPrice <= 0) {
            throw new NullPointerException("Add Products to your cart");
        }
        orderClosed = true;
    }

    public Branch getBranch() {
        return branch;
    }
}
