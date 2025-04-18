package DataBase;

public class SupplierBranchKey{
    private String supplierID;
    private String branchID;

    public SupplierBranchKey(String supplierID, String branchID) {
        this.supplierID = supplierID;
        this.branchID = branchID;
    }
    public String getSupplierID() {
        return supplierID;
    }
    public String getBranchID() {
        return branchID;
    }
}
