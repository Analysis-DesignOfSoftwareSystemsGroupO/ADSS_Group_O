package Domain;

public class SuppliedItem {
    private static int gsuppliedItemID = 0;
    private int suppliedItemPrice;
    private Product product;
    private int suppliedItemID;

    public SuppliedItem(int suppliedItemPrice, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Supplied item ID and product ID cannot be null");
        }
        if (suppliedItemPrice <= 0){
            throw new IllegalArgumentException("Price cant be negative");
        }
        this.suppliedItemID = gsuppliedItemID;
        gsuppliedItemID++;
        this.suppliedItemPrice = suppliedItemPrice;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getSuppliedItemID() {
        return suppliedItemID;
    }

    public int getSuppliedItemPrice() {
        return suppliedItemPrice;
    }

    public String toString(){
        return "Product Id: " + suppliedItemID +  product.toString();
    }
}
