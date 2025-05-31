package SupplierMoudleSource.Domain;

import SupplierMoudleSource.DTO.BankDTO;

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
    public Bank(BankDTO bankDTO) {
        this.bankAccount = bankDTO.getBankAccount();
        this.bankNumber = bankDTO.getBankNumber();
        this.bankBranch = bankDTO.getBankBranch();
        this.ownerID = bankDTO.getOwnerID();
    }

    public Bank(Bank other) {
        this.bankAccount = other.bankAccount;
        this.bankNumber = other.bankNumber;
        this.bankBranch = other.bankBranch;
        this.ownerID = other.ownerID;
    }


    public String getOwnerID() {
        return ownerID;
    }


    public String toString() {
        return "Bank Account number: " + bankAccount + ", Bank Number: " + bankNumber + ", Bank Branch Number: " + bankBranch;
    }

    public BankDTO getBankDTO(){
        return new BankDTO(bankAccount, bankNumber, bankBranch, ownerID);
    }
}