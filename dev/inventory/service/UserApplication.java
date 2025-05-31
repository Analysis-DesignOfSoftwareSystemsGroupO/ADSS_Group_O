package inventory.service;

import MainService.SupplierInventoryService;
import inventory.data.DTO.ImmediateOrderDemand;
import inventory.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserApplication {
    public String branchId;
    InventoryController inventoryController;

    public UserApplication(String branchId) {
        this.branchId = branchId; // This can be set dynamically based on the branch

        this.inventoryController = new InventoryControllerImpl(branchId);
    }

    public void updateInventoryWithDefectiveItems(String productName, String productManufacturer, String location, LocalDate expiryDate, int defectedAmount) {
        inventoryController.updateInventoryWithDefects(productName, productManufacturer, location, expiryDate, defectedAmount);
    }

    public void checkForExpiredStock() {
        inventoryController.checkForExpiredStock();
    }


    public void saveProduct(String name, int minimumStock, String[] categoryInfo, double sellingPrice, String location, String manufacturer) {
        inventoryController.addProduct(name, minimumStock, categoryInfo, sellingPrice, location, manufacturer);
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

        saveProduct("Bamba", 10, new String[]{"Cat1", "Cat11", "Cat111"}, 10, "A17-Shelf 12", "Osem");
        saveProduct("Bisli", 20, new String[]{"Cat2", "Cat21", "Cat211"}, 20, "A18-Shelf 17", "Osem");
        saveProduct("Sprite", 30, new String[]{"Cat3", "Cat31", "Cat311"}, 55, "C27-Shelf 3", "Tempo");
        saveProduct("Chocolate", 150, new String[]{"Cat1", "Cat12", "Cat122"}, 11, "B2-Shelf 8", "Elit");
        saveProduct("Yogurt", 100, new String[]{"Cat2", "Cat22", "Cat221"}, 11.5, "B7-Shelf 2", "Shtraus");
        saveProduct("Bamba", 60, new String[]{"Cat4", "Cat41", "Cat411"}, 45.90, "S6-Shelf 9", "Lulu");
        saveProduct("Milk", 100, new String[]{"Cat2", "Cat22", "Cat222"}, 21.90, "S4-Shelf 13", "Tnuva");
        saveProduct("Bread", 80, new String[]{"Cat3", "Cat32", "Cat321"}, 15.90, "M11-Shelf 12", "Ariel Bakery");
        saveProduct("Pasta", 150, new String[]{"Cat2", "Cat22", "Cat121"}, 12, "H5-Shelf 18", "Osem");
        saveProduct("Cola", 200, new String[]{"Cat4", "Cat41", "Cat412"}, 9.90, "U2-Shelf 10", "Coca Cola");

        inventoryController.addDiscount(inventoryController.getProductByName("Cola", "Coca Cola").getId(), 20, "Test Discount 1",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.STORE);
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("Cat4"), 10, "Test Discount 2",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.STORE);
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("Cat12"), 5, "Test Discount 3",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.MANUFACTURER);
        inventoryController.addDiscount(inventoryController.getProductByName("Milk", "Tnuva").getId(), 10, "Test Discount 4",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.STORE);
        inventoryController.addDiscount(inventoryController.getProductByName("Sprite", "Tempo").getId(), 15, "Test Discount 5",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.MANUFACTURER);

        inventoryController.saveStockItem("Bamba", "Osem", 50, "in store", StockItemStatus.OK, LocalDate.now().plusDays(10));
        inventoryController.saveStockItem("Cola", "Coca Cola", 20, "in store", StockItemStatus.OK, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Sprite", "Tempo", 30, "in store", StockItemStatus.OK, LocalDate.now().plusDays(12));
        inventoryController.saveStockItem("Pasta", "Osem", 30, "storage", StockItemStatus.EXPIRED, LocalDate.now().minusDays(10));
        inventoryController.saveStockItem("Yogurt", "Shtraus", 15, "in store", StockItemStatus.OK, LocalDate.now().plusDays(10));
        inventoryController.saveStockItem("Pasta", "Osem", 30, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(365));
        inventoryController.saveStockItem("Bread", "Ariel Bakery", 30, "in store", StockItemStatus.OK, LocalDate.now().plusDays(365));
        inventoryController.saveStockItem("Bisli", "Osem", 40, "storage", StockItemStatus.OK, LocalDate.now().plusDays(30));
        inventoryController.saveStockItem("Bisli", "Osem", 50, "storage", StockItemStatus.OK, LocalDate.now().minusDays(6));
        inventoryController.saveStockItem("Milk", "Tnuva", 15, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Bamba", "Lulu", 60, "storage", StockItemStatus.OK, LocalDate.now().plusDays(110));
        inventoryController.saveStockItem("Chocolate", "Elit", 20, "storage", StockItemStatus.OK, LocalDate.now().plusDays(1));
        inventoryController.saveStockItem("Milk", "Tnuva", 22, "storage", StockItemStatus.OK, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Bread", "Ariel Bakery", 13, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(150));
        inventoryController.saveStockItem("Bisli", "Osem", 52, "storage", StockItemStatus.OK, LocalDate.now().plusDays(22));
        inventoryController.saveStockItem("Milk", "Tnuva", 74, "storage", StockItemStatus.OK, LocalDate.now().plusDays(21));
        inventoryController.saveStockItem("Bread", "Ariel Bakery", 30, "storage", StockItemStatus.OK, LocalDate.now().plusDays(21));
        inventoryController.saveStockItem("Chocolate", "Elit", 24, "storage", StockItemStatus.OK, LocalDate.now().minusDays(3));
        inventoryController.saveStockItem("Cola", "Coca Cola", 36, "storage", StockItemStatus.OK, LocalDate.now().plusDays(4));

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
                            DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate, DiscountType discountType) {
        inventoryController.addDiscount(discountTargetId, discountPercentage, discountDescription, type,
                discountStartDate, discountEndDate, discountType);
    }

    public List<ImmediateOrderDemand> checkAndCreateImmediateOrderDemands() throws Exception {
        return inventoryController.checkAndCreateImmediateOrderDemands();
    }


    public void listDiscounts() {
        inventoryController.listDiscounts();
    }

    public double getDiscountByProductId(String productId) {
        return inventoryController.getDiscountByProductId(productId);
    }

    public void printDefectedStockItems() {
        inventoryController.printDefectedStockItems();
    }

    public void sellProduct(String productId, int quantity) {
        inventoryController.sellProduct(productId, quantity);
    }

    public void moveStockItem(String productName, String productManufacturer, String newLocation, int amount, LocalDate expiryDate) {
        Product product = inventoryController.getProductByName(productName, productManufacturer);
        inventoryController.moveStockItem(product, newLocation, amount, expiryDate);

    }

    public void printExpiredStockItems() {
        inventoryController.printExpiredStockItems();
    }

    public void clearExpiredStock() {
        inventoryController.clearExpiredStock();
    }

    public void clearDefectedStockItems() {
        inventoryController.clearDefectedStockItems();
    }

    public void removeDiscount(String discountId) {
        inventoryController.removeDiscount(discountId);
    }

    public void printProductsByCategories(ArrayList<String> categoryNames) {
        inventoryController.printProductsByCategories(categoryNames);
    }

    public void updateMinimumStockLevel(String productId, int newMinimumStockLevel) {
        inventoryController.updateMinimumStockLevel(productId, newMinimumStockLevel);
    }

    public int getAmountToOrder(String productName, String productManufacturer) {
        return inventoryController.getAmountToOrder(productName, productManufacturer);
    }


}
