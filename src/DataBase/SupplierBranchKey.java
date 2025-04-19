package DataBase;

public class SupplierBranchKey{
    private final String supplierID;
    private final String branchID;

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
