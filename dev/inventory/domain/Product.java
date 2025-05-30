package inventory.domain;


import java.util.Objects;
import java.util.UUID;

/**
 * Represents a product in the inventory system. (catalog item)
 * Holds information common to all items of this type.
 */
public class Product {
    private String id;
    private String name;
    private String manufacturer;
    private double sellingPrice;
    private int minimumStockLevel;
    private String categoryGroupId;
    private String location;
    private boolean storeDiscountActive;
    private boolean manufacturerDiscountActive;

    /**
     * Constructor for Product
     *
     * @param name              The display name of the product. (cannot be null)
     * @param minimumStockLevel The minimum amount of this product that should be in stock. (cannot be negative)
     */
    public Product(String name, int minimumStockLevel,Double sellingPrice, String location,
                   String manufacturer,String categoryGroupId) {
        Objects.requireNonNull(name, "Product name cannot be null");
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.manufacturer = manufacturer;
        this.minimumStockLevel = minimumStockLevel;
        this.sellingPrice = sellingPrice;
        this.location = location;
        this.categoryGroupId = categoryGroupId;
    }

    public Product(String id, String name, String manufacturer, int minimumStockLevel,
                   String location, String categoryGroupId, Double sellingPrice) {
        Objects.requireNonNull(id, "Product ID cannot be null");
        Objects.requireNonNull(name, "Product name cannot be null");
        Objects.requireNonNull(manufacturer, "Manufacturer cannot be null");
        Objects.requireNonNull(location, "Location cannot be null");
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.minimumStockLevel = minimumStockLevel;
        this.location = location;
        this.categoryGroupId = categoryGroupId;
        this.sellingPrice = sellingPrice;
    }


    public void setMinimumStockLevel(int minimumStockLevel) {
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        this.minimumStockLevel = minimumStockLevel;
    }

    public boolean getStoreDiscountActive() {
        return manufacturerDiscountActive;
    }

    public boolean getManufacturerDiscountActive() {
        return manufacturerDiscountActive;
    }

    public void setStoreDiscountActive(boolean discountActive) {
        this.storeDiscountActive = discountActive;
    }

    public void setSellingPrice(double sellingPrice) {
        if (sellingPrice < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative");
        }
        this.sellingPrice = sellingPrice;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }


    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public String getCategoryGroupId() {
        return categoryGroupId;
    }

    public void setCategoryGroupId(String categoryGroupId) {}


    @Override
    public String toString() {
        String res = "---------- Product: " + name +  " ----------" +
                "\n\tid = '" + id + "'," +
                "\n\tmanufacturer = '" + manufacturer + "'," +
                "\n\tminimumStockLevel = " + minimumStockLevel + "'," +
                "\n\tcategoryId = '" + categoryGroupId + "'" +
                "\n\tlocation = '" + location + "'" +
                "\n\tSelling Price = " + sellingPrice;


        res += "\n ------------------------------------\n";
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }



}
