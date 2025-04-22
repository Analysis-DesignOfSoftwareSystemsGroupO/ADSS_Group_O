package Presentation;

import java.util.Scanner;


import Controllers.*;

public class PresentAgreementOptions {
    private final AgreementController agreementController = new AgreementController();
    private final SupplierController supplierController = new SupplierController();

    public void runAgreementMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Pick Agreement Options:");
            System.out.println("1.View all Agreements");
            System.out.println("2.Add new agreement");
            System.out.println("3.Remove agreement");
            System.out.println("4.Edit agreement");
            System.out.println("5.Return to main menu");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        agreementController.viewAllAgreements();
                        break;
                    case 2:
                        this.addNewAgreementP();
                        break;
                    case 3:
                        this.removeAgreementP();
                        break;
                    case 4:
                        this.editAgreementP();
                        break;
                    case 5:
                        return;
                    default:
                        throw new Exception();
                }
            }
            catch (Exception e) {
                System.out.println("Invalid option !");
            }
        }
   }


    //add a new agreement
    private void addNewAgreementP() {
        Scanner scanner = new Scanner(System.in);
        supplierController.printAllSuppliers(); // prints all suppliers
        //input Supplier id
        System.out.println("Enter supplier's id:");
        String supplierId = scanner.nextLine();
        System.out.println("Enter branch id:");
        String branchId = scanner.nextLine();
        try {
            // transfer to controller
            agreementController.createNewAgreement(supplierId, branchId);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //helper method to remove an agreement
    private void removeAgreementP() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter supplier's id:");
        String supplierId = scanner.nextLine();
        System.out.println("Enter branch id:");
        String branchId = scanner.nextLine();
        agreementController.viewAgreement(branchId, supplierId);
        try {
            agreementController.removeAgreement(branchId, supplierId);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    private void editAgreementP() {
        Scanner editAgreement = new Scanner(System.in);
        while (true){
            System.out.println("1.Add product to agreement\n");
            System.out.println("2.Remove product from agreement\n");
            System.out.println("3.Edit product discount agreement\n");
            System.out.println("4.Return to main menu\n");
            int choice = editAgreement.nextInt();
            String agreementID = null;
            String supplierID = "", branchId = "";
            if (choice == 1 || choice == 2 || choice == 3) {
                System.out.println("Enter branch Id: ");
                branchId = editAgreement.nextLine();
                supplierController.printAllSuppliers();
                System.out.println("Enter supplier ID: ");
                supplierID = editAgreement.nextLine();
                try {
                    agreementController.viewAgreement(supplierID, branchId);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }
            switch (choice) {
                case 1:
                    this.addProductToAgreementP(supplierID, branchId);
                    break;
                case 2:
                   this.removeProductFromAgreementP(supplierID, branchId);
                    break;
                case 3:
                    this.editProductDiscount(supplierID, branchId);
                case 4:
                    return;
                default:
                    System.out.println("Invalid option !\n");
                    break;
            }

        }
    }

    ///edit agreement functions
    // adds a product to an existing agreement
    private void addProductToAgreementP(String supplierID, String branchId) {
        //todo print all product from
        Scanner editAgreement = new Scanner(System.in);
        System.out.println("Enter product name:");
        String productName = editAgreement.nextLine();
        System.out.println("Enter product price:");
        int price = editAgreement.nextInt();
        int productDiscount = -1;
        int productQuantity = -1;
        while (true) {
            System.out.println("Discount ? y/n");
            editAgreement.nextLine();
            if (editAgreement.nextLine().equals("y")) {
                System.out.println("Enter product quantity for discount:");
                productQuantity = editAgreement.nextInt();
                System.out.println("Enter product discount:");
                productDiscount = editAgreement.nextInt();
                break;
            } else if (editAgreement.nextLine().equals("n")) {
                break;
            }
            System.out.println("Invalid option !\n");
        }
        try {
            agreementController.addProductToAgreement(supplierID, branchId , productName, price, productQuantity, productDiscount);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //removes a product from an existing agreement
    private void removeProductFromAgreementP(String supplierID, String branchId) {
        Scanner editAgreement = new Scanner(System.in);
        System.out.println("Enter product name:");
        String productId = editAgreement.nextLine();
        try {
            agreementController.removeProductFromAgreement(supplierID, branchId, productId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //check if available to combine with add product
    private void editProductDiscount(String supplierID, String branchId) {
        Scanner editAgreement = new Scanner(System.in);
        System.out.println("Enter product name:");
        String productName = editAgreement.nextLine();
        Integer productDiscount = null;
        Integer productQuantity = null;
        System.out.println("Enter new product quantity for discount:");
        productQuantity = editAgreement.nextInt();
        System.out.println("Enter new product discount:");
        productDiscount = editAgreement.nextInt();
        try {
            agreementController.editProductDiscount(branchId, supplierID, productName, productQuantity, productDiscount);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
