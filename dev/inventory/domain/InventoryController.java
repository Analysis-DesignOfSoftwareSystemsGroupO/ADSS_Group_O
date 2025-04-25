package inventory.domain;

import java.time.LocalDate;
import java.util.List;

public interface InventoryController {
    void addProduct( String name, int minimumStock, String parentCategory, double costPrice);

    List<Product> getAllProductsDefinitions();

    void printAllProducts();

    void printAllCategories();

    void removeProduct(String id);

    void saveStockItem(String productName,String productManufacturer, int quantity, String location, StockItemStatus status);

    void printProductById(String id);

    void printCurrentStock();

    Category getCategoryById(String catId);

    void saveCategory(String catName, String parentCategory);

    void deleteCategory(String toRemoveCatId);

    void addDiscount(String discountTargetId, double discountPercentage, String discountDescription,
                     DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate);

    void listDiscounts();

    double getDiscountByProductId(String productId);

    public String getCategoryIdByName(String name);

    public List<StockItem> getAllStockItems();
}
