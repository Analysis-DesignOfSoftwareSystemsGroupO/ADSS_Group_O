package SupplierMoudle;

public class Agreement {
    private String agreementID;

    public Agreement(Branch b, Supplier supplier) {
        if (b == null || supplier == null) {
            throw new NullPointerException();
        }

    }
}
