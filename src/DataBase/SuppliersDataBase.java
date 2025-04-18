package DataBase;

import SupplierMoudle.Agreement;
import SupplierMoudle.Branch;
import SupplierMoudle.Supplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuppliersDataBase {
    public Map<String, Supplier> suppliers;
    public Map<SupplierBranchKey, Agreement> suppliersAgreements;

    public SuppliersDataBase() {
        suppliers = new HashMap<>();
        suppliersAgreements = new HashMap<>();
    }

    public void addSupplier(Supplier supplier) {
        suppliers.put(supplier.getSupplierName(), supplier);
    }
    public Supplier getSupplier(String supplierID) {
        return suppliers.get(supplierID);
    }

    public void addAgreement(String supplierID, Branch branch, Agreement agreement) {
        Supplier supplier = suppliers.get(supplierID);
        if (supplier == null || branch == null) {
            return;
        }
        SupplierBranchKey supBKey = new SupplierBranchKey(supplier.getID(), branch.getBranchID());
        suppliersAgreements.put(supBKey, agreement);
    }

}