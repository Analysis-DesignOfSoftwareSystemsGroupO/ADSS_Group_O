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
    public boolean isOrderEmpty(String OrderID) {
        return orderService.isOrderEmpty(OrderID);
    }
    //deletes an order if the user decides to cancel order while in the making
    public void deleteConcurrentOrder(String OrderID) {
        orderService.deleteOrder(OrderID);
    }
    //prints the order for the user to view while making the order
    public void viewConcurrentOrder(String OrderID) {
        orderService.viewOrder(OrderID);
    }

    //adds a product to the order
    public void addProductToOrder(String supplierID, String branchId, String orderId, String productId, int quantity) throws Exception {
        //check if the product exists in the agreement
        if (agreementService.productExistsInAgreement(supplierID, branchId, productId)) {
                orderService.addProductToOrder(supplierID, orderId, productId, quantity);
        }
        else {
            throw new Exception("Invalid product id");
        }
    }

    //prints all orders from a specific suppliers
    public void viewPastOrders(String supplierId){
        orderService.viewPastOrders(supplierId);
    }

    public void printAllBranchIds(){
        branchService.printAllBranchIds();
    }

    //method that finishes an orders (its point is to check if the order is empty, if it is throw an exception )
    public void finishOrder(String orderID) {
        orderService.finishOrder(orderID);
    }
}
