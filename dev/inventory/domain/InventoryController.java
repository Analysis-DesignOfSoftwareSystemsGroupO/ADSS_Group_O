package inventory.domain;

import java.time.LocalDate;
import java.util.List;

public interface InventoryController {
    void addProduct( String name, int minimumStock, String parentCategory, double costPrice, String location, String manufacturer);

    List<Product> getAllProductsDefinitions();

    void printAllProducts();

    void printAllCategories();

    void removeProduct(String id);

    void removeStock(String id);

    void saveStockItem(String productName,String productManufacturer, int quantity, String location, StockItemStatus status, LocalDate expiryDate);

    void printProductById(String id);

    void printCurrentStock();

    Category getCategoryById(String catId);

    void saveCategory(String catName, String parentCategory);

    void deleteCategory(String toRemoveCatId);

    void addDiscount(String discountTargetId, double discountPercentage, String discountDescription,
                     DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate);
    void printStockItemByProductByName(String name, String manufacturer);

    void listDiscounts();

    double getDiscountByProductId(String productId);

    void moveStockItem(String productName,String productManufacturer,String newLocation, int amount, LocalDate expiryDate);

    void changeStockItemStatus(String productName,String productManufacturer,LocalDate expiryDate,StockItemStatus newStatus);

    void UpdateDiscounts();

    String getCategoryIdByName(String name);
    List<StockItem> getAllStockItems();
    Product getProductByName(String name, String manufacturer);
}
