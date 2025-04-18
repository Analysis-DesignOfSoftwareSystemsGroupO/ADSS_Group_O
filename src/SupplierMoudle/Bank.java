package SupplierMoudle;

public class Bank {
    private String bankAccount;
    private String bankNumber;
    private String bankBranch;
    private String ownerID;

    public Bank(String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        this.bankAccount = bankAccount;
        this.bankNumber = bankNumber;
        this.bankBranch = bankBranch;
        this.ownerID = ownerID;
    }

    public String getBankInfo() {
        return bankAccount + " " + bankNumber + " " + bankBranch + " " + ownerID;
    }

    public String getOwnerID() {
        return ownerID;
    }
}
