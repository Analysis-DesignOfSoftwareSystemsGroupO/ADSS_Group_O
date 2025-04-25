package SupplierMoudleSource.Presentation;

import java.util.Scanner;


import SupplierMoudleSource.Controllers.*;
import SupplierMoudleSource.Service.AgreementService;
import SupplierMoudleSource.Service.SupplierService;

public class PresentAgreementOptions {
    private final AgreementService agreementController = new AgreementService();
    private final SupplierService supplierController = new SupplierService();

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
        System.out.println("Enter supplier's id: ");
        String supplierId = scanner.nextLine();
        System.out.println("Enter branch id: ");
        String branchId = scanner.nextLine();
        try {
            // transfer to controller
            agreementController.createNewAgreement(supplierId, branchId);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("New Agreement Between Branch: " + branchId + " , Supplier: " + supplierId  +   " was added !");
        System.out.println("*********************************************************");
    }
    //helper method to remove an agreement
    private void removeAgreementP() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter supplier's id:");
        String supplierId = scanner.nextLine();
        System.out.println("Enter branch id:");
        String branchId = scanner.nextLine();
        try {
            agreementController.removeAgreement(branchId, supplierId);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Agreement removed");
    }



    private void editAgreementP() {
        Scanner editAgreement = new Scanner(System.in);
        while (true){
            System.out.println("1.Add product to agreement");
            System.out.println("2.Remove product from agreement");
            System.out.println("3.Edit product discount agreement");
            System.out.println("4.Return to main menu");
            int choice = editAgreement.nextInt();
            editAgreement.nextLine();
            String supplierID = "", branchId = "";
            if (choice == 4){return;}
            if (choice == 1 || choice == 2 || choice == 3) {
                agreementController.viewAllAgreements();
                System.out.println("Enter supplier ID: ");
                supplierID = editAgreement.nextLine();
                System.out.println("Enter branch Id: ");
                branchId = editAgreement.nextLine();
                try {
                    agreementController.viewAgreement(branchId, supplierID);
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
                    break;
                default:
                    System.out.println("Invalid option !\n");
                    break;
            }

        }
    }

    // adds a product to an existing agreement
    private void addProductToAgreementP(String supplierID, String branchId) {
        try {
            System.out.println("Supplier's products :");
            supplierController.printSupplier(supplierID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Scanner editAgreement = new Scanner(System.in);
        System.out.println("Enter product ID:");
        String productID = editAgreement.nextLine();
        System.out.println("Enter product price:");
        int price = editAgreement.nextInt();
        editAgreement.nextLine();
        Integer productDiscount = null;
        Integer productQuantity = null;
        while (true) {
            System.out.println("Discount ? y/n");
            String result = editAgreement.nextLine();
            if (result.equals("y")) {
                System.out.println("Enter product quantity for discount:");
                productQuantity = editAgreement.nextInt();
                System.out.println("Enter product discount:");
                productDiscount = editAgreement.nextInt();
                editAgreement.nextLine();
                break;
            } else if (result.equals("n")) {
                break;
            }
            System.out.println("Invalid option !\n");
        }
        try {
            agreementController.addProductToAgreement(branchId ,supplierID, productID, price, productQuantity, productDiscount);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Product ID: " + productID + " was added to agreement");
    }

    //removes a product from an existing agreement
    private void removeProductFromAgreementP(String supplierID, String branchId) {
        if (agreementController.isAgreementEmpty(branchId, supplierID)){ // check if agreement has items
            System.out.println("Agreement has no items");
            return;
        }
        Scanner editAgreement = new Scanner(System.in);
        System.out.println("Enter product ID:");
        String productId = editAgreement.nextLine();
        try {
            agreementController.removeProductFromAgreement(supplierID, branchId, productId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Product ID: " + productId + " was deleted");
    }

    //check if available to combine with add product
    private void editProductDiscount(String supplierID, String branchId) {
        if (agreementController.isAgreementEmpty(branchId, supplierID)){ // check if agreement has items
            System.out.println("Agreement has no items");
            return;
        }
        Scanner editAgreement = new Scanner(System.in);
        System.out.println("Enter product ID:");
        String productID = editAgreement.nextLine();
        System.out.println("Enter new product quantity for discount:");
        int productQuantity = editAgreement.nextInt();
        System.out.println("Enter new product discount amount:");
        int productDiscount = editAgreement.nextInt();
        editAgreement.nextLine();
        try {
            agreementController.editProductDiscount(supplierID, branchId, productID, productQuantity, productDiscount);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Discount was updated successfully !");

    }
}
