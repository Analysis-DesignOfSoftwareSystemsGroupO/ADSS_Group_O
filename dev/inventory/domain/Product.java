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
    private int minimumStockLevel;
    private Category category;
    private String location;
    private boolean discountActive;
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
        this.discountActive = false;
        this.location = location;
    }

    public boolean getDiscountActive() {
        return discountActive;
    }

    public void setDiscountActive(boolean discountActive) {
        this.discountActive = discountActive;
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

    @Override
    public String toString() {
        return "Product {" +
                "\n\tid = '" + id + "'," +
                "\n\tname= '" + name + "'," +
                "\n\tmanufacturer = '" + manufacturer + "'," +
                "\n\tcostPrice = " + costPrice + "'," +
                "\n\tsellingPrice = " + sellingPrice + "'," +
                "\n\tminimumStockLevel = " + minimumStockLevel + "'," +
                "\n\tcategory = '" + category.getName() + "'" +
                "\n\tlocation = '" + location + "'" +
                '}';
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
