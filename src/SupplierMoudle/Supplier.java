package SupplierMoudle;

public class Supplier {
    private String ID;
    private String accountNumber;
    private String supplierName;

    public Supplier(String ID, String accountNumber, String supplierName) {
        if (ID == null || accountNumber == null || supplierName == null) {
            throw new NullPointerException();
        }
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
