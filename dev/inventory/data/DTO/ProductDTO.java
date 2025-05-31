package inventory.data.DTO;

import inventory.domain.Category;

public class ProductDTO {
    //TODO: change the cost price thing
    public String id;
    public String name;
    public String manufacturer;
    public double costPrice;
    public double sellingPrice;
    public double discountCostPrice;
    public double discountSellingPrice;
    public int minimumStockLevel;
    public String location;
    public Category category; //TODO: maybe change to categoryGroupID ? ?
    public boolean storeDiscountActive;
    public boolean manufacturerDiscountActive;

    public ProductDTO(String id, String name, String manufacturer, double costPrice, double sellingPrice,
                      double discountCostPrice, double discountSellingPrice, String location, Category category,
                      int minimumStockLevel, boolean storeDiscountActive, boolean manufacturerDiscountActive) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.discountCostPrice = discountCostPrice;
        this.discountSellingPrice = discountSellingPrice;
        this.location = location;
        this.category = category;
        this.minimumStockLevel = minimumStockLevel;
        this.storeDiscountActive = storeDiscountActive;
        this.manufacturerDiscountActive = manufacturerDiscountActive;
    }
}
