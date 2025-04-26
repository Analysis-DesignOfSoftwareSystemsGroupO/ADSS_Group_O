package inventory.domain;

import inventory.data.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class InventoryControllerImpl implements InventoryController {
    // Assuming ProductRepository is a class that provides access to product data
    private final ProductRepository productRepository = new InMemoryProductRepository();
    private final StockItemRepository stockItemRepository = new InMemoryStockItemRepository();
    private final DiscountRepository discountRepository = new InMemoryDiscountRepository();

    public InventoryControllerImpl() {
    }

    public void addProduct(String name, int minimumStock, String parentCategory, double costPrice, String location, String manufacturer) {
        System.out.println("Adding product: " + name);
        Category prodParentCategory = getCategoryById(getCategoryIdByName(parentCategory));
        if (prodParentCategory == null) {
            throw new IllegalArgumentException("Parent category not found. Aborting product add operation.");
        }

        Product productToAdd = new Product(name, minimumStock, costPrice, location, manufacturer);
        productToAdd.setCategory(prodParentCategory);
        productRepository.saveProduct(productToAdd);
    }

    public void removeProduct(String id) {
        System.out.println("Removing product with ID: " + id);
        Product productToRemove = productRepository.getProductById(id);
        Objects.requireNonNull(productToRemove, "Product not found");
        Category parentCategory = productToRemove.getCategory();
        if (parentCategory != null) {
            parentCategory.removeProduct(productToRemove);
        }
        productRepository.deleteProduct(id);
    }

    public void saveStockItem(String productName, String productManufacturer, int quantity, String location, StockItemStatus status, LocalDate expiryDate) {
        System.out.println("Adding stock for product: " + productName);
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
    }

    public List<Product> getAllProductsDefinitions() {
        System.out.println("Getting all products...");
        return productRepository.getAllProducts();
    }

    public List<StockItem> getAllStockItems() {
        System.out.println("Getting all stock items...");
        return stockItemRepository.getAllStockItems();
    }

    public void printAllProducts() {
        productRepository.printAllProducts();
    }

    public void printAllCategories() {
        InMemoryCategoryRepository.printAllCategories();
    }

    public Category getCategoryById(String id) {
        return InMemoryCategoryRepository.getCategoryById(id);
    }

    public String getCategoryIdByName(String name) {
        return InMemoryCategoryRepository.getCategoryIdByName(name);
    }

    public void saveCategory(String catName, String parentCategoryName) {
        System.out.println("Adding category: " + catName);

        Category parentCategory = getCategoryById(getCategoryIdByName(parentCategoryName));
        if (!parentCategoryName.isEmpty() && parentCategory == null) {
            throw new IllegalArgumentException("Parent category not found. Aborting category add operation.");
        }
        Category newCategory = new Category(catName);

        newCategory.setParentCategory(parentCategory);
        if (parentCategory != null) {
            Category parent = this.getCategoryById(parentCategory.getId());
            if (parent != null) {
                parent.addSubCategory(newCategory);
            } else {
                throw new IllegalArgumentException("Parent category not found");
            }
        }
    }

    public void changeStockItemStatus(String productName, String productManufacturer, LocalDate expiryDate, StockItemStatus newStatus) {
        System.out.println("Changing stock item status for product: " + productName);
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock status change operation.");
        }
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(product.getId()) && stockItem.getExpiryDate().equals(expiryDate)) {
                stockItem.setStatus(newStatus);
                stockItemRepository.updateStockItem(stockItem);
            }
        }
        if (countProductQuantity(product.getId()) <= product.getMinimumStockLevel()) {
            System.out.println(" ***** Warning: Product " + product.getName() +
                    " is below minimum stock level. Please restock. *****");
        }
    }

    public void moveStockItem(String productName, String productManufacturer, String newLocation, int amount, LocalDate expiryDate) {
        System.out.println("Moving stock item for product: " + productName);
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        String originId;
        String destinationId;

        while (amount > 0) {
            if (Objects.equals(newLocation, "in store")) {
                // Overall Product to move
                originId = getEarliestExpiryDateInStorage(product); // Compatible StockItem ID Origin
                destinationId = getTargetStockId(product, newLocation, expiryDate);
            } else if (Objects.equals(newLocation, "storage")) {
                originId = getStockItemByBatch(product, "in store", expiryDate); //Giving StockItem ID
                destinationId = getTargetStockId(product, newLocation, expiryDate);
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
                moveBatch(origin, destination, amount);
                amount -= origin.getQuantity();
            }
        }
    }

    public StockItem getStockItemById(String id) {
        StockItem stockItem = stockItemRepository.getStockItemById(id);
        if (stockItem == null) {
            throw new IllegalArgumentException("Stock item not found. Aborting stock move operation.");
        }
        return stockItem;
    }

    public String getTargetStockId(Product product, String location, LocalDate expiryDate) {
        String stockItemId = getStockItemByBatch(product, location, expiryDate);
        if (stockItemId != null) {
            return stockItemId;
        } else {
            // Create a new StockItem since no matching one exists
            StockItem newStockItem = new StockItem(0, location, StockItemStatus.OK, expiryDate);
            newStockItem.setProduct(product);
            stockItemRepository.saveStockItem(newStockItem);
            return newStockItem.getStockItemId();
        }
    }


    public String getStockItemByProdNameManExp(String productName, String productManufacturer, LocalDate expiryDate) {
        System.out.println("Moving stock item for product: " + productName);
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        String stockToMove = null;
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(product.getId()) && stockItem.getExpiryDate().equals(expiryDate)) {
                stockToMove = stockItem.getStockItemId();
                break;
            }
        }
        if (stockToMove == null) {
            throw new IllegalArgumentException("No stock item found in store with an expiry date before today.");
        }
        return stockToMove;
    }


    public void moveBatch(StockItem origin, StockItem destination, int amount) {
        System.out.println("Moving batch of " + amount + " from " + origin.getLocation() + " to " + destination.getLocation());
        origin.setQuantity(origin.getQuantity() - amount);
        destination.setQuantity(destination.getQuantity() + amount);
        if (destination.getQuantity() == 0) {
            stockItemRepository.deleteStockItem(destination.getStockItemId());
        }
    }


    public String getEarliestExpiryDateInStorage(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        LocalDate maxDate = LocalDate.now().minusYears(100);
        String productToMove = null;
        for (StockItem stockItem : stockItems) {
            if (stockItem.getExpiryDate().isBefore(maxDate) && stockItem.getLocation().equals("storage") && stockItem.getExpiryDate().isAfter(LocalDate.now())) {
                maxDate = stockItem.getExpiryDate();
                productToMove = stockItem.getProduct().getId();
            }
        }
        if (productToMove == null) {
            throw new IllegalArgumentException("No stock item found in storage with an expiry date before today.");
        }
        return productToMove;
    }

    public String latestExpiryDateInStorage(String productName, String productManufacturer) {
        System.out.println("Moving stock item for product: " + productName);
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock move operation.");
        }
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
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

    public String getStockItemByBatch(Product product, String location, LocalDate expiryDate) {
        List<StockItem> stockItems;
        if (location.equals("in store")) {
            stockItems = getStockItemsInStore();
        } else if (location.equals("storage")) {
            stockItems = getStockItemsFromStorage();
        } else {
            throw new IllegalArgumentException("Invalid location. Aborting stock move operation.");
        }
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(product.getId()) && stockItem.getExpiryDate().equals(expiryDate)) {
                return stockItem.getStockItemId();
            }
        }
        return null;
    }


    public void removeStock(String id) {
        System.out.println("Removing stock item with ID: " + id);
        StockItem stockItemToRemove = stockItemRepository.getStockItemById(id);
        Objects.requireNonNull(stockItemToRemove, "Stock item not found");
        stockItemRepository.deleteStockItem(id);
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
        List<Product> products = productRepository.getAllProducts();
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
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
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
        Product product = productRepository.getProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
        int count = 0;
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(id) && stockItem.getLocation().equals("storage")) {
                count += stockItem.getQuantity();
            }
        }
        return count;
    }

    public int countProductQuantity(String id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
        int count = 0;
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(id) && stockItem.getStatus() == StockItemStatus.OK) {
                count += stockItem.getQuantity();
            }
        }
        return count;
    }

    public int countDefectedProductQuantity(String id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            System.out.println("Product not found.");
            throw new IllegalArgumentException("Product not found.");
        }
        int count = 0;
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProduct().getId().equals(id) && (stockItem.getStatus() != StockItemStatus.OK)) {
                count += stockItem.getQuantity();
            }
        }
        return count;
    }


    public void printDefectedStockItems() {
        System.out.println("Defected stock items:");
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            if (stockItem.getStatus() == StockItemStatus.DAMAGED || stockItem.getStatus() == StockItemStatus.EXPIRED) {
                System.out.println(stockItem);
            }
        }
    }

    public void addDiscount(String discountTargetId, double discountPercentage, String discountDescription,
                            DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate) {
        System.out.println("Adding discount: " + discountDescription);
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        if (discountEndDate.isBefore(discountStartDate)) {
            throw new IllegalArgumentException("Discount end date cannot be before start date.");
        }
        if (type == DiscountTargetType.PRODUCT) {
            Product product = productRepository.getProductById(discountTargetId);
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
                discountStartDate, discountEndDate);
        discountRepository.saveDiscount(discount);
    }

    public void listDiscounts() {
        discountRepository.printAllDiscounts();
    }

    public double getDiscountByProductId(String productId) {
        Product product = productRepository.getProductById(productId);
        Objects.requireNonNull(product, "Product not found");
        List<Discount> discounts = discountRepository.getAllDiscounts();

        double currentPricePercentage = 1; // Will be reverted before returned
        for (Discount discount : discounts) {
            if (!discount.isActive()) {
                continue;
            }
            if (discount.getTargetType() == DiscountTargetType.PRODUCT) {
                if (discount.getTargetId().equals(productId)) {
                    currentPricePercentage *= 1 - (discount.getDiscountPercentage() / 100);
                    break;
                }
            }
        }

        Category currCategory = product.getCategory();
        while (currCategory != null) {
            for (Discount discount : discounts) {
                if (!discount.isActive()) {
                    continue;
                }
                if (discount.getTargetType() == DiscountTargetType.CATEGORY) {
                    if (discount.getTargetId().equals(currCategory.getId())) {
                        currentPricePercentage *= 1 - (discount.getDiscountPercentage() / 100);
                        break;
                    }
                }
            }
            currCategory = currCategory.getParentCategory();
        }

        return 100 * (1 - currentPricePercentage);
    }

    public Product getProductByName(String name, String manufacturer) {
        List<Product> products = productRepository.getAllProducts();
        for (Product product : products) {
            if (product.getName().equals(name) && product.getManufacturer().equals(manufacturer)) {
                return product;
            }
        }
        return null;
    }

    public List<StockItem> getStockItemsFromStorage() {
        List<StockItem> allStockItems = stockItemRepository.getAllStockItems();
        List<StockItem> storageItems = new java.util.ArrayList<>();
        for (StockItem stockItem : allStockItems) {
            if (stockItem.getLocation().equals("storage")) {
                storageItems.add(stockItem);
            }
        }
        return storageItems;
    }

    public List<StockItem> getStockItemsInStore() {
        List<StockItem> allStockItems = stockItemRepository.getAllStockItems();
        List<StockItem> storeItems = new java.util.ArrayList<>();
        for (StockItem stockItem : allStockItems) {
            if (!stockItem.getLocation().equals("storage")) {
                storeItems.add(stockItem);
            }
        }
        return storeItems;
    }

}