package inventory.service;

import inventory.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class UserApplication {
    InventoryController inventoryController;

    public UserApplication() {
        this.inventoryController = new InventoryControllerImpl();

    }

    public void saveProduct(String id, String name, int minimumStock) {
        inventoryController.addProduct(id, name, minimumStock);
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
        inventoryController.addProduct("1", "Test Product 1", 10);
        inventoryController.addProduct("2", "Test Product 2", 20);
        inventoryController.addProduct("3", "Test Product 3", 30);
        inventoryController.addProduct("4", "Test Product 4", 40);
        inventoryController.addProduct("5", "Test Product 5", 50);
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
}
