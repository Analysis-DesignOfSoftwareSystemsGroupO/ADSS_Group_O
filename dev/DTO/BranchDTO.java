package DTO;

public class BranchDTO {
    private String branchID;
    private String branchCity;
    private String branchAddress;

    public BranchDTO(String branchID, String branchCity, String branchAddress) {
        this.branchID = branchID;
        this.branchCity = branchCity;
        this.branchAddress = branchAddress;
    }

    public String getCity() {
        return branchCity;
    }

    public String getAddress() {
        return branchAddress;
    }
    public String getBranchID() {
        return branchID;
    }
}
