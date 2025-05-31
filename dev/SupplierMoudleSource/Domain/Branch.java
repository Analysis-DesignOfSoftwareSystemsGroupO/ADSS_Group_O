package SupplierMoudleSource.Domain;

import SupplierMoudleSource.DTO.BranchDTO;

public class Branch {
    private String branchID;
    private String branchCity;
    private String branchAddress;

    public Branch(String branchID,  String branchCity, String branchAddress) {
        this.branchID = branchID;
        this.branchCity = branchCity;
        this.branchAddress = branchAddress;
    }
    public Branch(BranchDTO branchDTO) {
        this.branchID = branchDTO.getBranchID();
        this.branchAddress = branchDTO.getAddress();
        this.branchCity = branchDTO.getCity();
    }

    public BranchDTO getBranchDTO(){
        return new BranchDTO(branchID,branchCity,branchAddress);
    }

    public String getBranchID() {
        return branchID;
    }
    public String getBranchAddress() {
        return branchAddress;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public String toString(){
        return "Branch Id: " + branchID + ", Branch Name: " + branchCity + ", Branch Address" + branchAddress;
    }
}