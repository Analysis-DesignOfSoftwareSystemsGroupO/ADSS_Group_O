package Service;

import DataBase.OrderDataBase;

public class OrderService {
    private static OrderDataBase orderDataBase = new OrderDataBase();
    private int orderId = 2000;
    //creates a new order, returns orderId as a string
    public String createOrder(String branchId, String supplierId) {
        //todo
        return null;
    }

    public boolean isOrderEmpty(String OrderID) {
        //todo
        return false;
    }

    //deletes an order if the user decides to cancel order while in the making
    public void deleteOrder(String OrderID) {
        //todo
    }

    //prints the order for the user to view while making the order
    public void viewOrder(String OrderID) {
        //todo
    }

    //adds a product to the order, use doesProductExistsInAgreementFunc in AgreementService
    public void addProductToOrder(String ID, String productId) {
        //todo
    }

    //function that receives supplier id and prints all the past orders
    public void viewPastOrders(String supplierId){
        //todo
    }

    //method that finishes an orders (its point is to check if the order is empty, if it is throw an exception)
    public void finishOrder(String orderID) {
        //todo
    }

    public String getBranchId(String orderId){
        return null;
        //todo
    }
    public String getSupplierId(String orderId){
        //todo
        return null;
    }
}
