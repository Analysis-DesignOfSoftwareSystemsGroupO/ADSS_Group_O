package SupplierMoudle;

import DataBase.SuppliersDataBase;

public class Supplier {
    private final String supplierID;
    private String supplierAccountNumber;
    private String supplierName;
    private final Bank bank;

    public Supplier(String ID, String accountNumber, String supplierName, String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (ID == null || accountNumber == null || supplierName == null) {
            throw new NullPointerException();
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
        this.supplierID = ID;
        this.supplierAccountNumber = accountNumber;
        this.supplierName = supplierName;
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
