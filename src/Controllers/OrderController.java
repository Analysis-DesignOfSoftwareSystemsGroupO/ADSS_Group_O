package Controllers;
import Service.AgreementService;
import Service.BranchService;
import Service.OrderService;

public class OrderController {
    private final OrderService orderService = new OrderService();
    private final BranchService branchService = new BranchService();
    private final AgreementService agreementService = new AgreementService();
    /// Order functions
    //creates an order returns OrderId
    public String createOrder(String branchId, String supplierId) {
        return orderService.createOrder(branchId, supplierId);
    }
    public boolean isOrderEmpty(String OrderID, String supplierId) {
        return orderService.isOrderEmpty(OrderID, supplierId);
    }
    //deletes an order if the user decides to cancel order while in the making
    public void deleteConcurrentOrder(String orderId, String supplierId) throws Exception {
        orderService.deleteOrder(supplierId, orderId);
    }
    //prints the order for the user to view while making the order
    public void viewConcurrentOrder(String OrderID, String supplierId) throws Exception {
        orderService.viewOrder(supplierId, OrderID);
    }

    //adds a product to the order
    public void addProductToOrder(String supplierID, String orderId, String productId, int quantity) throws Exception {
        //check if the product exists in the agreement
        orderService.addProductToOrder(supplierID, orderId, productId, quantity);

    }

    //prints all orders from a specific suppliers
    public void viewPastOrders(String supplierId){
        orderService.viewPastOrders(supplierId);
    }

    public void printAllBranchIds(){
        branchService.printAllBranchIds();
    }

    //method that finishes an orders (its point is to check if the order is empty, if it is throw an exception )
    public void finishOrder(String orderID, String supplierID){
        orderService.finishOrder(supplierID, orderID);
    }
}
