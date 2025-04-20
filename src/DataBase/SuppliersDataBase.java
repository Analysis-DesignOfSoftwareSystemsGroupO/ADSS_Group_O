package DataBase;

import Domain.Agreement;
import Domain.Branch;
import Domain.Supplier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getSupplierName(), supplier);
    }
    public Supplier getSupplier(String supplierID) {
        if (supplierID == null){
            throw new NullPointerException();
        }
        if (!suppliers.containsKey(supplierID)) {
            return null;
        }
        return new Supplier(suppliers.get(supplierID));
    }

    public void addAgreement(String supplierID, Branch branch, Agreement agreement) {
        Supplier supplier = suppliers.get(supplierID);
        if (supplier == null || branch == null) {
            return;
        }
        SupplierBranchKey supBKey = new SupplierBranchKey(supplier.getID(), branch.getBranchID());
        suppliersAgreements.put(supBKey, agreement);
    }

    public Agreement getAgreement(String branchID, String supplierID) {
        if (supplierID == null || branchID == null) {
            throw new NullPointerException();
        }
        for(SupplierBranchKey key : suppliersAgreements.keySet()){
            if (Objects.equals(key.getBranchID(), branchID) && Objects.equals(key.getSupplierID(), supplierID)) {
                return new Agreement(suppliersAgreements.get(key));
            }
        }
        return null;
    }

}