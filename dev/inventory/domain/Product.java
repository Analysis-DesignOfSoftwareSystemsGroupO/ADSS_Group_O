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
    private double costPrice;
    private double sellingPrice;
    private double discountCostPrice;
    private double discountSellingPrice;
    private int minimumStockLevel;
    private Category category;
    private String location;
    private boolean storeDiscountActive;
    private boolean manufacturerDiscountActive;
//    private Discount discount;

    /**
     * Constructor for Product
     *
     * @param name              The display name of the product. (cannot be null)
     * @param minimumStockLevel The minimum amount of this product that should be in stock. (cannot be negative)
     */
    public Product(String name, int minimumStockLevel, double costPrice, String location, String manufacturer) {
        Objects.requireNonNull(name, "Product name cannot be null");
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.manufacturer = manufacturer;
        this.minimumStockLevel = minimumStockLevel;
        this.costPrice = costPrice;
        this.sellingPrice = costPrice * 1.2;// set Default selling price to 20% more than cost price
        this.manufacturerDiscountActive = false;
        this.storeDiscountActive = false;
        this.location = location;
        this.discountCostPrice = 0;
        this.discountSellingPrice = 0;
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

    public void setManufacturerDiscountActive(boolean discountActive) {
        this.manufacturerDiscountActive = discountActive;
    }

    public void setCostPrice(double costPrice) {
        if (costPrice < 0) {
            throw new IllegalArgumentException("Cost price cannot be negative");
        }
        this.costPrice = costPrice;
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

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public int getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public Category getCategory() {
        return category;
    }

    public double getDiscountCostPrice() {
        return discountCostPrice;
    }

    public double getDiscountSellingPrice() {
        return discountSellingPrice;
    }

    public void setDiscountCostPrice(double discountCostPrice) {
        if (discountCostPrice < 0) {
            throw new IllegalArgumentException("Discount cost price cannot be negative");
        }
        this.discountCostPrice = discountCostPrice;
    }

    public void setDiscountSellingPrice(double discountSellingPrice) {
        if (discountSellingPrice < 0) {
            throw new IllegalArgumentException("Discount selling price cannot be negative");
        }
        this.discountSellingPrice = discountSellingPrice;
    }

    @Override
    public String toString() {
        String res = "---------- Product: " + name +  " ----------" +
                "\n\tid = '" + id + "'," +
                "\n\tmanufacturer = '" + manufacturer + "'," +
                "\n\tminimumStockLevel = " + minimumStockLevel + "'," +
                "\n\tcategory = '" + category.getName() + "'" +
                "\n\tlocation = '" + location + "'";
        if (storeDiscountActive && manufacturerDiscountActive) {
            res += "\n\tCost Price = " + costPrice +
                    "\n\tDiscount Cost Price = " + discountCostPrice +
                    "\n\tSelling Price = " + sellingPrice +
                    "\n\tDiscount Selling Price = " + discountSellingPrice;
        } else if (storeDiscountActive) {
            res += "\n\tCost Price = " + costPrice +
                    "\n\tSelling Price = " + sellingPrice +
                    "\n\tDiscount Selling Price = " + discountSellingPrice;
        } else if (manufacturerDiscountActive) {
            res += "\n\tCost Price = " + costPrice +
                    "\n\tDiscount Cost Price = " + discountCostPrice +
                    "\n\tSelling Price = " + sellingPrice;
        } else {
            res += "\n\tCost Price = " + costPrice +
                    "\n\tSelling Price = " + sellingPrice;
        }

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

    public void setCategory(Category parentCategory) {
        Objects.requireNonNull(parentCategory, "Parent category cannot be null");
        this.category = parentCategory;
        parentCategory.addProduct(this);
    }


}
