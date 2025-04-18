package SupplierMoudle.presentation;

import java.util.Scanner;

public class OrderOptions {
    private MenuMain menuMain;
    public OrderOptions(MenuMain menu) {
        this.menuMain = menu;
    }
    public void runOrderMenu() {
        System.out.println("Order Options:\n");
        System.out.println("1.View all previous orders");
        System.out.println("2.Add Order\n");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                this.viewOrders();
                break;
            case 2:



        }

    }
    private void viewOrders() {
        Scanner scanner = new Scanner(System.in);
        //todo: print all supplier Id's
        System.out.println("Enter supplier ID: ");
        try {
            int supplierID = scanner.nextInt();
            //TODO: domain function to get all orders from supplier id throw an exception if invalid Id.
        }
        catch (Exception e) {
            System.out.println("Invalid supplier ID");
            this.runOrderMenu();
        }
    }

    private void addOrder() {
        //todo: domain func to create an order

        Scanner scanner = new Scanner(System.in);
        //todo: domain func to print all supplier id's
        System.out.println("Enter supplier ID: ");
        int supplierID = scanner.nextInt();
        //todo: check for valid supplier Id
        // and print all items from this supplier

        while (true){
            System.out.println("To add product Enter the product ID\n");
            System.out.println("To finalize order enter '*'\n");
            System.out.println("Choice: ");
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case '*':
                        //todo: add check if order is empty, if empty print message and return to order menu
                        System.out.println("order created successfully !\n");
                        break;
                    default:
                        break;

                }

            }
            catch (Exception e) {
                System.out.println("Invalid choice !");
                continue;
            }
        }




    }
}
