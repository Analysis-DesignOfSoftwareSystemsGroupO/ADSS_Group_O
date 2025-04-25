package inventory.presentation;

//import inventory.domain.Product;

import inventory.data.InMemoryCategoryRepository;
import inventory.domain.Category;
import inventory.domain.Discount;
import inventory.domain.DiscountTargetType;
import inventory.domain.StockItemStatus;
import inventory.service.UserApplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class InventoryManu {

    private final Scanner scanner;
    private final UserApplication service;

    public InventoryManu() {
        this.scanner = new Scanner(System.in);
        this.service = new UserApplication();
    }

    public void run() {
        System.out.println("\nWelcome to the inventory Management Manu!");
        ;
        int choice = 0;
        do {
            displayMenu();
            choice = readIntInput("Please enter your choice: ");
            handleManuChoice(choice);
        } while (choice != 0);
        System.out.println("Exiting the inventory Management Manu. Goodbye!");

    }

    // TODO: implement a login by worker type
    private void logIn() {
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

    }

    private void displayMenu() {
        System.out.println("\n---- Inventory Management Menu: ----");
        System.out.println("1.  List Products");
        System.out.println("2.  List Categories");
        System.out.println("3.  List Current Stock");
        System.out.println("4.  Add Product");
        System.out.println("5.  Update Product");
        System.out.println("6.  Delete Product");
        System.out.println("7.  Add Stock");
        System.out.println("8.  Update Stock");
        System.out.println("9.  Delete Stock");
        System.out.println("10. Print Stock Report (Category Based)");
        System.out.println("11. Print Order List (Stock Based)");
        System.out.println("12. Print Defect List (Product Based)");
        System.out.println("13. Upload Test Data");
        System.out.println("14. Add Category");
        System.out.println("15. Delete Category");
        System.out.println("16. Add Discount");
        System.out.println("17. List Discounts");
        System.out.println("18. Show discount for a product");
        System.out.println("0.  Exit");
    }

    private void handleManuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    // List Products
                    System.out.println("Listing all products...");
                    service.printAllProducts();
                    break;
                case 2:
                    // List Categories
                    System.out.println("Listing all categories...");
                    service.printAllCategories();
                    break;
                case 3:
                    // List Current Stock
                    System.out.println("Listing current stock...");
                    service.printCurrentStock();
                    //TODO: implement better format for the output
                    break;
                case 4:
                    // Add Product
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter product's parent category ID: ");
                    String prodParentCatId = scanner.nextLine();
                    int minimumStock = readIntInput("Enter minimum stock: ");
                    System.out.print("Enter product cost price: ");
                    double costPrice =readDoubleInput("Enter product cost price: ");
                    // Assuming InventoryController is a class that handles product operations
                    service.saveProduct(name, minimumStock, prodParentCatId, costPrice);
                    System.out.println("Product added successfully!");
                    break;
                case 5:
                    // Update Product
//                    TODO: choose weather the update will be specific field or all fields

                case 6:
                    // Delete Product
                    System.out.print("Enter product ID to delete: ");
                    String productIdToDelete = scanner.nextLine();
                    service.removeProduct(productIdToDelete);
                    break;
                case 7:
                    // Create Stock
                    // TODO: make the creation of stock more user friendly (make it use name instead of id)
                    System.out.print("Enter product ID for stock: ");
                    String stockProductId = scanner.nextLine();
                    int stockQuantity = readIntInput("Enter stock quantity: ");
                    System.out.print("Enter stock date (YYYY-MM-DD): ");
                    LocalDate stockDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter stock location: ");
                    String stockLocation = scanner.nextLine();
                    System.out.print("Enter stock condition (OK, DEFECT, EXPIRED): ");
                    StockItemStatus stockStatus = StockItemStatus.valueOf(scanner.nextLine().toUpperCase());
                    service.saveStockItem(stockProductId, stockQuantity, stockLocation, stockStatus, stockDate);
                    System.out.println("Stock added successfully!");
                    break;
                case 8:
                    // Update Stock
                    break;
                case 9:
                    // Delete Stock
                    break;
                case 10:
                    // Print Stock Report (Category-Based)
                    break;
                case 11:
                    // Print Order List (Stock-Based)
                    break;
                case 12:
                    // Print Defect List (Product-Based)
                    break;
                case 13:
                    // Upload Test Data
                    System.out.println("Uploading test data...");
                    service.uploadTestData();
                    break;
                case 14:
                    // Add Category
                    System.out.print("Enter category name: ");
                    String catName = scanner.nextLine();
                    System.out.print("Enter category's parent category ID (don't enter enything for no parent category): ");
                    String parentCatId = scanner.nextLine();
                    service.saveCategory(catName, parentCatId);
                    System.out.println("Category added successfully!");
                    break;
                case 15:
                    // Delete Category
                    System.out.print("Enter category ID: ");
                    String toRemoveCatId = scanner.nextLine();
                    service.deleteCategory(toRemoveCatId);
                    System.out.println("Category added successfully!");
                case 16:
                    // Add Discount
                    System.out.print("Enter 1 for a product discount and 2 for a category discount: ");
                    DiscountTargetType type;
                    String discountChoiceInput = scanner.nextLine();
                    if (discountChoiceInput.equals("1")) {
                        type = DiscountTargetType.PRODUCT;
                    } else if (discountChoiceInput.equals("2")) {
                        type = DiscountTargetType.CATEGORY;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                        break;
                    }
                    System.out.print("Enter discount percentage: ");
                    double discountPercentage = readDoubleInput("Enter discount percentage: ");
                    System.out.print("Enter discount description: ");
                    String discountDescription = scanner.nextLine();
                    System.out.print("Enter target ID (product or category ID): ");
                    String targetID = scanner.nextLine();
                    System.out.print("Enter discount start date (in the format of YYYY-MM-DD): ");
                    LocalDate discountStartDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter discount end date (in the format of YYYY-MM-DD): ");
                    LocalDate discountEndDate = LocalDate.parse(scanner.nextLine());
                    service.addDiscount(targetID, discountPercentage, discountDescription, type,
                            discountStartDate, discountEndDate);
                    System.out.println("Discount added successfully!");
                    break;
                case 17:
                    service.listDiscounts();
                case 18:
                    // Show discount for a product
                    System.out.print("Enter product ID: ");
                    String productId = scanner.nextLine();
                    double discountPercentageForProduct = service.getDiscountByProductId(productId);
                    System.out.println("Discount percentage for product " + productId + ": " + Math.round(discountPercentageForProduct) + "%");
                    break;
                case 0:
                    // Exit
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    public double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        // This is the main method where the program starts
        System.out.println("Hello, Inventory!");
        InventoryManu inventoryManu = new InventoryManu();
        inventoryManu.run();
    }
}
