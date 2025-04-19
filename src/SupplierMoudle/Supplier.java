package SupplierMoudle;

import DataBase.SuppliersDataBase;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private final String supplierID;
    private String supplierAccountNumber;
    private String supplierName;
    private final Bank bank;
    public List<InformationContact> InformationContacts;


    public Supplier(String ID, String accountNumber, String supplierName, String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (ID == null || accountNumber == null || supplierName == null) {
            throw new NullPointerException();
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
        this.supplierID = ID;
        this.supplierAccountNumber = accountNumber;
        this.supplierName = supplierName;
        this.InformationContacts = new ArrayList<InformationContact>();
    }

    public Supplier(Supplier other){
        this.supplierID = other.supplierID;
        this.supplierAccountNumber = other.supplierAccountNumber;
        this.supplierName = other.supplierName;
        this.bank = new Bank(this.getBank());
        this.InformationContacts = other.InformationContacts;
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

    public List<InformationContact> getInformationContacts() {
        return InformationContacts;
    }
}