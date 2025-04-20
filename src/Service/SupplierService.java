package Service;

import DataBase.SuppliersDataBase;
import Domain.*;



public class SupplierService {
    private static SuppliersDataBase suppliersDataBase = new SuppliersDataBase();
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
        return suppliersDataBase.suppliers.containsKey(supplierId);
    }

    //prints all existing suppliers
    public void printAllSuppliers() {
        for (Supplier supplier : suppliersDataBase.suppliers.values()) {
            System.out.println(supplier);
        }
    }

    //prints the details of a specific supplier
    public void printSupplier(String supplierId) {
        if (suppliersDataBase.suppliers.containsKey(supplierId)) {
            System.out.println(suppliersDataBase.suppliers.get(supplierId));
        }
    }
    //updates supplier phone number given supplierId
    public void updateSupplierInfoContactPhoneNumber(String supplierId, String contactName, String newPhoneNumber) {
        Supplier supplier = suppliersDataBase.suppliers.get(supplierId);
        if (supplier != null) {
            for (InformationContact infoContact : supplier.InformationContacts){
                if (infoContact.getContactName().equals(contactName)) {
                    infoContact.setContactPhone(newPhoneNumber);
                }
            }
        }
    }
    //updates supplier phone number given supplierId
    public void updateSupplierBankAccount(String supplierID, String newBankAccount, String newBankNumber,
                                          String newBankBranch
            , String ownerID) {
        if(suppliersDataBase.suppliers.containsKey(supplierID)) {
            try {
                Supplier supplier = suppliersDataBase.suppliers.get(supplierID);
                supplier.setNewBank(newBankAccount, newBankNumber, newBankBranch, ownerID);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //updates a supplier name given a supplier id
    public void updateSupplierName(String supplierId, String newName) {
        Supplier supplier = suppliersDataBase.suppliers.get(supplierId);
        if (supplier != null) {
            supplier.setSupplierName(newName);
        }
   }
}
