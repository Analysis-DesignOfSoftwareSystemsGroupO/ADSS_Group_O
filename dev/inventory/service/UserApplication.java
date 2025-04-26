package inventory.service;

import inventory.domain.*;

import java.time.LocalDate;
import java.util.List;

public class UserApplication {
    InventoryController inventoryController;

    public UserApplication() {
        this.inventoryController = new InventoryControllerImpl();
    }

    public void saveProduct(String name, int minimumStock, String parentCategory, double costPrice, String location, String manufacturer) {
        System.out.println("Adding product: " + name);
        inventoryController.addProduct(name, minimumStock, parentCategory, costPrice, location, manufacturer);
    }

    public void saveStockItem(String productName, String productManufacturer, int quantity, String location, StockItemStatus status, LocalDate stockDate) {
        System.out.println("Adding stock for product: ");
        inventoryController.saveStockItem(productName, productManufacturer, quantity, location, status);
    }

    public void removeProduct(String id) {
        inventoryController.removeProduct(id);
    }

    public void removeStock(String id){
        inventoryController.removeStock(id);
    }

    public void uploadTestData() {
        inventoryController.saveCategory("1234", "");
        inventoryController.saveCategory("12345", inventoryController.getCategoryIdByName("1234"));
        inventoryController.saveCategory("123456", inventoryController.getCategoryIdByName("12345"));
//        Category parentCategory = inventoryController.getCategoryById("1234");

        inventoryController.addProduct("Test Product 1", 10, inventoryController.getCategoryIdByName("1234"), 10,"A17-Shelf 12","ADF");
        inventoryController.addProduct("Test Product 2", 20, inventoryController.getCategoryIdByName("1234"), 20,"A18-Shelf 17","DCF");
        inventoryController.addProduct("Test Product 3", 30, inventoryController.getCategoryIdByName("1234"), 30,"C27-Shelf 3","RCF");
        inventoryController.addProduct("Test Product 4", 40, inventoryController.getCategoryIdByName("1234"), 40,"B2-Shelf 8","VBX");
        inventoryController.addProduct("Test Product 5", 50, inventoryController.getCategoryIdByName("1234"), 50,"B7-Shelf 2","TCF");
        inventoryController.addProduct("Test Product 6", 60, inventoryController.getCategoryIdByName("12345"), 60,"S6-Shelf 9","GGV");

        inventoryController.addDiscount(inventoryController.getCategoryIdByName("1234"), 20, "Test Discount 1",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10));
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("1234"), 20, "Test Discount 2",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10));
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("12345"), 20, "Test Discount 3",
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

    public void printCurrentStock() {
        inventoryController.printCurrentStock();
    }

    public Category getCategoryById(String catId) {
        return inventoryController.getCategoryById(catId);
    }

    public void saveCategory(String catName, String parentCategoryId) {
        inventoryController.saveCategory(catName, parentCategoryId);
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

    public void printDefectedStockItems() {
        List<StockItem> stockItems = inventoryController.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.DAMAGED || stockItem.getStatus() == StockItemStatus.EXPIRED) {
                System.out.println(stockItem);
            }
        }
    }
}
