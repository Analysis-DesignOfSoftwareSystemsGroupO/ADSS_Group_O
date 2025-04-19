package Presentation;

import java.util.Scanner;

public class PresentOrderOptions {
    public void runOrderMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) { // order menu loop
            System.out.println("Order Options:\n");
            System.out.println("1.View all previous orders");
            System.out.println("2.Add Order\n");
            System.out.println("3.Return to main menu\n");
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
                System.out.println("Invalid option ! \n");
            }
        }
    }

    // this function is used for viewing all orders by supplyId
    private void viewOrdersPresentation() {
        Scanner scanner = new Scanner(System.in);
        //TODO: print all supplier Id's
        System.out.println("Enter supplier ID: ");
        try {
            int supplierID = scanner.nextInt();
            //TODO: domain function to get all orders from supplier id throw an exception if invalid Id.
        }
        catch (Exception e) {
            System.out.println("Invalid supplier ID ! \n");
            return;
        }
    }

    //this function is used for adding a new order
    private void addOrderPresentation() {
        //todo: domain func to create an order
        Scanner scanner = new Scanner(System.in);
        //todo: domain func to print all supplier id's
        System.out.println("Enter supplier ID: ");
        int supplierID = scanner.nextInt();
        //todo: check for existing agreement
        while (true){
            //todo:domain func to print all items from this suppliers agreement with the branch
            System.out.println("To add product Enter the product ID\n");
            System.out.println("To finalize order enter '*'\n");
            System.out.println("To cancel the Order press '-'\n");
            System.out.println("Choice: ");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case '*':
                        //todo: add check if order is empty, if empty print message and return to order menu
                        System.out.println("order created successfully !\n");
                        break;
                    case '-': //cancel existing order option
                        while (true){
                            System.out.println("Are you sure you want to cancel ? y/n\n");
                            scanner.nextLine();
                            if (scanner.nextLine().equals("y")) {
                                //todo :domain func delete order
                                return;
                            } else if (scanner.nextLine().equals("n")) {
                                break;
                            }
                            else {
                                System.out.println("Invalid option ! \n");
                            }
                        }
                        break;
                    default:
                        //todo : check if it is a valid product id, ask for quantity and add to the order
                        // using a domain function
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
