package SupplierMoudleSource.Presentation;

import SupplierMoudleSource.Service.AgreementService;
import SupplierMoudleSource.Service.OrderService;
import SupplierMoudleSource.Service.SupplierService;

import java.util.Scanner;

public class PresentOrderOptions {
    private final SupplierService supplierController = new SupplierService();
    private final OrderService orderController = new OrderService();
    private final AgreementService agreementController = new AgreementService();
    public void runOrderMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) { // order menu loop
            System.out.println("Order Options:");
            System.out.println("1.View Previous Orders Of Supplier");
            System.out.println("2.Add Order");
            System.out.println("3.Return to main menu");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1: // view all orders
                        this.viewOrdersPresentation();
                        break;
                    case 2: //add a new order
                        this.addOrderPresentation();
                        break;
                    case 3: // return to main menu
                        return;
                    default:
                        throw new Exception();
                }
            }
            catch (Exception e) {
                System.out.println("Invalid option ! ");
            }
        }
    }

    // this function is used for viewing all orders by supplyId
    private void viewOrdersPresentation() {
        Scanner scanner = new Scanner(System.in);
        try{
            supplierController.printAllSuppliers();
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Enter Supplier ID: ");
        try {
            String supplierID = scanner.nextLine();
            orderController.viewPastOrders(supplierID);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //this function is used for adding a new order
    private void addOrderPresentation() {
        Scanner scanner = new Scanner(System.in);
        agreementController.viewAllAgreements();
        System.out.println("Enter supplier ID: ");
        String supplierID = scanner.nextLine();
        System.out.println("Enter branch Id: ");
        String branchId = scanner.nextLine();
        String orderID = ""; // initialize order id
        //create an empty order
        try {
            orderID = orderController.createOrder(branchId, supplierID); // save order id
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        //order Loop
        while (true){
            //view the agreement of products
            try {
                System.out.println("*********************************************************");
                agreementController.viewAgreement(branchId, supplierID);
                System.out.println("*********************************************************");
                orderController.viewConcurrentOrder(supplierID, orderID);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
            System.out.println("To add product Enter the product ID");
            System.out.println("To finalize order enter '*'");
            System.out.println("To cancel the order press '-'");
            System.out.println("Enter product ID: ");
            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "*": // finish order choice
                        try {
                            orderController.finishOrder(supplierID, orderID);
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                            continue;
                        }
                        System.out.println("Order finished Successfully !");
                        return;
                    case "-": //cancel existing order option
                        while (true){
                            System.out.println("Are you sure you want to cancel ? y/n");
                            String res = scanner.nextLine();
                            if (res.equals("y")) {
                                try {
                                    orderController.deleteConcurrentOrder(supplierID, orderID);
                                    System.out.println("Order canceled");
                                }
                                catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                return;
                            } else if (res.equals("n")) {
                                break;
                            }
                            else {
                                System.out.println("Invalid option ! ");
                            }
                        }
                        continue;
                    default:
                        try {
                            System.out.println("Enter quantity:");
                            int quantity = scanner.nextInt();
                            orderController.addProductToOrder(supplierID, orderID, choice, quantity);
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                        System.out.println("Product added successfully !");
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Invalid choice !");
                return;
            }
            scanner.nextLine();
        }
    }


}
