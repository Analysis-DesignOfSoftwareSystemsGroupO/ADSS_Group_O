package Presentation;

import Controllers.AgreementController;
import Controllers.OrderController;
import Controllers.SupplierController;

import java.util.Scanner;

public class PresentOrderOptions {
    private final SupplierController supplierController = new SupplierController();
    private final OrderController orderController = new OrderController();
    private final AgreementController agreementController = new AgreementController();
    public void runOrderMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) { // order menu loop
            System.out.println("Order Options:");
            System.out.println("1.View previous orders");
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
        supplierController.printAllSuppliers();
        System.out.println("Enter supplier ID: ");
        try {
            String supplierID = scanner.nextLine();
            orderController.viewPastOrders(supplierID);
        }
        catch (Exception e) {
            System.out.println("Invalid supplier ID ! ");
        }
    }

    //this function is used for adding a new order
    private void addOrderPresentation() {
        Scanner scanner = new Scanner(System.in);
        orderController.printAllBranchIds(); // prints all branch id's
        System.out.println("Enter branch Id: ");
        String branchId = scanner.nextLine();
        supplierController.printAllSuppliers();
        System.out.println("Enter supplier ID: ");
        String supplierID = scanner.nextLine();
        String orderID = ""; // initialize order id
        //create an empty order
        try {
            orderID = orderController.createOrder(branchId, supplierID); // save order id
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        while (true){
            //view the agreement of products
            try {
                agreementController.viewAgreement(branchId, supplierID);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("To add product Enter the product ID");
            System.out.println("To finalize order enter '*'");
            System.out.println("To cancel the order press '-'");
            System.out.println("Choice: ");
            try {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "*": // finish order choice
                        try {
                            orderController.finishOrder(orderID);
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
                            scanner.nextLine();
                            if (scanner.nextLine().equals("y")) {
                                orderController.deleteConcurrentOrder(orderID);
                                return;
                            } else if (scanner.nextLine().equals("n")) {
                                break;
                            }
                            else {
                                System.out.println("Invalid option ! ");
                            }
                        }
                        break;
                    default:
                        try {
                            orderController.addProductToOrder(orderID, choice );
                        }
                        catch (Exception e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                        System.out.println("Order added Successfully !");
                        break;
                }
                break;
            }
            catch (Exception e) {
                System.out.println("Invalid choice !");
            }
        }
    }


}
