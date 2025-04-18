package inventory.domain;

import java.util.List;

public interface InventoryController {
    void addProduct(String id, String name, int minimumStock, String parentCategory);

    List<Product> getAllProductsDefinitions();

    void printAllProducts();

    void printAllCategories();

    void removeProduct(String id);

    void saveStockItem(String productId, int quantity, String location, StockItemStatus status);

    void printProductById(String id);

    void printCurrentStock();

    Category getCategoryById(String catId);

    void saveCategory(String catId, String catName, String parentCategory);
}
