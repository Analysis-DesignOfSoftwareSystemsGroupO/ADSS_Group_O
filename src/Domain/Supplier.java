package Domain;

import DataBase.SuppliersDataBase;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private final String supplierID;
    private String supplierAccountNumber;
    private String supplierName;
    private Bank bank;
    private List<InformationContact> InformationContacts;
    private List<Product> supplyProducts;


    public Supplier(String ID, String accountNumber, String supplierName, String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (ID == null || accountNumber == null || supplierName == null || bankAccount == null || bankNumber == null || bankBranch == null || ownerID == null) {
            throw new NullPointerException();
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
        this.supplierID = ID;
        this.supplierAccountNumber = accountNumber;
        this.supplierName = supplierName;
        this.InformationContacts = new ArrayList<InformationContact>();
        this.supplyProducts = new ArrayList<Product>();
    }

    public Supplier(Supplier other) {
        this.supplierID = other.supplierID;
        this.supplierAccountNumber = other.supplierAccountNumber;
        this.supplierName = other.supplierName;
        this.bank = new Bank(this.getBank());
        this.InformationContacts = other.InformationContacts;
        this.supplyProducts = other.supplyProducts;
    }

    public String getID() {
        return supplierID;
    }

    public String getAccountNumber() {
        return supplierAccountNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setAccountNumber(String accountNumber) {
        this.supplierAccountNumber = accountNumber;
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
            throw new NullPointerException();
        }
        if (!InformationContacts.contains(informationContact)) {
            InformationContacts.add(informationContact);
        }
    }

    public void setNewBank(String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (bankAccount == null || bankNumber == null || bankBranch == null || ownerID == null) {
            throw new NullPointerException("Bank account or bank number or bank branch is null");
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
    }

    public List<InformationContact> getInformationContacts() {
        return InformationContacts;
    }

    @Override
    public String toString() {
        return "Supplier ID: " + supplierID + System.lineSeparator() + "Supplier Name: " +
                supplierName + System.lineSeparator() + "Account Number: " + supplierAccountNumber + System.lineSeparator();
    }
}