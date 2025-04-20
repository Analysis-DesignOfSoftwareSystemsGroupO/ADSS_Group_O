package Service;

import Domain.Product;

public class SupplierService {
    private int supplierId = 100;
    private int productId = 1000;

    public void createSupplier(String accountNumber, String supplierName,
                               String bankAccount, String bankNumber, String bankBranch, String ownerID,
                               String contactName, String contactPhoneNumber, String contactTitle) {
        //todo
    }

    //adds a new product to an existing supplier
    public void addNewProductToSupplier(String productName, int price) {
        //todo check if the product already exist if it doesnt create a new product id
    }

    //checks validity of the id of the supplier
    public boolean validIdSupplier(String supplierId) {
        return false;
        //todo
    }

    //prints all existing suppliers
    public void printAllSuppliers() {
        //todo
    }

    //prints the details of a specific supplier
    public void printSupplier(String supplierId) {
        //todo
    }
    //updates supplier phone number given supplierId
    public void updateSupplierPhoneNumber(String supplierId, String newPhoneNumber) {
        //todo
    }
    //updates supplier phone number given supplierId
    public void updateSupplierBankAccount(String supplierId, String newBankAccount, String newBankNumber,
                                          String newBankBranch
            , String ownerID) {
        //todo
    }
    //updates a supplier name given a supplier id
    public void updateName(String supplierId, String newName) {
        //todo
    }
}
