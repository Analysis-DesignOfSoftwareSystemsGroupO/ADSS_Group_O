package inventory.domain;

import inventory.data.*;
import inventory.data.DAO.CategoryDAO;
import inventory.data.DAO.DiscountDAO;
import inventory.data.DAO.ProductDAO;
import inventory.data.DAO.StockItemDAO;
import inventory.data.Repositories.InMemoryCategoryRepository;
import inventory.data.Repositories.InMemoryDiscountRepository;
import inventory.data.Repositories.InMemoryProductRepository;
import inventory.data.Repositories.InMemoryStockItemRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InventoryControllerImpl implements InventoryController {
    // Assuming ProductRepository is a class that provides access to product data
    private final ProductRepository productRepository = new InMemoryProductRepository();
    private final StockItemRepository stockItemRepository = new InMemoryStockItemRepository();
    private final DiscountRepository discountRepository = new InMemoryDiscountRepository();
    private final CategoryRepository categoryRepository = new InMemoryCategoryRepository();
    private final ProductDAO productDAO = new ProductDAO();
    private final StockItemDAO stockItemDAO = new StockItemDAO(productDAO);
    private final DiscountDAO discountDAO = new DiscountDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();


    public InventoryControllerImpl() {
    }

    public void addProduct(String name, int minimumStock, String[] categoryInfo,
                           double sellingPrice, String location, String manufacturer) {
        for (int i = 0; i <= 2; i++) {
            if (!categoryDAO.categoryExists(categoryInfo[i])) {
                saveCategory(categoryInfo[i], "");
            }
        }

        String groupId = getOrCreateCategoryGroup(categoryInfo[0], categoryInfo[1], categoryInfo[2]);

        if (productDAO.productExists(name, manufacturer)) {
            System.out.println("Product already exists. Updating existing product.");
            return;
        } else {
            Product productToAdd = new Product(name, minimumStock,sellingPrice, location, manufacturer, groupId);
            // Add to repository
            productRepository.saveProduct(productToAdd);
            // Save to DB - category
            productDAO.saveProduct(productToAdd);
            // Save to DB - categories by products
            List<String> categories = categoryDAO.getCategoriesByGroupId(groupId);
            for (String categoryId : categories) {
                categoryDAO.SaveCategoryByProductPair(productToAdd.getId(), categoryId);
            }
            // Save to DB - selling prices
            productDAO.saveSellingPrice(productToAdd);
        }
        for (int i = 0; i <= 2; i++) {
            String categoryId = getCategoryIdByName(categoryInfo[i]);
            Category cat = getCategoryById(categoryId);
            cat.getProducts().add(getProductByName(name, manufacturer));
        }

    }

    public void removeProduct(String id) {
        System.out.println("Removing product with ID: " + id);
        List<StockItem> stockItems = stockItemDAO.getStockItemsByProductId(id);
        if (!stockItems.isEmpty()) {
            throw new IllegalArgumentException("Product has stock items. Cannot delete product.");
        }

        productDAO.removeFromProductsByCategory(id);
        //todo: check for discount before deleting
        String groupId = productDAO.getProductById(id).getCategoryGroupId();
        productDAO.deleteFromSellingPrices(id);
        productRepository.deleteProduct(id);
        productDAO.deleteProduct(id);
        if (productDAO.getProductsByGroupId(groupId).isEmpty()) {
            List<String> categories = categoryDAO.getCategoriesByGroupId(groupId);
            categoryDAO.deleteCategoryGroup(groupId);
            for (String categoryId : categories) {
                if (categoryDAO.getCategoryGroupsByCategoryId(categoryId).size()==1) {
                    categoryDAO.deleteCategoryFromCategories(categoryId);
                }
            }
        }
        else {
            productDAO.deleteFromSellingPrices(id);
            productRepository.deleteProduct(id);
            productDAO.deleteProduct(id);
        }


    }

    public void saveStockItem(String productName, String productManufacturer, int quantity, String location, StockItemStatus status, LocalDate expiryDate) {
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock add operation.");
        }
        StockItem stockItemToAdd = new StockItem(quantity, location, status, expiryDate);
        stockItemToAdd.setProduct(product);
        if (location == "in store") {
            stockItemToAdd.setLocation(product.getLocation());
        } else if (location == "storage") {
            stockItemToAdd.setLocation(location);
        }

        stockItemRepository.saveStockItem(stockItemToAdd);
        stockItemDAO.saveStockItem(stockItemToAdd);
    }

    public List<Product> getAllProductsDefinitions() {
        System.out.println("Getting all products...");
        return productDAO.getAllProducts();
    }

    public List<StockItem> getAllStockItems() {
        System.out.println("Getting all stock items...");
        return stockItemDAO.getAllStockItems();
    }

    public void printAllProducts() {
        System.out.println("------- Product Report -------");
        List<Product> products = productDAO.getAllProducts();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public void printAllCategories() {
        InMemoryCategoryRepository.printAllCategories();
    }

    public void UpdateDiscounts() {
        System.out.println("Updating discounts...");
        discountDAO.updateAllDiscountsAndSellingPrices();
//        // Remove Expired and Apply Active to selling prices
//        discountDAO.updateAllDiscountsSellingPrices();
//        // Update all product selling prices
//        productDAO.updateAllProductSellingPricesInBulk();

    }

    public void checkForExpiredStock() {
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.OK && stockItem.getExpiryDate().isBefore(LocalDate.now())) {
                System.out.println("Product " + stockItem.getProduct().getName() + " is expired.");
                stockItem.setStatus(StockItemStatus.EXPIRED);
                stockItemDAO.updateStockItem(stockItem);
            }
        }
    }


    public Category getCategoryById(String id) {
        return categoryDAO.getCategoryById(id);
    }

    public String getCategoryIdByName(String name) {
        return categoryDAO.getCategoryByName(name).getId();
    }

    public void saveCategory(String catName, String parentCategoryName) {
        Category newCategory = new Category(catName);
        categoryDAO.saveCategory(newCategory);

    }

    public String getOrCreateCategoryGroup(String parentName, String subName, String subSubName) {
        String parentId = categoryDAO.getCategoryByName(parentName).getId();
        String subId = categoryDAO.getCategoryByName(subName).getId();
        String subSubId = categoryDAO.getCategoryByName(subSubName).getId();
        return categoryDAO.getOrCreateCategoryGroup(parentId, subId, subSubId);
    }

    public void updateInventoryWithDefects(String productName, String productManufacturer, String location, LocalDate expiryDate, int defectedAmount) {
        Product product = productDAO.getProductByNameAndManufacturer(productName, productManufacturer);

        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock status change operation.");
        }
        String stockItemId = stockItemDAO.getStockItemByBatch(product, location, expiryDate, StockItemStatus.OK);
        if (stockItemId == null) {
            throw new IllegalArgumentException("No stock item found with the specified batch and status.");
        }
        StockItem originStockItem = stockItemDAO.getStockItemById(stockItemId);
        if (originStockItem.getQuantity() < defectedAmount) {
            throw new IllegalArgumentException("Defected amount exceeds available stock.");
        }
        String targetStockItemId = getTargetStockId(product, "storage", expiryDate, StockItemStatus.DAMAGED);
        StockItem targetStockItem = stockItemDAO.getStockItemById(targetStockItemId);
        moveBatch(originStockItem, targetStockItem, defectedAmount);

        stockItemDAO.updateStockItem(originStockItem);
        stockItemDAO.updateStockItem(targetStockItem);
        if (countProductQuantity(product.getId()) <= product.getMinimumStockLevel()) {
            System.out.println(" ***** Warning: Product " + product.getName() +
                    " is below minimum stock level. Please restock. *****");
        }
    }

    public void changeStockItemStatus(StockItem stockItem, StockItemStatus newStatus) {
        System.out.println("Changing stock item status...");
        if (stockItem.getStatus() != newStatus) {
            stockItem.setStatus(newStatus);
            stockItemDAO.updateStockItem(stockItem);
        }
    }

    public void moveStockItem(Product product, String newLocation, int amount, LocalDate expiryDate) {
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        String originId;
        String destinationId = "";

        while (amount > 0) {
            if (Objects.equals(newLocation, "in store")) {
                // Overall Product to move
                originId = getEarliestExpiryDateInStorage(product, "storage"); // Compatible StockItem ID Origin
                destinationId = getTargetStockId(product, newLocation, expiryDate, StockItemStatus.OK);
            } else if (Objects.equals(newLocation, "storage")) {
                originId = getStockItemByBatch(product, "in store", expiryDate, StockItemStatus.OK); //Giving StockItem ID
                destinationId = getTargetStockId(product, newLocation, expiryDate, StockItemStatus.OK);
            } else {
                throw new IllegalArgumentException("Invalid location. Aborting stock move operation.");
            }
            StockItem origin = getStockItemById(originId);
            StockItem destination = getStockItemById(destinationId);
            if (amount >= origin.getQuantity()) {
                System.out.println("Moving all stock from " + originId + " to " + destinationId);
                moveBatch(origin, destination, amount);
                amount = 0;
            } else {
                System.out.println("Moving partial stock from " + originId + " to " + destinationId);
                moveBatch(origin, destination, origin.getQuantity());
                amount -= origin.getQuantity();
            }
        }
    }

    public StockItem getStockItemById(String id) {
        StockItem stockItem = stockItemDAO.getStockItemById(id);
        if (stockItem == null) {
            throw new IllegalArgumentException("Stock item not found. Aborting stock move operation.");
        }
        return stockItem;
    }

    public String getTargetStockId(Product product, String location, LocalDate expiryDate, StockItemStatus status) {
        String stockItemId = stockItemDAO.getStockItemByBatch(product, location, expiryDate, status);
        if (stockItemId != null) {
            return stockItemId;
        } else {
            // Create a new StockItem since no matching one exists
            StockItem newStockItem = new StockItem(0, location, status , expiryDate);
            newStockItem.setProduct(product);
            stockItemDAO.saveStockItem(newStockItem);
            return newStockItem.getStockItemId();
        }
    }


    public void moveBatch(StockItem origin, StockItem destination, int amount) {
        System.out.println("Moving batch of " + amount + " from " + origin.getLocation() + " to " + destination.getLocation());
        origin.setQuantity(origin.getQuantity() - amount);
        destination.setQuantity(destination.getQuantity() + amount);
        if (origin.getQuantity() == 0) {
            stockItemDAO.deleteStockItem(origin.getStockItemId());
        }
    }


    public String getEarliestExpiryDateInStorage(Product product, String location) {
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        LocalDate maxDate = LocalDate.now().minusYears(100);
        String productToMove = null;
        for (StockItem stockItem : stockItems) {
            if (stockItem.getExpiryDate().isBefore(maxDate) && stockItem.getLocation().equals(location) && stockItem.getExpiryDate().isAfter(LocalDate.now())
                    && stockItem.getStatus() == StockItemStatus.OK) {
                maxDate = stockItem.getExpiryDate();
                productToMove = stockItem.getProduct().getId();
            }
        }
        if (productToMove == null) {
            throw new IllegalArgumentException("No available stock item found in storage.");
        }
        return productToMove;
    }

    public String latestExpiryDateInStorage(String productName, String productManufacturer) {
        System.out.println("Moving stock item for product: " + productName);
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        LocalDate maxDate = LocalDate.now();
        String productToMove = null;
        for (StockItem stockItem : stockItems) {
            if (stockItem.getExpiryDate().isAfter(maxDate) && stockItem.getLocation().equals("in store")) {
                maxDate = stockItem.getExpiryDate();
                productToMove = stockItem.getProduct().getId();
            }
        }
        if (productToMove == null) {
            throw new IllegalArgumentException("No stock item found in storage with an expiry date before today.");
        }
        return productToMove;
    }

    public String getStockItemByBatch(Product product, String location, LocalDate expiryDate, StockItemStatus status) {
        List<StockItem> stockItems;
        if (location.equals("in store")) {
            stockItems = getStockItemsInStore();
        } else if (location.equals("storage")) {
            stockItems = getStockItemsFromStorage();
        } else if (location.equals("all")) {
            stockItems = stockItemDAO.getAllStockItems();
        } else {
            throw new IllegalArgumentException("Invalid location. Aborting stock move operation.");
        }
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(product.getId()) && stockItem.getExpiryDate().equals(expiryDate)
                    && stockItem.getStatus() == status) {
                return stockItem.getStockItemId();
            }
        }
        return null;
    }


    public void removeStock(String id) {
        System.out.println("Removing stock item with ID: " + id);
        StockItem stockItemToRemove = stockItemDAO.getStockItemById(id);
        Objects.requireNonNull(stockItemToRemove, "Stock item not found");
        stockItemDAO.deleteStockItem(id);
        int itemQuantity = countProductQuantity(stockItemToRemove.getProduct().getId());
        int minQuantity = stockItemToRemove.getProduct().getMinimumStockLevel();
        if (itemQuantity <= minQuantity) {
            System.out.println(" ***** Warning: Product " + stockItemToRemove.getProduct().getName() +
                    " is below minimum stock level. Please restock. *****");
        }
    }

    public void deleteCategory(String toRemoveCatId) {
        System.out.println("Removing category with ID: " + toRemoveCatId);
        Category categoryToRemove = getCategoryById(toRemoveCatId);
        Objects.requireNonNull(categoryToRemove, "Category not found");

        if (!categoryToRemove.getProducts().isEmpty() || !categoryToRemove.getSubCategories().isEmpty()) {
            throw new IllegalArgumentException("Category has products or subcategories. " +
                    "Cannot delete non empty category.");
        }

        if (categoryToRemove.getParentCategory() != null) {
            categoryToRemove.getParentCategory().removeSubCategory(categoryToRemove);
        }
        InMemoryCategoryRepository.deleteCategory(toRemoveCatId);
    }

    public void printProductById(String id) {
        Product product = productRepository.getProductById(id);
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product with ID " + id + " not found.");
        }
    }

    public void printCurrentStock() {
        System.out.println("------- Stock Report -------");
        List<Product> products = productDAO.getAllProducts();
        for (Product product : products) {
            int inStorage = countProductInStorage(product.getId());
            int productQuantity = countProductQuantity(product.getId());
            int defectedProductQuantity = countDefectedProductQuantity(product.getId());
            System.out.println("Product: " + product.getName()
                    + "\nProduct ID: " + product.getId()
                    + "\nProduct Manufacturer: " + product.getManufacturer()
                    + "\nProduct Minimum Stock Level: " + product.getMinimumStockLevel()
                    + "\nProduct Quantity: " + productQuantity
                    + "\nAmount of product in Storage: " + inStorage
                    + "\nAmount of Product in Store: " + (productQuantity + defectedProductQuantity - inStorage)
                    + "\nDamaged/Expired Product Quantity: " + defectedProductQuantity
                    + "\nLocation: " + product.getLocation() +
                    "\n------------------------------\n");
        }
    }


    public void printStockItemByProductByName(String name, String manufacturer) {
        System.out.println("Stock items for product: " + name + " " + manufacturer);
        Product product = getProductByName(name, manufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(product.getId())) {
                System.out.println("Stock ID: " + stockItem.getStockItemId()
                        + "\nQuantity: " + stockItem.getQuantity()
                        + "\nLocation: " + stockItem.getLocation()
                        + "\nExpiry Date: " + stockItem.getExpiryDate().toString()
                        + "\nStatus: " + stockItem.getStatus() +
                        "\n------------------------------\n");
            }
        }
    }

    public int countProductInStorage(String id) {
        Product product = productDAO.getProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
        int count = 0;
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(id) && stockItem.getLocation().equals("storage")) {
                count += stockItem.getQuantity();
            }
        }
        return count;
    }

    public int countProductQuantity(String id) {
        Product product = productDAO.getProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
        int count = 0;
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(id) && stockItem.getStatus() == StockItemStatus.OK) {
                count += stockItem.getQuantity();
            }
        }
        return count;
    }

    public int countDefectedProductQuantity(String id) {
        Product product = productDAO.getProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
        int count = 0;
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(id) && (stockItem.getStatus() != StockItemStatus.OK)) {
                count += stockItem.getQuantity();
            }
        }
        return count;
    }


    public void printDefectedStockItems() {
        System.out.println("Defected stock items:");
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.DAMAGED) {
                System.out.println(stockItem);
            }
        }
    }

    public void addDiscount(String discountTargetId, double discountPercentage, String discountDescription,
                            DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate, DiscountType discountType) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        if (discountEndDate.isBefore(discountStartDate)) {
            throw new IllegalArgumentException("Discount end date cannot be before start date.");
        }
        if (type == DiscountTargetType.PRODUCT) {
            Product product = productDAO.getProductById(discountTargetId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found. Aborting discount add operation.");
            }
        } else if (type == DiscountTargetType.CATEGORY) {
            Category category = getCategoryById(discountTargetId);
            if (category == null) {
                throw new IllegalArgumentException("Category not found. Aborting discount add operation.");
            }
        } else {
            throw new IllegalArgumentException("Invalid discount target type. Aborting discount add operation.");
        }
        Discount discount = new Discount(discountDescription, type, discountTargetId, discountPercentage,
                discountStartDate, discountEndDate, discountType);
        discountRepository.saveDiscount(discount);
        discountDAO.saveDiscount(discount);
    }

    public void listDiscounts() {
        discountRepository.printAllDiscounts();
    }

    public void sellProduct(String productId, int quantity) {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting sell operation.");
        }
        int availableQuantity = countProductQuantity(productId);
        if (availableQuantity < quantity) {
            throw new IllegalArgumentException("Not enough stock available. Aborting sell operation.");
        }

        List<StockItem> stockItems = getBatchesInStoreByProduct(product);
        stockItems.sort((a, b) -> a.getExpiryDate().compareTo(b.getExpiryDate()));

        int remainingQuantity = quantity;
        for (StockItem stockItem : stockItems) {
            if (stockItem.getQuantity() >= remainingQuantity) {
                stockItem.setQuantity(stockItem.getQuantity() - remainingQuantity);
                if (stockItem.getQuantity() == 0) {
                    stockItemDAO.deleteStockItem(stockItem.getStockItemId());
                }
                break;
            } else {
                remainingQuantity -= stockItem.getQuantity();
                stockItemDAO.deleteStockItem(stockItem.getStockItemId());
            }
        }

        if (countProductQuantity(productId) <= product.getMinimumStockLevel()) {
            System.out.println(" ***** Warning: Product " + product.getName() +
                    " is below minimum stock level. Please restock. *****");
        }
    }

    public List<StockItem> getBatchesInStoreByProduct(Product product) {
        List<StockItem> result = new ArrayList<>();
        List<StockItem> stockItems = getStockItemsInStore();
        for (StockItem item : stockItems) {
            if (item.getProduct().getId().equals(product.getId()) &&
                    item.getStatus() == StockItemStatus.OK) {
                result.add(item);
            }
        }
        return result;
    }

    public double getDiscountByProductId(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty.");
        }
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting discount retrieval operation.");
        }
        return discountDAO.getDiscountPercentageByProductId(productId);
    }

    public Product getProductByName(String name, String manufacturer) {
        List<Product> products = productDAO.getAllProducts();
        for (Product product : products) {
            if (product.getName().equals(name) && product.getManufacturer().equals(manufacturer)) {
                return product;
            }
        }
        return null;
    }

    public List<StockItem> getStockItemsFromStorage() {
        List<StockItem> allStockItems = stockItemDAO.getAllStockItems();
        List<StockItem> storageItems = new ArrayList<>();
        for (StockItem stockItem : allStockItems) {
            if (stockItem.getLocation().equals("storage")) {
                storageItems.add(stockItem);
            }
        }
        return storageItems;
    }

    public List<StockItem> getStockItemsInStore() {
        List<StockItem> allStockItems = stockItemDAO.getAllStockItems();
        List<StockItem> storeItems = new ArrayList<>();
        for (StockItem stockItem : allStockItems) {
            if (!stockItem.getLocation().equals("storage")) {
                storeItems.add(stockItem);
            }
        }
        return storeItems;
    }

    public void printOrderList() {
        System.out.println("Printing order list...");
        List<Product> products = productDAO.getAllProducts();
        List<Product> orderList = new ArrayList<>();
        for (Product product : products) {
            if (countProductQuantity(product.getId()) < product.getMinimumStockLevel()) {
                orderList.add(product);
            }
        }
        if (orderList.isEmpty()) {
            System.out.println("No items need to be ordered.");
        } else {
            System.out.println("Items that need to be ordered:");
            for (Product product : orderList) {
                System.out.println(product.getName() + " - " + product.getManufacturer() + " - " + (product.getMinimumStockLevel() - countProductQuantity(product.getId())));
            }
        }
    }

    public void printExpiredStockItems() {
        System.out.println("Expired stock items:");
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.EXPIRED) {
                System.out.println(stockItem);
            }
        }
    }

    public void clearDefectedStockItems() {
        System.out.println("Clearing defected stock items...");
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.DAMAGED) {
                stockItemDAO.deleteStockItem(stockItem.getStockItemId());
            }
        }
    }

    public void clearExpiredStock() {
        System.out.println("Clearing expired stock...");
        List<StockItem> stockItems = stockItemDAO.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.EXPIRED) {
                stockItemDAO.deleteStockItem(stockItem.getStockItemId());
            }
        }
    }


    public void removeDiscount(String discountId) {
        System.out.println("Removing discount with ID: " + discountId);
        Discount discountToRemove = discountRepository.getDiscountById(discountId);
        Objects.requireNonNull(discountToRemove, "Discount not found");
        discountRepository.deleteDiscount(discountId);
    }

    public void printProductsByCategories(ArrayList<String> categoryNames) {
        System.out.println("Products by categories:");
//        for (String categoryName : categoryNames) {
//            Category category = getCategoryById(getCategoryIdByName(categoryName));
//            if (category != null) {
//                System.out.println("Category: " + category.getName());
//                for (Product product : category.getProducts()) {
//                    System.out.println(product.toString());
//                }
//            } else {
//                System.out.println("Category with name " + categoryName + " not found.");
//            }
//        }
        List<Product> products = productDAO.getProductsByCategoryNames(categoryNames);
        if (products.isEmpty()) {
            System.out.println("No products found in the specified categories.");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }

    }

    public void updateMinimumStockLevel(String productId, int newMinimumStockLevel) {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting update operation.");
        }
        product.setMinimumStockLevel(newMinimumStockLevel);
        productDAO.updateProduct(product);
    }

}