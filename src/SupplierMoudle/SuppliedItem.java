package SupplierMoudle;

public class SuppliedItem {
    private String suppliedItemID;
    private int suppliedItemPrice;
    private Product product;

    public SuppliedItem(String suppliedItemID, int suppliedItemPrice, String productID, String productName) {
        if (suppliedItemID == null || suppliedItemPrice == 0 || productID == null) {
            throw new IllegalArgumentException("Supplied item ID and product ID cannot be null");
        }
        this.suppliedItemID = suppliedItemID;
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = new Product(productID, productName);
    }
}
