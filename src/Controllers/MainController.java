package Controllers;
import Presentation.*;
import Domain.*;
import DataBase.*;

import javax.crypto.spec.PSource;
import java.util.List;
import java.util.Set;


public class MainController {
    private static SuppliersDataBase suppliersDataBase = new SuppliersDataBase();
    private static OrderDataBase orderDataBase = new OrderDataBase();


    /// Supplier functions
    //creates a supplier and saves it in the database
    public static void createSupplier(String ID, String accountNumber, String supplierName,
                                      String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        try {
            Supplier newSupplier = new Supplier(ID, accountNumber, supplierName, bankAccount, bankNumber, bankBranch, ownerID);
            if (newSupplier != null) {
                suppliersDataBase.addSupplier(newSupplier);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //adds a new product to an existing supplier
    public static void addNewProductToSupplier(String supplierID, Product product, int price) {
        //todo
    }

    //checks validity of the id of the supplier
    public static boolean validIdSupplier(String ID) {
       return suppliersDataBase.suppliers.containsKey(ID);
    }

    //prints all existing suppliers
    public static void printAllSuppliers() {
        for (Supplier supplier : suppliersDataBase.suppliers.values()) {
            System.out.println(supplier);
        }
    }

    //prints the details of a specific supplier
    public static void printSupplier(String supplierId) {
        if (suppliersDataBase.suppliers.containsKey(supplierId)) {
            System.out.println(suppliersDataBase.suppliers.get(supplierId));
        }
    }

    public static void updateSupplierPhoneNumber(String ID, String phoneNumber) {
        //todo
    }

    //*******************
    //force owner id to be supplier id
    public static void updateSupplierBankAccount(String supplierID, String bankAccount, String bankNumber, String bankBranch
            , String ownerID) {
        if(suppliersDataBase.suppliers.containsKey(supplierID)) {
            try {
                Supplier supplier = suppliersDataBase.suppliers.get(supplierID);
                supplier.setNewBank(bankAccount, bankNumber, bankBranch, ownerID);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateSupplierName(String supplierID, String name) {
        try{
            Supplier supplier = suppliersDataBase.suppliers.get(supplierID);
            supplier.setSupplierName(name);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    
    /// Order functions
    //creates an order returns OrderId
    public static String createOrder(Branch branch, String supplierId) {
        try {
            Agreement agreement = suppliersDataBase.getAgreement(branch.getBranchID(), supplierId);
            if (agreement == null) {
                return null;
            }
            Order newOrder = new Order(agreement, branch);
            orderDataBase.addOrder(agreement.getSupplierID(), newOrder);
            return newOrder.getOrderID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isOrderEmpty(String supplierID,String OrderID) {
        List<Order> orders = orderDataBase.orders.get(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(OrderID)) {
                return (order.getTotalPrice() == 0);
            }
        }
        return true;
    }

    //deletes an order if the user decides to cancel order while in the making
    public static void deleteOrder(String supplierID, String OrderID) {
        List<Order> orders = orderDataBase.orders.get(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(OrderID)) {
                if (!order.isOrderClosed()) {
                    orderDataBase.orders.remove(order.getOrderID());
                }
                else{
                    System.out.println("Order is already closed");
                }
            }
        }
    }

    //prints the order for the user to view while making the order
    public static void viewOrder(String supplierID, String OrderID) {
        List<Order> order = orderDataBase.orders.get(supplierID);
        for (Order o : order) {
            if (o.getOrderID().equals(OrderID)) {
               o.displayOrder();
               break;
            }
        }
    }

    //adds a product to the order
    public static void addProductToOrder(String supplierID, String orderID, String productId, int quantity) {
        List<Order> orders = orderDataBase.orders.get(supplierID);
        for (Order order : orders) {
            if (order.getOrderID().equals(orderID)) {
                try {
                    order.addItemToOrder(productId, quantity);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /// agreement functions
    public static void removeAgreement(String branchId, String SupplierId) {
      Set<SupplierBranchKey> keys = suppliersDataBase.suppliersAgreements.keySet();
      for (SupplierBranchKey key : keys) {
          if (key.getBranchID().equals(branchId)) {
              suppliersDataBase.suppliersAgreements.remove(key);
              break;
          }
      }
    }

    public static void createNewAgreement(String supplierID, String branchId) {
        // Branch branch = bring the branch from Data Base
        Supplier supplier = suppliersDataBase.suppliers.get(supplierID);

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
