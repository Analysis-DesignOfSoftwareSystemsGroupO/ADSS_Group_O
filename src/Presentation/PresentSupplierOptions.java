package Presentation;

import Controllers.SupplierController;

import java.util.Scanner;

public class PresentSupplierOptions {
    private final SupplierController supplierController = new SupplierController();

    public void runSupplierMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Supplier Menu:");
            System.out.println("1.Add Supplier");
            System.out.println("2.Edit Supplier Details");
            System.out.println("3.Return to Main Menu");
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

    private void addSupplierPresentation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Supplier Name: ");
        String supplierName = scanner.nextLine();
        System.out.println("Enter Supplier bank number: ");
        String supplierBankNumber = scanner.nextLine();
        System.out.println("Enter Supplier bank branch number: ");
        String supplierBankBranchNumber = scanner.nextLine();
        System.out.println("Enter Supplier bank account number: ");
        String supplierBankAccountNumber = scanner.nextLine();
        System.out.println("Enter Supplier account number: ");
        String supplierAccountNumber = scanner.nextLine();
        System.out.println("Enter Owner ID: ");
        String ownerID = scanner.nextLine();
        System.out.println("Enter contact name: ");
        String contactName = scanner.nextLine();
        System.out.println("Enter contact phone number: ");
        String contactPhoneNumber = scanner.nextLine();
        System.out.println("Enter contact title: ");
        String contactTitle = scanner.nextLine();

        try {
            supplierController.createSupplier(supplierName,
                    supplierBankAccountNumber, supplierBankNumber, supplierBankBranchNumber, ownerID, contactName,
                    contactPhoneNumber, contactTitle);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void editSupplierPresentation() {
        Scanner scanner = new Scanner(System.in);
        supplierController.printAllSuppliers();
        System.out.println("Enter Supplier ID: ");
        String id = scanner.nextLine();
        if (!supplierController.validIdSupplier(id)){
            System.out.println("Invalid Supplier ID ! ");
            return;
        }
        while (true)
        {
            System.out.println("1.Add new product");
            System.out.println("2.Edit supplier details");
            System.out.println("3.Add Contact number");
            System.out.println("3.Return to menu");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        this.addNewProductToSupplier(id);
                        break;
                    case 2:
                        this.editSupplierDetails(id);
                        return;
                    case 3:
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
    // helper method to add a product
    private void addNewProductToSupplier(String supplierId){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.println("Enter product price: ");
        int productPrice = scanner.nextInt();
        try {
            supplierController.addNewProductToSupplier(supplierId, productName, productPrice);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    //helper method to edit detail
    private void editSupplierDetails(String id){
        Scanner scanner = new Scanner(System.in);
        try {
            supplierController.printSupplier(id);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        while (true){
            System.out.println("1.Edit supplier phone");
            System.out.println("2.Edit supplier name");
            System.out.println("3.Edit bank account number");
            System.out.println("4.Return to Main Menu");
            System.out.println("Please enter your option: ");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("Enter new phone number: ");
                        String newPhone = scanner.nextLine();
                        supplierController.updateSupplierInformationContact(id, newPhone);
                        break;
                    case 2:
                        System.out.println("Enter new name : ");
                        String newName = scanner.nextLine();
                        supplierController.updateSupplierName(id, newName);
                        break;
                    case 3:
                        System.out.println("Enter new bank number: ");
                        String newBankNumber = scanner.nextLine();
                        System.out.println("Enter new bank branch number: ");
                        String newBankBranch = scanner.nextLine();
                        System.out.println("Enter new bank account number: ");
                        String newBankAccountNumber = scanner.nextLine();
                        try {
                            supplierController.updateSupplierBankAccount(id, newBankNumber, newBankBranch,
                                    newBankAccountNumber);
                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        return;
                    default:
                        throw new Exception();
                }
            }
            catch (Exception e){
                System.out.println("Invalid option ! ");
            }
        }
    }

}


