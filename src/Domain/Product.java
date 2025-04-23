package Domain;

public class Product {
    private final String productID;
    private final String productName;
    private final String productManufacturer;

    public Product(String productID, String productName, String productManufacturer) {
        if (productID == null || productName == null || productManufacturer == null) {
            throw new NullPointerException("Product Details cannot be null");
        }
        this.productID = productID;
        this.productName = productName;
        this.productManufacturer = productManufacturer;
    }

    public String getProductID() {
        return productID;
    }
    public String getProductName() {
        return productName;
    }

    public String getProductManufacturer() {
        return productManufacturer;
    }

    public String toString(){
        return "Product ID: " + productID + ", Product Name: " + productName + ", Product Manufacturer: " + productManufacturer;
    }
}
