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

    public Order(Agreement agreement, Branch branch) {
        if (agreement == null || branch == null) {
            throw new NullPointerException();
        }
        this.agreement = agreement;
        this.suppliedItems = new ArrayList<>();
        this.orderDate = new Date();
        this.orderID = String.valueOf(generateOrderID());
    }

    private static int generateOrderID() {
        return ++idCounter;
    }


    public Boolean addItemToOrder(String itemID, int quantity) {
        if (itemID == null || itemID.isEmpty() || orderID == null) {
            throw new NullPointerException();
        }
        for (SuppliedItem item : agreement.supplierItemsList){
            int discountPercentage = 0;
            if (item.suppliedItemID.equals(itemID)) {
                for (Discount discount : agreement.getDiscounts()) {
                    if (discount.suppliedItem.equals(item) && quantity >= discount.quantity) {
                        discountPercentage = discount.getDiscount();
                        break;
                    }
                }
                int discountedPrice = item.suppliedItemPrice - (item.suppliedItemPrice * discountPercentage / 100);
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

    public void displayOrder(Order order) {
        if (order == null) {
            throw new NullPointerException();
        }
        System.out.println("Order Number: " + order.orderID);
        System.out.println("Order Date: " + order.orderDate);
        System.out.println("Total Price: " + order.totalPrice);
        System.out.println("Items: ");
        for (SuppliedItem item : suppliedItems) {
            System.out.println(item.suppliedItemID + ": " + item.suppliedItemPrice);
        }
    }
}
