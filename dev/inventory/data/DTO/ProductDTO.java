package inventory.data.DTO;

import inventory.domain.Category;

public class ProductDTO {
    private String id;
    private String name;
    private String manufacturer;
    private double sellingPrice;
    private int minimumStockLevel;
    private String categoryGroupId;
    private String location;
    private boolean storeDiscountActive;
    private boolean manufacturerDiscountActive;


    public ProductDTO(String id, String name, String manufacturer, double sellingPrice
            , String location, int minimumStockLevel, boolean storeDiscountActive, boolean manufacturerDiscountActive) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.sellingPrice = sellingPrice;
        this.location = location;
        this.minimumStockLevel = minimumStockLevel;
        this.storeDiscountActive = storeDiscountActive;
        this.manufacturerDiscountActive = manufacturerDiscountActive;
    }
}
