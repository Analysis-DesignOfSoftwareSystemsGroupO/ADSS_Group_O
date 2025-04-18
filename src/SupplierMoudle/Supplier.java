package SupplierMoudle;

public class Supplier {
    private String ID;
    private String accountNumber;
    private String supplierName;
    private Bank bank;

    public Supplier(String ID, String accountNumber, String supplierName, String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        if (ID == null || accountNumber == null || supplierName == null) {
            throw new NullPointerException();
        }
        this.bank = new Bank(bankAccount, bankNumber, bankBranch, ownerID);
        this.ID = ID;
        this.accountNumber = accountNumber;
        this.supplierName = supplierName;
    }

    public String getID() {
        return ID;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getSupplierName() {
        return supplierName;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
