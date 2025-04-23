package Domain;

import DataBase.SuppliersDataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supplier {
    private String supplierID;
    private String supplierName;
    private Bank bank;
    private PaymentMethod paymentMethod;
    private List<InformationContact> informationContacts;
    private HashMap<String, SuppliedItem> supplyProducts;
    private int supplied_product_id = 0;


    public Supplier(String ID, String supplierName, String paymentMethod, String bankAccount, String bankNumber, String bankBranch, String infoContactName, String infoContactPhoneNumber, String infoContactTitle) {
        if (ID == null || supplierName == null || bankAccount == null || bankNumber == null || bankBranch == null || paymentMethod == null) {
            throw new NullPointerException("Invalid Details");
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ID);
        PaymentMethod payMethod = new PaymentMethod(paymentMethod);
        if(payMethod.getPaymentMethodName() == null){
            throw new NullPointerException("Invalid Payment Method");
        }
        this.paymentMethod = payMethod;
        this.informationContacts = new ArrayList<InformationContact>();
        InformationContact newInfoContact = new InformationContact(infoContactName, infoContactPhoneNumber, infoContactTitle);
        this.informationContacts.add(newInfoContact);
        this.supplierID = ID;
        this.supplierName = supplierName;
        this.supplyProducts = new HashMap<String, SuppliedItem>();
    }

    public Supplier(Supplier other) {
        this.supplierID = other.supplierID;
        this.supplierName = other.supplierName;
        this.paymentMethod = other.paymentMethod;
        this.bank = new Bank(this.getBank());
        this.informationContacts = other.informationContacts;
        this.supplyProducts = other.supplyProducts;
        this.supplied_product_id = other.supplied_product_id;
    }

    public String getID() {
        return supplierID;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public int getSupplied_product_id() {
        return supplied_product_id++;
    }

    public void setSupplierName(String supplierName) {
        if (supplierName == null || supplierName.isEmpty()) {
            throw new NullPointerException("Supplier Name cannot be null or empty");
        }
        this.supplierName = supplierName;
    }

    public Bank getBank() {
        return bank;
    }

    public void addInformationContact(InformationContact informationContact) {
        if (informationContact == null) {
            throw new NullPointerException("informationContact cannot be null");
        }
        if (!informationContacts.contains(informationContact)) {
            informationContacts.add(informationContact);
        }
    }

    public void setNewBank(String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (bankAccount == null || bankNumber == null || bankBranch == null || ownerID == null) {
            throw new NullPointerException("Bank account or bank number or bank branch is null");
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
    }

    public List<InformationContact> getInformationContacts() {
        return informationContacts;
    }

    @Override
    public String toString() {
        return "Supplier ID: " + supplierID + System.lineSeparator() + "Supplier Name: " +
                supplierName + System.lineSeparator() + System.lineSeparator();
    }

    public void addProduct(Product product, int price) {
        if (!supplyProducts.containsKey(product.getProductID())){
            SuppliedItem suppliedItem = new SuppliedItem(price, product);
            this.supplyProducts.put(product.getProductID(), suppliedItem);
        }
        throw new NullPointerException("Supplied Product already exists");
    }

    public SuppliedItem getProduct(String pID){
        return this.supplyProducts.get(pID);
    }

    public String getPaymentMethod(){
        return this.paymentMethod.getPaymentMethodName();
    }

    public void setPaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new NullPointerException("Payment Method cannot be null or empty");
        }
        try{
            this.paymentMethod = new PaymentMethod(paymentMethod);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}