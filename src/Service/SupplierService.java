package Service;

import DataBase.SuppliersDataBase;
import Domain.*;

import java.util.List;


public class SupplierService {
    private static final SuppliersDataBase suppliersDataBase = SuppliersDataBase.getInstance();
    private int supplierId = 100;
    private int productId = 1000;

    public void createSupplier(String accountNumber, String supplierName,
                               String bankAccount, String bankNumber, String bankBranch, String ownerID,
                               String contactName, String contactPhoneNumber, String contactTitle) {
        try {
            String ID = String.valueOf(supplierId++);
            Supplier newSupplier = new Supplier(ID, accountNumber, supplierName, bankAccount, bankNumber, bankBranch, ownerID);
            suppliersDataBase.addSupplier(newSupplier);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //adds a new product to an existing supplier
    public void addNewProductToSupplier(String productName, int price) {
        //todo check if the product already exist if it doesnt create a new product id
    }

    //checks validity of the id of the supplier
    public boolean validIdSupplier(String supplierId) {
        return (suppliersDataBase.getSupplier(supplierId) != null);
    }

    //prints all existing suppliers
    public void printAllSuppliers() {
        List<Supplier> suppliers = suppliersDataBase.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            System.out.println(supplier);
        }
    }

    //prints the details of a specific supplier
    public void printSupplier(String supplierId) {
        if (suppliersDataBase.getSupplier(supplierId) != null) {
            System.out.println(suppliersDataBase.getSupplier(supplierId));
        }
    }

    //updates supplier phone number given supplierId
    public void updateSupplierInfoContactPhoneNumber(String supplierId, String contactName, String newTitle, String newPhoneNumber) {
        Supplier supplier = suppliersDataBase.getSupplier(supplierId);
        if (supplier != null) {
            for (InformationContact infoContact : supplier.getInformationContacts()){
                if (infoContact.getContactName().equals(contactName)) {
                    infoContact.setContactPhone(newPhoneNumber);
                    infoContact.setTitle(newTitle);
                    break;
                }
            }
        }
    }

    //updates supplier phone number given supplierId
    public void updateSupplierBankAccount(String supplierID, String newBankAccount, String newBankNumber, String newBankBranch, String ownerID) {
        if(suppliersDataBase.getSupplier(supplierID) != null) {
            try {
                Supplier supplier = suppliersDataBase.getSupplier(supplierID);
                supplier.setNewBank(newBankAccount, newBankNumber, newBankBranch, ownerID);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //updates a supplier name given a supplier id
    public void updateSupplierName(String supplierID, String newName) {
        Supplier supplier = suppliersDataBase.getSupplier(supplierID);
        if (supplier != null) {
            supplier.setSupplierName(newName);
        }
    }
    
}
