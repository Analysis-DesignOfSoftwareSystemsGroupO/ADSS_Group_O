package SupplierMoudle;

import java.util.ArrayList;
import java.util.List;

public class Agreement {
    public String agreementID;
    private final Supplier supplier;
    private final Branch Branch;
    public List<SuppliedItem> supplierItemsList;
    public List<Discount> discounts;

    public Agreement(Branch branch, Supplier supplier) {
        if (branch == null || supplier == null) {
            throw new NullPointerException();
        }
        this.supplier = supplier;
        this.Branch = branch;
        this.supplierItemsList = new ArrayList<SuppliedItem>();
        this.discounts = new ArrayList<Discount>();

    }

    public Agreement(Agreement other) {
        this.agreementID = other.agreementID;
        this.supplier = new Supplier(other.supplier);
        this.Branch = other.Branch;
        this.supplierItemsList = other.supplierItemsList;
        this.discounts = other.discounts;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void addItem(SuppliedItem suppliedItem) {
        if (suppliedItem == null) {
            throw new NullPointerException();
        }
        supplierItemsList.add(suppliedItem);
    }


}
