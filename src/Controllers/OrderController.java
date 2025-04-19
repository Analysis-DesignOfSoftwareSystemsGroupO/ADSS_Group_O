package Controllers;
import Service.OrderService;

public class OrderController {
    private OrderService orderService = new OrderService();
    /// Order functions
    //creates an order returns OrderId
    public String createOrder(String branchId, String supplierId) {
        return orderService.createOrder(branchId, supplierId);
    }
    public boolean isOrderEmpty(String OrderID) {
        return orderService.isOrderEmpty(OrderID);
    }
    //deletes an order if the user decides to cancel order while in the making
    public void deleteOrder(String OrderID) {
        orderService.deleteOrder(OrderID);
    }
    //prints the order for the user to view while making the order
    public void viewOrder(String OrderID) {
        orderService.viewOrder(OrderID);
    }
    //adds a product to the order
    public void addProductToOrder(String ID, String productId) {
        orderService.addProductToOrder(ID, productId);
    }

}
