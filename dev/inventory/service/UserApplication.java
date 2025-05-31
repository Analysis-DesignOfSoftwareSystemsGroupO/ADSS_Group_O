package inventory.service;

import MainService.SupplierInventoryService;
import inventory.data.DTO.ImmediateOrderDemand;
import inventory.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserApplication {
    String branchId;
    InventoryController inventoryController;
    SupplierInventoryService supplierInventoryService = new SupplierInventoryService();

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

        saveProduct("Test Product 1", 10, new String[]{"Cat1", "Cat11", "Cat111"}, 10, "A17-Shelf 12", "ADF");
        saveProduct("Test Product 2", 20, new String[]{"Cat2", "Cat21", "Cat211"}, 20, "A18-Shelf 17", "DCF");
        saveProduct("Test Product 3", 30, new String[]{"Cat3", "Cat31", "Cat311"}, 55, "C27-Shelf 3", "RCF");
        saveProduct("Test Product 4", 150, new String[]{"Cat1", "Cat12", "Cat122"}, 11, "B2-Shelf 8", "VBX");
        saveProduct("Test Product 5", 100, new String[]{"Cat2", "Cat22", "Cat221"}, 11.5, "B7-Shelf 2", "TCF");
        saveProduct("Test Product 6", 60, new String[]{"Cat4", "Cat41", "Cat411"}, 45.90, "S6-Shelf 9", "GGV");
        saveProduct("Test Product 7", 100, new String[]{"Cat2", "Cat22", "Cat222"}, 21.90, "S4-Shelf 13", "BBG");
        saveProduct("Test Product 8", 80, new String[]{"Cat3", "Cat32", "Cat321"}, 15.90, "M11-Shelf 12", "GGV");
        saveProduct("Test Product 9", 150, new String[]{"Cat2", "Cat22", "Cat121"}, 12, "H5-Shelf 18", "ATD");
        saveProduct("Test Product 10", 200, new String[]{"Cat4", "Cat41", "Cat412"}, 9.90, "U2-Shelf 10", "VBX");

        inventoryController.addDiscount(inventoryController.getProductByName("Test Product 3", "RCF").getId(), 20, "Test Discount 1",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.STORE);
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("Cat4"), 10, "Test Discount 2",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.STORE);
        inventoryController.addDiscount(inventoryController.getCategoryIdByName("Cat12"), 5, "Test Discount 3",
                DiscountTargetType.CATEGORY, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.MANUFACTURER);
        inventoryController.addDiscount(inventoryController.getProductByName("Test Product 10", "VBX").getId(), 10, "Test Discount 4",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.STORE);
        inventoryController.addDiscount(inventoryController.getProductByName("Test Product 1", "ADF").getId(), 15, "Test Discount 5",
                DiscountTargetType.PRODUCT, LocalDate.now(), LocalDate.now().plusDays(10), DiscountType.MANUFACTURER);

        inventoryController.saveStockItem("Test Product 1", "ADF", 50, "in store", StockItemStatus.OK, LocalDate.now().plusDays(10));
        inventoryController.saveStockItem("Test Product 2", "DCF", 20, "in store", StockItemStatus.OK, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Test Product 3", "RCF", 30, "in store", StockItemStatus.OK, LocalDate.now().plusDays(12));
        inventoryController.saveStockItem("Test Product 4", "VBX", 30, "storage", StockItemStatus.EXPIRED, LocalDate.now().minusDays(10));
        inventoryController.saveStockItem("Test Product 5", "TCF", 15, "in store", StockItemStatus.OK, LocalDate.now().plusDays(10));
        inventoryController.saveStockItem("Test Product 6", "GGV", 30, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(365));
        inventoryController.saveStockItem("Test Product 6", "GGV", 30, "in store", StockItemStatus.OK, LocalDate.now().plusDays(365));
        inventoryController.saveStockItem("Test Product 1", "ADF", 40, "storage", StockItemStatus.OK, LocalDate.now().plusDays(30));
        inventoryController.saveStockItem("Test Product 2", "DCF", 50, "storage", StockItemStatus.OK, LocalDate.now().minusDays(6));
        inventoryController.saveStockItem("Test Product 5", "TCF", 15, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Test Product 1", "ADF", 60, "storage", StockItemStatus.OK, LocalDate.now().plusDays(110));
        inventoryController.saveStockItem("Test Product 4", "VBX", 20, "storage", StockItemStatus.OK, LocalDate.now().plusDays(1));
        inventoryController.saveStockItem("Test Product 8", "GGV", 22, "storage", StockItemStatus.OK, LocalDate.now().plusDays(20));
        inventoryController.saveStockItem("Test Product 4", "VBX", 13, "storage", StockItemStatus.DAMAGED, LocalDate.now().plusDays(150));
        inventoryController.saveStockItem("Test Product 2", "DCF", 52, "storage", StockItemStatus.OK, LocalDate.now().plusDays(22));
        inventoryController.saveStockItem("Test Product 1", "ADF", 74, "storage", StockItemStatus.OK, LocalDate.now().plusDays(21));
        inventoryController.saveStockItem("Test Product 6", "GGV", 30, "storage", StockItemStatus.OK, LocalDate.now().plusDays(21));
        inventoryController.saveStockItem("Test Product 7", "BBG", 24, "storage", StockItemStatus.OK, LocalDate.now().minusDays(3));
        inventoryController.saveStockItem("Test Product 7", "BBG", 36, "storage", StockItemStatus.OK, LocalDate.now().plusDays(4));

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

    public void checkAndCreateImmediateOrder() throws Exception {
        List<ImmediateOrderDemand> orderList = inventoryController.checkAndCreateImmediateOrder();
        for (ImmediateOrderDemand order : orderList) {
            System.out.println("Immediate Order Demand: " + order);
            supplierInventoryService.createImmediateOrder(this.branchId, order.getProductName(), order.getManufacturer(), order.getAmountToOrder());
        }
    }

    public void createConstantOrder(String productId, int quantity, String location) {
        //inventoryController.createConstantOrder(productId, quantity, location);//TODO implement
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

}
