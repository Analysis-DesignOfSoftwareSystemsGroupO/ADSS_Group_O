package SupplierMoudleSource.Presentation;

import SupplierMoudleSource.Controllers.SupplierController;
import SupplierMoudleSource.Service.SupplierService;

import java.util.Scanner;

public class PresentSupplierOptions {
    private final SupplierService supplierController = new SupplierService();

    private String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    //main menu for supplier
    public void runSupplierMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Supplier Menu:");
            System.out.println("1.Add Supplier");
            System.out.println("2.Edit Supplier Details");
            System.out.println("3.Get Supplier Information Contacts");
            System.out.println("4.Remove Supplier From System");
            System.out.println("5.Return to Main Menu");
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
                        getAllInformationContacts();
                        break;
                    case 4:
                        removeSupplierFromSystem();
                    case 5:
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
        System.out.println("Starting Define New Supplier, Please Follow the Next Steps");
        System.out.println("Enter Supplier ID: ");
        String supplierID = scanner.nextLine();
        if (supplierController.validIdSupplier(supplierID)){
            System.out.println("Supplier ID: " + supplierID + " already exist");
            return;
        }
        System.out.println("Enter Supplier Name: ");
        String supplierName = scanner.nextLine();
        System.out.println("Enter Supplier Payment Method (CreditCard / Cash / Bank Transfer / Check): ");
        String supplierPaymentMethod = scanner.nextLine();
        System.out.println("Define Supplier's Bank");
        System.out.println("Enter Supplier Bank Number: ");
        String supplierBankNumber = scanner.nextLine();
        System.out.println("Enter Supplier Bank Branch Number: ");
        String supplierBankBranchNumber = scanner.nextLine();
        System.out.println("Enter Supplier Bank Account Number: ");
        String supplierBankAccountNumber = scanner.nextLine();
        System.out.println("Define Supplier's Information Contact");
        System.out.println("** Supplier must have at least one information contact **");
        System.out.println("Enter Contact Name: ");
        String contactName = scanner.nextLine();
        System.out.println("Enter contact Phone Number: ");
        String contactPhoneNumber = scanner.nextLine();
        System.out.println("Enter Contact Title: ");
        String contactTitle = scanner.nextLine();
        System.out.println("Enter Delivery Way (Constant Delivery / Temporary Delivery / Self Pick Up): ");
        String deliveryWay = scanner.nextLine();
        String dayOfWeek = "";
        if (deliveryWay.equals("Constant Delivery")){
            while (true){
                System.out.println("Enter day of the week: Sunday / Monday / Tuesday / Wednesday / Thursday / Friday / Saturday ");
                dayOfWeek = scanner.nextLine();
                if (!validWeek(dayOfWeek)){
                    System.out.println("Invalid day of week");
                    continue;
                }
                break;
            }

        }

        try {
            supplierController.createSupplier(supplierID, supplierName, supplierPaymentMethod,
                    supplierBankAccountNumber, supplierBankNumber, supplierBankBranchNumber, contactName,
                    contactPhoneNumber, contactTitle, deliveryWay, dayOfWeek);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Supplier Added successfully ! ");
        System.out.println("***************************************************");
    }

    private boolean validWeek(String dayOfWeek){
        for (String day : daysOfWeek){
            if (day.equals(dayOfWeek)){
                return true;
            }
        }
        return false;
    }
    private void editSupplierPresentation() {
        Scanner scanner = new Scanner(System.in);
        try{
            supplierController.printAllSuppliers();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Enter Supplier ID: ");
        String id = scanner.nextLine();
        if (!supplierController.validIdSupplier(id)){
            System.out.println("Invalid Supplier ID ! ");
            return;
        }
        while (true)
        {
            System.out.println("1.Add new product to supplier");
            System.out.println("2.Edit supplier Information");
            System.out.println("3.Add Information Contact");
            System.out.println("4.Return to menu");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1: //add new product
                        this.addNewProductToSupplier(id);
                        break;
                    case 2: //edit supplier details
                        this.editSupplierDetails(id);
                        return;
                    case 3: //add information contact
                        this.addInformationContact(id);
                        return;
                    case 4:
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
    // helper method to add a product
    private void addNewProductToSupplier(String supplierId){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Product ID: ");
        String productID = scanner.nextLine();
        System.out.println("Enter Product Name: ");
        String productName = scanner.nextLine();
        System.out.println("Enter Product Manufacturer: ");
        String productManufacturer = scanner.nextLine();
        System.out.println("Enter Product Price: ");
        int productPrice = scanner.nextInt();
        try {
            supplierController.addNewProductToSupplier(supplierId, productID, productName, productManufacturer, productPrice);
            System.out.println("Product added successfully");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    //helper func to edit information contact
    private void editInformationContact(String supplierid){
        Scanner scanner = new Scanner(System.in);
        supplierController.viewInformationContacts(supplierid);
        System.out.println("Enter information contact name: ");
        String contactName = scanner.nextLine();
        System.out.println("Enter new title for " + contactName + " : ");
        String newTitle = scanner.nextLine();
        System.out.println("Enter new phone number for" + contactName + " : ");
        String newPhone = scanner.nextLine();
        try {
            supplierController.updateSupplierInformationContact(supplierid, contactName, newTitle, newPhone);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(contactName + " updated successfully");
    }
    //helper func to edit bank details
    private void editBankDetails(String supplierId){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new bank number: ");
        String newBankNumber = scanner.nextLine();
        System.out.println("Enter new bank branch number: ");
        String newBankBranch = scanner.nextLine();
        System.out.println("Enter new bank account number: ");
        String newBankAccountNumber = scanner.nextLine();
        try {
            supplierController.updateSupplierBankAccount(supplierId, newBankAccountNumber, newBankNumber, newBankBranch);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Added successfully");
    }
    //helper func to edit the delivery way
    private void editDeliveryWay(String supplierId){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Delivery Way (Constant Delivery / Temporary Delivery / Self Pick Up): ");
        String deliveryWay = scanner.nextLine();
        String dayOfWeek = "";
        if (deliveryWay.equals("Constant Delivery")) {
            while (true) {
                System.out.println("Enter day of the week: Sunday / Monday / Tuesday / Wednesday / Thursday / Friday / Saturday ");
                dayOfWeek = scanner.nextLine();
                if (!validWeek(dayOfWeek)) {
                    System.out.println("Invalid day of week");
                    continue;
                }
                break;
            }
        }
        try {
            supplierController.updateDeliveryMethod(supplierId, deliveryWay, dayOfWeek);
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
            System.out.println("Choose: ");
            System.out.println("1.Edit information contact details");
            System.out.println("2.Edit supplier name");
            System.out.println("3.Edit bank account number");
            System.out.println("4.Edit supplier delivery way");
            System.out.println("5.Return to Main Menu");
            System.out.println("Please enter your option: ");
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1: // edit information contact
                        this.editInformationContact(id);
                        break;
                    case 2: //edit name
                        System.out.println("Enter new name : ");
                        String newName = scanner.nextLine();
                        supplierController.updateSupplierName(id, newName);
                        break;
                    case 3: // edit bank details
                        this.editBankDetails(id);
                        break;
                    case 4: // edit delivery way
                            this.editDeliveryWay(id);
                        break;
                    case 5:
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
    //adds information contact
    private void addInformationContact(String supID) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new Information Contact name: ");
        String contactName = scanner.nextLine();
        System.out.println("Enter contact phone number: ");
        String contactPhoneNumber = scanner.nextLine();
        System.out.println("Enter contact title: ");
        String contactTitle = scanner.nextLine();
        try{
            supplierController.addNewInformationContact(supID, contactName, contactPhoneNumber, contactTitle);{
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(contactName + " was added successfully");
    }
    //prints all information contacts
    private void getAllInformationContacts() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Supplier ID: ");
        String supplierID = scanner.nextLine();
        try {
            supplierController.printAllInformationContacts(supplierID);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    // removes supplier from the sytem
    private void removeSupplierFromSystem(){
        Scanner scanner = new Scanner(System.in);
        try{
            supplierController.printAllSuppliers();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("** Notice : Removing Supplier Will Automatically Delete Agreements With That Supplier **");
        System.out.println("Enter Supplier ID: ");
        String supplierID = scanner.nextLine();
        try{
            supplierController.deleteSupplier(supplierID);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}


