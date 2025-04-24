package Controllers;

import Service.SupplierService;

public class SupplierController {
    private SupplierService supplierService = new SupplierService();

    /// Supplier functions
    //creates a supplier and saves it in the database
    public void createSupplier(String supplierID, String supplierName, String paymentMethod,
                                      String bankAccount, String bankNumber, String bankBranch,
                                        String contactName, String contactPhoneNumber, String contactTitle, String deliveryWay) throws Exception {
        supplierService.createSupplier(supplierID, supplierName, paymentMethod, bankAccount, bankNumber, bankBranch,
                contactName, contactPhoneNumber, contactTitle, deliveryWay);
    }

    //adds a new product to an existing supplier
    public void addNewProductToSupplier(String supplierId, String productID, String productName, String productManufacturer, int price) {
        supplierService.addNewProductToSupplier(supplierId, productID, productName, price, productManufacturer);
    }

    //checks validity of the id of the supplier
    public boolean validIdSupplier(String ID) {
        return supplierService.validIdSupplier(ID);
    }

    //prints all existing suppliers
    public void printAllSuppliers() {
        supplierService.printAllSuppliers();
    }

    //prints the details of a specific supplier
    public void printSupplier(String supplierId) throws Exception {
        supplierService.printSupplier(supplierId);
    }

    public void updateSupplierInformationContact(String supplierId,String contactName, String title, String phoneNumber ) {
        supplierService.updateSupplierInfoContactPhoneNumber(supplierId,contactName, title, phoneNumber);
    }

    public void updateSupplierBankAccount(String supplierId, String bankAccount, String bankNumber, String bankBranch) {
        supplierService.updateSupplierBankAccount(supplierId, bankAccount, bankNumber, bankBranch);
    }

    public void updateSupplierName(String ID, String name) {
        supplierService.updateSupplierName(ID, name);
    }

    public void addNewInformationContact(String supplierID, String contactName, String newPhoneNumber, String newTitle) {
        supplierService.addNewInfoContact(supplierID, contactName, newPhoneNumber, newTitle);
    }

    public void printAllInformationContacts(String supplierID) {
        supplierService.getAllSupplierInformationContacts(supplierID);
    }

    public void deleteSupplierFromSystem(String supplierID){
        supplierService.deleteSupplier(supplierID);
    }
}
