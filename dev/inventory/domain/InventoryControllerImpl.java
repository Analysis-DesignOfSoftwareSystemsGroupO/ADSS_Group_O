package inventory.domain;

import inventory.data.*;

import java.util.List;

public class InventoryControllerImpl implements InventoryController {
    // Assuming ProductRepository is a class that provides access to product data
    private final ProductRepository productRepository = new InMemoryProductRepository();
    private final CategoryRepository categoryRepository = new InMemoryCategoryRepository();
    private final StockItemRepository stockItemRepository = new InMemoryStockItemRepository();

    public InventoryControllerImpl() {
        // Constructor can be used for dependency injection if needed
    }

    public void addProduct(String id, String name, int minimumStock) {
        System.out.println("Adding product: " + name);
        Product productToAdd = new Product(id, name, minimumStock);
        productRepository.saveProduct(productToAdd);
    }

    public void removeProduct(String id) {
        System.out.println("Removing product with ID: " + id);
        productRepository.deleteProduct(id);
    }

    public void saveStockItem(String productId, int quantity, String location, StockItemStatus status) {
        System.out.println("Adding stock for product with ID: " + productId);
        StockItem stockItemToAdd = new StockItem(productId, quantity, location, status);
        stockItemRepository.saveStockItem(stockItemToAdd);
    }

    public List<Product> getAllProductsDefinitions() {
        System.out.println("Getting all products...");
        return productRepository.getAllProducts();
    }

    public void printAllProducts() {
        productRepository.printAllProducts();
    }

    public void printAllCategories() {
        categoryRepository.printAllCategories();
    }

    public void printProductById(String id) {
        Product product = productRepository.getProductById(id);
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product with ID " + id + " not found.");
        }
    }

    public void printCurrentStock(){
        System.out.println("Current stock:");
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            System.out.println(stockItem);
        }
    }


}
