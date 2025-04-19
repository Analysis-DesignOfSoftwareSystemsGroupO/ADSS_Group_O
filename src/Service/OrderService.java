package Service;

import DataBase.OrderDataBase;

public class OrderService {
    private static OrderDataBase orderDataBase = new OrderDataBase();
    public String createOrder(String branchId, String supplierId) {
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

    //adds a product to the order
    public void addProductToOrder(String ID, String productId) {
        //todo

    }
}
