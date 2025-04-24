package Service;

import DataBase.ProductDataBase;
import DataBase.SuppliersDataBase;
import Domain.*;

import java.util.List;


public class SupplierService {
    private final SuppliersDataBase suppliersDataBase = SuppliersDataBase.getInstance();
    private ProductDataBase productDataBase = ProductDataBase.getInstance();

    public void createSupplier(String supplierID, String supplierName, String supplierPaymentMethod,
                               String bankAccount, String bankNumber, String bankBranch,
                               String contactName, String contactPhoneNumber, String contactTitle) throws Exception {

            if (suppliersDataBase.getSupplier(supplierID) == null) {
                Supplier newSupplier = new Supplier(supplierID, supplierName, supplierPaymentMethod, bankAccount, bankNumber, bankBranch, contactName, contactPhoneNumber, contactTitle);
                if (newSupplier.getID() != null) {
                    suppliersDataBase.addSupplier(newSupplier);
                }
            }
            else{
                throw new Exception("Supplier already exists");
            }
    }

    //adds a new product to an existing supplier
    public void addNewProductToSupplier(String supplierId, String productId, String productName, int price, String manufacturer) {
        if (suppliersDataBase.getSupplier(supplierId) == null) {
            throw new NullPointerException("Supplier does not exist");
        }
        //add product to product database handles multiple products in the db
        Product p = new Product(productId,productName, manufacturer);
        productDataBase.addProduct(p);
        //add product to supplier
        suppliersDataBase.getSupplier(supplierId).addProduct(p, price);
    }

    //checks validity of the id of the supplier
    public boolean validIdSupplier(String supplierId) {
        return (suppliersDataBase.getSupplier(supplierId) != null);
    }

    //prints all existing suppliers
    public void printAllSuppliers() {
        List<Supplier> suppliers = suppliersDataBase.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            System.out.println(supplier.toString());
        }
    }

    //prints the details of a specific supplier //todo
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
    public void updateSupplierBankAccount(String supplierID, String newBankAccount, String newBankNumber, String newBankBranch) {
        if(suppliersDataBase.getSupplier(supplierID) != null) {
            try {
                Supplier supplier = suppliersDataBase.getSupplier(supplierID);
                supplier.setNewBank(newBankAccount, newBankNumber, newBankBranch, supplierID);
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

    public void addNewInfoContact(String supplierID, String contactName, String newPhoneNumber, String newTitle) {
        InformationContact infoContact = new InformationContact(contactName, newPhoneNumber, newTitle);
        if (infoContact.getContactName() != null) {
            Supplier supplier = suppliersDataBase.getSupplier(supplierID);
            supplier.addInformationContact(infoContact);
        }
    }

    public void getAllSupplierInformationContacts(String supplierID){
        Supplier supplier = suppliersDataBase.getSupplier(supplierID);
        List<InformationContact> infoContacts = supplier.getInformationContacts();
        for (InformationContact infoContact : infoContacts) {
            System.out.println(infoContact);
        }
    }

    public void deleteSupplier(String supplierID) {
        Supplier supplier = suppliersDataBase.getSupplier(supplierID);
        if (supplier == null) {
            throw new NullPointerException("Supplier not found");
        }
        List<Agreement> agreements = suppliersDataBase.getAllAgreement();
        for (Agreement agreement : agreements){
            if (agreement.getSupplierID().equals(supplierID)) {
                suppliersDataBase.removeAgreement(agreement.getBranchID(), agreement.getSupplierID());
            }
        }
        suppliersDataBase.removeSupplier(supplierID);
    }
}
