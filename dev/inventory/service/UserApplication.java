package inventory.service;

import inventory.domain.*;

import java.time.LocalDate;
import java.util.List;

public class UserApplication {
    InventoryController inventoryController;

    public UserApplication() {
        this.inventoryController = new InventoryControllerImpl();
    }

    public void updateInventoryWithDefectiveItems(String productName, String productManufacturer, String location, LocalDate expiryDate, int defectedAmount) {
        Product product = inventoryController.getProductByName(productName, productManufacturer);

        inventoryController.updateInventoryWithDefects(product, location, expiryDate, defectedAmount);
    }

    public void checkForExpiredStock() {
        inventoryController.checkForExpiredStock();
    }

//    public void updateStockStatus(String stockId, StockItemStatus status) {
//        StockItem stockItem = inventoryController.getStockItemById(stockId);
//    }

    public void saveProduct(String name, int minimumStock, String[] categoryInfo, double costPrice, String location, String manufacturer) {
        System.out.println("Adding product: " + name);
        String mainCategory = categoryInfo[0];
        if (getCategoryById(inventoryController.getCategoryIdByName(mainCategory)) == null) {
            inventoryController.saveCategory(mainCategory, "");
        }
        for (int i = 1; i <= 2; i++) {
            if (getCategoryById(inventoryController.getCategoryIdByName(categoryInfo[i])) == null) {
                inventoryController.saveCategory(categoryInfo[i], categoryInfo[i - 1]);
            }
        }
        inventoryController.addProduct(name, minimumStock, mainCategory, costPrice, location, manufacturer);
    }

    public void saveStockItem(String productName, String productManufacturer, int quantity, String location, StockItemStatus status, LocalDate expiryDate) {
        System.out.println("Adding stock for product: ");
        inventoryController.saveStockItem(productName, productManufacturer, quantity, location, status, expiryDate);
    }

    public void removeProduct(String id) {
        inventoryController.removeProduct(id);
    }

    public void removeStock(String id) {
        inventoryController.removeStock(id);
    }

    public void uploadTestData() {
//        inventoryController.saveCategory("1234", "");
//        inventoryController.saveCategory("12345", "1234");
//        inventoryController.saveCategory("123456", "12345");
//        Category parentCategory = inventoryController.getCategoryById("1234");

        saveProduct("Test Product 1", 10, new String[]{"Cat1", "Cat11", "Cat111"}, 10, "A17-Shelf 12", "ADF");
        saveProduct("Test Product 2", 20, new String[]{"Cat2", "Cat21", "Cat211"}, 20, "A18-Shelf 17", "DCF");
        saveProduct("Test Product 3", 30, new String[]{"Cat3", "Cat31", "Cat311"}, 30, "C27-Shelf 3", "RCF");
        saveProduct("Test Product 4", 40, new String[]{"Cat1", "Cat12", "Cat122"}, 40, "B2-Shelf 8", "VBX");
        saveProduct("Test Product 5", 50, new String[]{"Cat2", "Cat22", "Cat221"}, 50, "B7-Shelf 2", "TCF");
        saveProduct("Test Product 6", 60, new String[]{"Cat1", "Cat12", "Cat122"}, 60, "S6-Shelf 9", "GGV");

        inventoryController.addDiscount(inventoryController.getProductByName("Test Product 6", "GGV").getId(), 20, "Test Discount 1",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10));
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("Cat1"), 20, "Test Discount 2",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10));
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("Cat122"), 20, "Test Discount 3",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10));

        inventoryController.saveStockItem("Test Product 1", "ADF", 50, "in store", StockItemStatus.OK, LocalDate.now().plusDays(10));
        inventoryController.saveStockItem("Test Product 2", "DCF", 20, "in store", StockItemStatus.OK, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Test Product 3", "RCF", 30, "in store", StockItemStatus.OK, LocalDate.now().plusDays(12));
        inventoryController.saveStockItem("Test Product 4", "VBX", 30, "storage", StockItemStatus.EXPIRED, LocalDate.now().plusDays(5));
        inventoryController.saveStockItem("Test Product 5", "TCF", 15, "in store", StockItemStatus.OK, LocalDate.now().plusDays(10));
        inventoryController.saveStockItem("Test Product 6", "GGV", 30, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(365));
        inventoryController.saveStockItem("Test Product 6", "GGV", 30, "in store", StockItemStatus.OK, LocalDate.now().plusDays(365));
        inventoryController.saveStockItem("Test Product 1", "ADF", 40, "storage", StockItemStatus.OK, LocalDate.now().plusDays(30));
        inventoryController.saveStockItem("Test Product 2", "DCF", 50, "storage", StockItemStatus.OK, LocalDate.now().plusDays(6));
        inventoryController.saveStockItem("Test Product 5", "TCF", 60, "storage", StockItemStatus.OK, LocalDate.now().plusDays(35));

    }

    public void printStockItemByProductByName(String name, String manufacturer) {
        inventoryController.printStockItemByProductByName(name, manufacturer);
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

    public void printOrderList() {
        inventoryController.printOrderList();
    }

    public Category getCategoryById(String catId) {
        return inventoryController.getCategoryById(catId);
    }

    public void saveCategory(String catName, String parentCategoryId) {
        inventoryController.saveCategory(catName, parentCategoryId);
    }

    public void updateDiscounts() {
        inventoryController.UpdateDiscounts();
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
            if (stockItem.getStatus() != StockItemStatus.OK) {
                System.out.println(stockItem);
            }
        }
    }

    public void moveStockItem(String productName, String productManufacturer, String newLocation, int amount, LocalDate expiryDate) {
        Product product = inventoryController.getProductByName(productName, productManufacturer);
        inventoryController.moveStockItem(product, newLocation, amount, expiryDate);

    }
}
