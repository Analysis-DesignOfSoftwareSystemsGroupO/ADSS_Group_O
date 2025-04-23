package Controllers;

import Service.SupplierService;

public class SupplierController {
    private SupplierService supplierService = new SupplierService();

    /// Supplier functions
    //creates a supplier and saves it in the database
    //todo check if accountNumber field is necessary
    public void createSupplier( String supplierName,
                                      String bankAccount, String bankNumber, String bankBranch, String ownerID,
                                        String contactName, String contactPhoneNumber, String contactTitle) {
        supplierService.createSupplier("", supplierName, bankAccount, bankNumber, bankBranch, ownerID,
                contactName, contactPhoneNumber, contactTitle);
    }

    //adds a new product to an existing supplier
    public void addNewProductToSupplier(String supplierId, String productName, int price) {
        supplierService.addNewProductToSupplier(supplierId, productName, price);
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
    public void printSupplier(String supplierId) {
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

    public void printSupplierProducts(String supplierID) throws Exception {
        supplierService.printAllSupplierProducts(supplierID);
    }
}
