package SupplierMoudle.presentation;

import java.util.Scanner;

public class PresentSupplierOptions {

    public void runSupplierMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Supplier Menu:\n");
            System.out.println("1.Add Supplier\n");
            System.out.println("2.Edit Supplier Details\n");
            System.out.println("3.Remove Supplier\n");
            System.out.println("4.Return to Main Menu\n");
            System.out.println("Please enter your option: ");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        addSupplierPresentation();
                        break;
                    case 2:
                        editSupplierPresentation();
                        break;
                    case 3:
                        break;
                    case 4:
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

    public void addSupplierPresentation() {
        //todo: get all the required info from the user and use a domain func to create the supplier domain func
        // to create the supplier
    }

    public void editSupplierPresentation() {
        Scanner scanner = new Scanner(System.in);
        //todo print all id's
        System.out.println("Enter Supplier ID: ");
        String id = scanner.nextLine();
        //todo check validity of id
        while (true)
        {
            System.out.println("1.Add new product\n");
            System.out.println("2.Edit supplier details\n");
            System.out.println("3.Return to menu\n");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        this.addNewProduct(id);
                        break;
                    case 2:
                        this.editSupplierDetails(id);
                        break;
                    case 3:
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

    private void addNewProduct(String id){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.println("Enter product price: ");
        int productPrice = scanner.nextInt();
        //todo: add to database
        return;
    }
    private void editSupplierDetails(String id){
        Scanner scanner = new Scanner(System.in);
        //todo print detail

        System.out.println("1.");

    }
}


