package dto;

public class ProductDTO {
    public final String productID;
    public final String productName;
    public final String productManufacturer;
    private final int shelflifedays;

    public ProductDTO(String productID, String productName, String productManufacturer, int shelflifedays) {
        this.productID = productID;
        this.productName = productName;
        this.productManufacturer = productManufacturer;
        this.shelflifedays = shelflifedays;
    }
}
