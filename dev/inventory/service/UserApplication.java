package inventory.service;

import inventory.domain.*;

import java.time.LocalDate;
import java.util.List;

public class UserApplication {
    InventoryController inventoryController;

    public UserApplication() {
        this.inventoryController = new InventoryControllerImpl();
    }

    public void saveProduct(String id, String name, int minimumStock, String parentCategory) {
        inventoryController.addProduct(id, name, minimumStock, parentCategory);
    }

    public void saveStockItem(String productId, int quantity, String location, StockItemStatus status, LocalDate stockDate) {
        System.out.println("Adding stock for product: ");
        inventoryController.printProductById(productId);
        inventoryController.saveStockItem(productId, quantity, location, status);
    }

    public void removeProduct(String id) {
        inventoryController.removeProduct(id);
    }

    public void uploadTestData() {
        inventoryController.saveCategory("1234", "Nice Category", "");
        Category parentCategory = inventoryController.getCategoryById("1234");

        inventoryController.addProduct("1", "Test Product 1", 10, "1234");
        inventoryController.addProduct("2", "Test Product 2", 20, "1234");
        inventoryController.addProduct("3", "Test Product 3", 30, "1234");
        inventoryController.addProduct("4", "Test Product 4", 40, "1234");
        inventoryController.addProduct("5", "Test Product 5", 50, "1234");
    }

    public List<Product> getAllProductsDefinitions() {
        return inventoryController.getAllProductsDefinitions();
    }

    public void printAllProducts() {
        inventoryController.printAllProducts();
    }

    public void printAllCategories() {
        inventoryController.printAllCategories();
    }

    public void printProductById(String id) {
        inventoryController.printProductById(id);
    }

    public void printStockItemByProductId(String id) {
        inventoryController.printProductById(id);
    }

    public void printCurrentStock(){
        inventoryController.printCurrentStock();
    }

    public Category getCategoryById(String catId) {
        return inventoryController.getCategoryById(catId);
    }

    public void saveCategory(String catId, String catName, String parentCategory) {
        inventoryController.saveCategory(catId, catName, parentCategory);
    }
}
