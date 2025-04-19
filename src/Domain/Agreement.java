package SupplierMoudle;

import java.util.List;

public class Agreement {
    public String agreementID;
    private Supplier supplier;
    private Branch Branch;
    public List<SuppliedItem> supplierItemsList;
    public List<Discount> discounts;

    public Agreement(Branch branch, Supplier supplier) {
        if (branch == null || supplier == null) {
            throw new NullPointerException();
        }

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
