package Domain;

public class SuppliedItem {
    public String suppliedItemID;
    public int suppliedItemPrice;
    private Product product;

    public SuppliedItem(String suppliedItemID, int suppliedItemPrice, Product product) {
        if (suppliedItemID == null || suppliedItemPrice == 0 || product == null) {
            throw new IllegalArgumentException("Supplied item ID and product ID cannot be null");
        }
        this.suppliedItemID = suppliedItemID;
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
