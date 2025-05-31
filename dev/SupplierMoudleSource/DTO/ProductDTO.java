package SupplierMoudleSource.DTO;

public class ProductDTO {
    public final String productID;
    public final String productName;
    public final String productManufacturer;
    public final int shelfLifeDays;

    public ProductDTO(String productID, String productName, String productManufacturer, int shelfLifeDays) {
        this.productID = productID;
        this.productName = productName;
        this.productManufacturer = productManufacturer;
        this.shelfLifeDays = shelfLifeDays;
    }
}
