package Domain;

public class Branch {
    private String branchID;
    private String branchName;
    private String branchAddress;

    public Branch(String branchID,  String branchName, String branchAddress) {
        this.branchID = branchID;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    public String getBranchID() {
        return branchID;
    }
}
