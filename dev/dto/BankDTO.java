package dto;

public class BankDTO {
    private final String bankAccount;
    private final String bankNumber;
    private final String bankBranch;
    private final String ownerID;

    public BankDTO(String bankAccount, String bankNumber, String bankBranch, String ownerID) {
        this.bankAccount = bankAccount;
        this.bankNumber = bankNumber;
        this.bankBranch = bankBranch;
        this.ownerID = ownerID;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public String getBankNumber() {
        return bankNumber;
    }
    public String getBankBranch() {
        return bankBranch;
    }
    public String getOwnerID() {
        return ownerID;
    }
}
