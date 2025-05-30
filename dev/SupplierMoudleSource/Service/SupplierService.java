package SupplierMoudleSource.Service;

import DTO.*;
import SupplierMoudleSource.Repository.AgreementRepository;
import SupplierMoudleSource.Repository.ProductRepository;
import SupplierMoudleSource.Repository.SupplierRepository;
import SupplierMoudleSource.Domain.*;

import java.util.List;
import java.util.Objects;


public class SupplierService {
    private final SupplierRepository supplierRepository = SupplierRepository.getInstance();
    private ProductRepository productRepository = ProductRepository.getInstance();
    private AgreementRepository agreementRepository = AgreementRepository.getInstance();

    //this method creates a supplier
    public void createSupplier(String supplierName, String supplierPaymentMethod,
                               String bankAccount, String bankNumber, String bankBranch,
                               String contactName, String contactPhoneNumber, String contactTitle, String deliveryWay) throws Exception {


        supplierRepository.addSupplier(supplierName, new PaymentMethod(supplierPaymentMethod),
                new BankDTO(bankAccount, bankNumber, bankBranch, ""), new InformationContactDTO(contactName, contactPhoneNumber, contactTitle), new Delivery(deliveryWay));

    }

    //adds a new product to an existing supplier
    public void addNewProductToSupplier(String supplierId, String productName, String manufacturer, int price, int shelfLife) throws Exception {
        if (supplierRepository.getSupplier(supplierId) == null) {
            throw new NullPointerException("Supplier does not exist");
        }
        //add product to product database handles multiple products in the db

        String productId = productRepository.addProduct(productName, manufacturer, shelfLife);
        //add product to supplier
        supplierRepository.addProduct(new SuppliedItemDTO(price, new ProductDTO(productId, productName, manufacturer, shelfLife)), supplierId);
    }

    //checks validity of the id of the supplier
    public boolean validIdSupplier(String supplierId) throws Exception {
        return (supplierRepository.getSupplier(supplierId) != null);
    }

    //prints all existing suppliers
    public void printAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.getAllSuppliers();
        if(suppliers.isEmpty()) {
            throw new NullPointerException("No Suppliers in System");
        }
        for (Supplier supplier : suppliers) {
            System.out.println("*********************************************************");
            System.out.println(supplier.toString());
        }
        System.out.println("*********************************************************");
    }

    //prints the details of a specific supplier //todo
    public void printSupplier(String supplierId) throws Exception {
        if (supplierRepository.getSupplier(supplierId) != null) {
            System.out.println(new Supplier(supplierRepository.getSupplier(supplierId)).toString());
            return;
        }
        throw new Exception("Supplier doesn't exist");
    }



    //updates supplier phone number given supplierId
    public void updateSupplierInformationContact(String supplierId, String contactName, String newTitle, String newPhoneNumber) throws Exception {
        SupplierDTO supplier1 = supplierRepository.getSupplier(supplierId);
        if (supplier1 == null) {
            throw new NullPointerException("Supplier does not exist");
        }
        Supplier supplier = new Supplier(supplier1);
        for (InformationContact infoContact : supplier.getInformationContacts()) {
            if (infoContact.getContactName().equals(contactName)) {
                infoContact.setContactPhone(newPhoneNumber);
                infoContact.setTitle(newTitle);
                supplierRepository.editInformationCotact(supplierId, new InformationContactDTO(contactName, newTitle, newPhoneNumber));
                return;
            }
        }
        throw new NullPointerException("Contact does not exist");
    }

    //updates supplier phone number given supplierId
    public void updateSupplierBankAccount(String supplierID, String newBankAccount, String newBankNumber, String newBankBranch) throws Exception {
        supplierRepository.editBankInformation(supplierID, new BankDTO(newBankAccount, newBankNumber, newBankBranch, supplierID));
    }

    //updates a supplier name given a supplier id
    public void updateSupplierName(String supplierID, String newName) throws Exception {
        supplierRepository.updateSupplierName(supplierID, newName);
    }

    public void addNewInformationContact(String supplierID, String contactName, String newPhoneNumber, String newTitle) throws Exception {
        supplierRepository.addNewInformationContact(supplierID, new InformationContactDTO(contactName, newPhoneNumber, newTitle));

    }



    public void printAllInformationContacts(String supplierID) throws Exception {
        SupplierDTO supplier = supplierRepository.getSupplier(supplierID);
        if (supplier == null) {
            throw new NullPointerException("Supplier doesn't exists");
        }
        List<InformationContactDTO> infoContacts = supplier.getInformationContacts();
        for (InformationContactDTO infoContact : infoContacts) {
            InformationContact informationContact = new InformationContact(infoContact);
            System.out.println(informationContact.toString());
        }
    }

    public void deleteSupplier(String supplierID) throws Exception {
        SupplierDTO supplier = supplierRepository.getSupplier(supplierID);
        if (supplier == null) {
            throw new NullPointerException("Supplier not found");
        }
        agreementRepository.removeAgreementOfSupplierLocally(supplierID);
        supplierRepository.removeSupplier(supplierID);
    }

    public void updateDeliveryMethod(String supplierId, String deliveryWay) throws Exception {

        supplierRepository.editDeliveryMethod(supplierId, new Delivery(deliveryWay));
    }

    public void viewInformationContacts(String id) throws Exception {
        SupplierDTO supplier = supplierRepository.getSupplier(id);
        if (supplier == null) {
            throw new NullPointerException("Supplier not found");
        }
        for (InformationContactDTO infoContact : supplier.getInformationContacts()) {
            InformationContact informationContact = new InformationContact(infoContact);
            System.out.println(informationContact.toString());
        }
    }
}