package Domain;

public class Product {
    private String productID;
    private String productName;

    public Product(String productID, String productName) {
        this.productID = productID;
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }
    public String getProductName() {
        return productName;
    }

    public String toString(){
        return "Product ID: " + productID + ", Product Name: " + productName;
    }
}
