package DataBase;

import Domain.*;

import java.util.*;

public class SuppliersDataBase {
    private Map<String, Supplier> suppliers;
    private Map<SupplierBranchKey, Agreement> suppliersAgreements;


    //singleton database
    private static SuppliersDataBase suppliersDataBase = null;
    public static SuppliersDataBase getInstance() {
        if (suppliersDataBase == null) {
            suppliersDataBase = new SuppliersDataBase();
        }
        return suppliersDataBase;
    }
    private SuppliersDataBase(){
        suppliersAgreements = new HashMap<>();
        suppliers = new HashMap<>();
    }


    /**
     *Supplier Data Base Functions:
     */
    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getID(), supplier);
    }

    public Supplier getSupplier(String supplierID) {
        if (supplierID == null){
            throw new NullPointerException("supplier does not exist");
        }
        if (!suppliers.containsKey(supplierID)) {
            return null;
        }
        return suppliers.get(supplierID);
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<>(suppliers.values());
    }

    /*
        agreement functions
     */

    public void removeAgreement(String branchId, String supplierID){
        for (SupplierBranchKey branchKey : suppliersAgreements.keySet()){
            if (Objects.equals(branchKey.branchID, branchId) && Objects.equals(branchKey.supplierID, supplierID)){
                suppliersAgreements.remove(branchKey);
                return;
            }
        }
        throw new NullPointerException("Agreement does not exist");
    }

    public void removeSupplier(String supplierID){
        if (suppliers.containsKey(supplierID)){
            suppliers.remove(supplierID);
        }
        else {
            throw new NullPointerException("Supplier does not exist");
        }
    }

    public void addAgreement(Agreement agreement) {
        SupplierBranchKey supBKey = new SupplierBranchKey(agreement.getSupplierID(), agreement.getBranchID());
        suppliersAgreements.put(supBKey, agreement);
    }

    //return an agreement
    public Agreement getAgreement(String branchID, String supplierID) {
        if (supplierID == null || branchID == null) {
            throw new NullPointerException();
        }
        for(SupplierBranchKey key : suppliersAgreements.keySet()){
            if (Objects.equals(key.getBranchID(), branchID) && Objects.equals(key.getSupplierID(), supplierID)) {
                return suppliersAgreements.get(key);
            }
        }
        return null;
}

    //add product to agreement
    public void addProductToAgreement(SuppliedItem product, String branchID, String supplierID){
        Agreement agreement = this.getAgreement(branchID, supplierID);
        if (agreement == null){
            throw new NullPointerException("agreement does not exist for Branch:" + branchID + " And Supplier:" + supplierID);

        }
        agreement.addItem(product);
    }

    public void addDiscountToAgreement(String branchID, String supplierID, Discount discount){
        Agreement agreement = this.getAgreement(branchID, supplierID);
        if (agreement == null){
            throw new NullPointerException("agreement does not exist");
        }
        agreement.addDiscount(discount);
    }


    //return a copy of all agreement
    public List<Agreement> getAllAgreement(){
        return new ArrayList<>(suppliersAgreements.values());
    }


    public static class SupplierBranchKey{
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

        public boolean equalsKey(String supplierID, String branchID) {
            return this.supplierID.equals(supplierID) && this.branchID.equals(branchID);
        }
    }
}