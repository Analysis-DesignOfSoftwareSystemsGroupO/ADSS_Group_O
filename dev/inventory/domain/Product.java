package inventory.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a product in the inventory system. (catalog item)
 * Holds information common to all items of this type.
 */
public class Product {
    private String id;
    private String name;
    private String manufacturer;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private int minimumStockLevel;
    private Category category;
//    private Discount discount;

    /**
     * Constructor for Product
     *
     * @param id                Unique id for the product. (cannot be null)
     * @param name              The display name of the product. (cannot be null)
     * @param minimumStockLevel The minimum amount of this product that should be in stock. (cannot be negative)
     */
    public Product(String id, String name, int minimumStockLevel) {
        Objects.requireNonNull(id, "Product id cannot be null");
        Objects.requireNonNull(name, "Product name cannot be null");
        if (minimumStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.minimumStockLevel = minimumStockLevel;
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public BigDecimal getSellingPrice() {
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
