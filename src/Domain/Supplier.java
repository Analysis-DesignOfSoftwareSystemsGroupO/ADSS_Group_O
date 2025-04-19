package Domain;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private final String supplierID;
    private String supplierAccountNumber;
    private String supplierName;
    private final Bank bank;
    private List<InformationContact> contacts;

    public Supplier(String ID, String accountNumber, String supplierName, String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (ID == null || accountNumber == null || supplierName == null) {
            throw new NullPointerException();
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
        if (bankAccount == null|| bankNumber == null || bankBranch == null || ownerID == null) {
            throw new NullPointerException();
        }
        this.supplierID = ID;
        this.supplierAccountNumber = accountNumber;
        this.supplierName = supplierName;
    }

    public void AddContacts(InformationContact contacts) {
        if (this.contacts == null) {
            this.contacts = new ArrayList<>();
        }
        if (contacts == null) {
            throw new NullPointerException();
        }
        this.contacts.add(contacts);
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
}
