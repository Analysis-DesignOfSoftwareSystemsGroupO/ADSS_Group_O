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
        // Constructor can be used for dependency injection if needed
    }

    public void addProduct(String name, int minimumStock, String parentCategory, double costPrice) {
        System.out.println("Adding product: " + name);
        Category prodParentCategory = getCategoryById(parentCategory);
        if (prodParentCategory == null) {
            throw new IllegalArgumentException("Parent category not found. Aborting product add operation.");
        }

        Product productToAdd = new Product(name, minimumStock, costPrice);
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

    public void saveStockItem(String productName, String productManufacturer, int quantity, String location, StockItemStatus status) {
        System.out.println("Adding stock for product: " + productName);
        Product product = getProductByName(productName, productManufacturer);
        if (product == null) {
            throw new IllegalArgumentException("Product not found. Aborting stock add operation.");
        }
        StockItem stockItemToAdd = new StockItem(quantity, location, status);
        stockItemToAdd.setProduct(product);
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

    public void saveCategory( String catName, String parentCategoryId) {
        System.out.println("Adding category: " + catName);

        Category parentCategory = getCategoryById(parentCategoryId);
        if (!parentCategoryId.isEmpty() && parentCategory == null) {
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

    public void printCurrentStock(){
        System.out.println("Current stock:");
        List<StockItem> stockItems = stockItemRepository.getAllStockItems();
        for (StockItem stockItem : stockItems) {
            System.out.println(stockItem);
        }
    }

    public void addDiscount(String discountTargetId, double discountPercentage, String discountDescription,
                            DiscountTargetType type, LocalDate discountStartDate, LocalDate discountEndDate) {
        System.out.println("Adding discount: " + discountDescription);
        if(discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        if(discountEndDate.isBefore(discountStartDate)) {
            throw new IllegalArgumentException("Discount end date cannot be before start date.");
        }
        if(type == DiscountTargetType.PRODUCT) {
            Product product = productRepository.getProductById(discountTargetId);
            if (product == null) {
                throw new IllegalArgumentException("Product not found. Aborting discount add operation.");
            }
        } else if(type == DiscountTargetType.CATEGORY) {
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
            if (discount.getTargetType() == DiscountTargetType.PRODUCT) {
                if(discount.getTargetId().equals(productId)) {
                    currentPricePercentage *= 1 - (discount.getDiscountPercentage() / 100);
                    break;
                }
            }
        }

        Category currCategory = product.getCategory();
        while (currCategory != null) {
            for (Discount discount : discounts) {
                if (discount.getTargetType() == DiscountTargetType.CATEGORY) {
                    if(discount.getTargetId().equals(currCategory.getId())) {
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
}