package SupplierMoudle;

public class Bank {
    private final String bankAccount;
    private final String bankNumber;
    private final String bankBranch;
    private final String ownerID;

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
