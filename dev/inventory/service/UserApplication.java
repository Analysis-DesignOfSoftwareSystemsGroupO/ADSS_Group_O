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
        inventoryController.saveCategory("12345", "Nicer Category", "1234");
        Category parentCategory = inventoryController.getCategoryById("1234");

        inventoryController.addProduct("1", "Test Product 1", 10, "1234");
        inventoryController.addProduct("2", "Test Product 2", 20, "1234");
        inventoryController.addProduct("3", "Test Product 3", 30, "1234");
        inventoryController.addProduct("4", "Test Product 4", 40, "1234");
        inventoryController.addProduct("5", "Test Product 5", 50, "1234");
        inventoryController.addProduct("6", "Test Product 6", 50, "12345");

        inventoryController.addDiscount("6", 20, "Test Discount 1",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10));
        inventoryController.addDiscount("12345", 20, "Test Discount 2",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10));
        inventoryController.addDiscount("1234", 20, "Test Discount 3",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10));

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

    public void deleteCategory(String toRemoveCatId) {
        inventoryController.deleteCategory(toRemoveCatId);
    }

    public void addDiscount(String discountTargetId, double discountPercentage, String discountDescription,
                            DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate) {
        inventoryController.addDiscount(discountTargetId, discountPercentage, discountDescription, type,
                                        discountStartDate, discountEndDate);
    }

    public void listDiscounts() {
        inventoryController.listDiscounts();
    }

    public double getDiscountByProductId(String productId) {
        return inventoryController.getDiscountByProductId(productId);
    }
}
