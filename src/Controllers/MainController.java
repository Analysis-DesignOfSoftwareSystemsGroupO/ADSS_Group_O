package Controllers;
import Presentation.*;
import SupplierMoudle.*;
import DataBase.*;


public class MainController {
    private static SuppliersDataBase suppliersDataBase = new SuppliersDataBase();
    private static OrderDataBase orderDataBase = new OrderDataBase();


    /// Supplier functions
    //creates a supplier and saves it in the database
    public static void createSupplier(String ID, String accountNumber, String supplierName,
                                      String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        //todo
    }

    //adds a new product to an existing supplier
    public static void addNewProductToSupplier(String ID, Product product, int price) {
        //todo
    }

    //checks validity of the id of the supplier
    public static boolean validIdSupplier(String ID) {
        return false;
        //todo
    }

    //prints all existing suppliers
    public static void printAllSuppliers() {
        //todo
    }

    //prints the details of a specific supplier
    public static void printSupplier(String supplierId) {
        //todo
    }

    public static void updateSupplierPhoneNumber(String ID, String phoneNumber) {
        //todo
    }

    public static void updateSupplierBankAccount(String ID, String bankAccount, String bankNumber, String bankBranch
            , String ownerID) {
        //todo
    }

    public static void updateName(String ID, String name) {
        //todo
    }



    
    /// Order functions
    //creates an order returns OrderId
    public static String createOrder(String branchId, String supplierId) {
        return null;
        //todo
    }
    public static boolean isOrderEmpty(String OrderID) {
        //todo
        return false;
    }
    //deletes an order if the user decides to cancel order while in the making
    public static void deleteOrder(String OrderID) {
        //todo
    }
    //prints the order for the user to view while making the order
    public static void viewOrder(String OrderID) {
        //todo
    }
    //adds a product to the order
    public static void addProductToOrder(String ID, String productId) {
        //todo
    }




    /// agreement functions
    public static void removeAgreement(String branchId, String SupplierId) {
        //todo
    }

    public static void createNewAgreement(String supplierID, String branchId) {
        //todo
    }

    //create a supplier and saves it in the database
    public static void viewAllAgreements() {
        //todo
    }

    // adds a product to the agreement
    public static void addProductToAgreement(String supplierID, String branchId, String productId, int quantity, int discount) {
        //todo
    }

    //checks if an agreement exists
    public static boolean agreementExists(String SupplierID, Branch branch) {
        return false;
        //todo
    }
    //check if an item exists in an Agreement
    public static boolean productExistsInAgreement(String supplierID, String branchId, String productID) {
        return false;
        //todo
    }






}
