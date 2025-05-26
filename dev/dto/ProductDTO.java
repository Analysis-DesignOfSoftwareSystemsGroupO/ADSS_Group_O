package dto;

public class ProductDTO {
    public final String productID;
    public final String productName;
    public final String productManufacturer;

    public ProductDTO(String productID, String productName, String productManufacturer) {
        this.productID = productID;
        this.productName = productName;
        this.productManufacturer = productManufacturer;
    }
}
