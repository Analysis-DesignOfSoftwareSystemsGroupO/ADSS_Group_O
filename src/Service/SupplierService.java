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
    public boolean validIdSupplier(String ID) {
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

    public void updateSupplierPhoneNumber(String ID, String phoneNumber) {
        //todo
    }

    public void updateSupplierBankAccount(String ID, String bankAccount, String bankNumber, String bankBranch
            , String ownerID) {
        //todo
    }

    public void updateName(String ID, String name) {
        //todo
    }
}
