package SupplierMoudleSource.Domain;

public class SuppliedItem {
    private final int suppliedItemPrice;
    private final Product product;
    private final String suppliedItemID;

    public SuppliedItem(int suppliedItemPrice, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Supplied item ID and product ID cannot be null");
        }
        if (suppliedItemPrice < 0){
            throw new IllegalArgumentException("price cant be negative");
        }
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = product;
        this.suppliedItemID = product.getProductID();
    }

    public Product getProduct() {
        return product;
    }

    public String getSuppliedItemID() {
        return suppliedItemID;
    }

    public int getSuppliedItemPrice() {
        return suppliedItemPrice;
    }

    public String toString(){
        return product.toString();
    }
}
