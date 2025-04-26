package inventory.presentation;

//import inventory.domain.Product;

import inventory.domain.DiscountTargetType;
import inventory.domain.StockItemStatus;
import inventory.service.UserApplication;

import java.time.LocalDate;
import java.util.*;

public class InventoryMenu {

    private final Scanner scanner;
    private final UserApplication service;

    public InventoryMenu() {
        this.scanner = new Scanner(System.in);
        this.service = new UserApplication();
    }

    public void run() {
        System.out.println("\nWelcome to the inventory Management Menu!");
        int worker = chooseWorkerType();
        int choice = 0;
        do {
            if(worker == 2) {
                displayMenuForWorker();
                choice = readIntInput("Please enter your choice: ");
                handleMenuChoice(choice);
            } else if (worker == 1) {
                displayMenuForManager();
                choice = readIntInput("Please enter your choice: ");
                handleMenuChoice(choice);
            } else {
                System.out.println("Invalid choice. Please try again.");
                break;
            }


        } while (choice != 0);
        System.out.println("Exiting the inventory Management Menu. Goodbye!");

    }

    private int chooseWorkerType() {
        System.out.println("Choose which worker you are:\n 1. Manager\n 2. Worker\nEnter choice: ");
        String worker = scanner.nextLine();
        return Integer.parseInt(worker);
    }

    private void displayMenuForManager() {
        List<String> menuOptions = Arrays.asList(
                "List Products",
                "List Categories",
                "Print Current Stock Report",
                "Add Stock",
                "Update Stock",
                "Delete Stock",
                "Print Defect List (Product Based)",
                "Add Product",
                "Update Product",
                "Delete Product",
                "Add Category",
                "Update Category",
                "Delete Category",
                "Add Discount",
                "Update Discount",
                "Delete Discount",
                "List Discounts",
                "Upload Test Data",
                "Add Category",
                "Delete Category",
                "Add Discount",
                "List Discounts",
                "Show discount for a product");
        System.out.println("\n---- Inventory Worker Management Menu: ----");
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println((i + 1) + ".  " + menuOptions.get(i));
        }
        System.out.println("0.  Exit");
    }

    private void displayMenuForWorker() {
        List<String> menuOptions = Arrays.asList(
                "List Products",
                "List Categories",
                "Print Current Stock Report",
                "Add Stock",
                "Update Stock",
                "Delete Stock",
                "Print Defect List (Product Based)"
);
        System.out.println("\n---- Inventory Management Menu: ----");
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println((i + 1) + ".  " + menuOptions.get(i));
        }

        System.out.println("0.  Exit");
    }

    private void handleMenuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    // List Products
                    System.out.println("\nListing all products...\n");
                    service.printAllProducts();
                    break;
                case 2:
                    // List Categories
                    System.out.println("\nListing all categories...\n");
                    service.printAllCategories();
                    break;
                case 3:
                    // List Current Stock (REPORT)
                    System.out.println("\nCreating Stock report...\n");
                    service.printCurrentStock();
                    break;
                case 4:
                     // Add Stock
                    System.out.println("\nStarting new Stock creation...\n");
                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter product manufacturer: ");
                    String productManufacturer = scanner.nextLine();
                    int stockQuantity = readIntInput("Enter stock quantity: ");
                    System.out.print("Enter stock date (YYYY-MM-DD): ");
                    LocalDate stockDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter stock location (in store, storage): ");
                    String stockLocation = scanner.nextLine();
                    System.out.print("Enter stock condition (OK, DEFECT, EXPIRED): ");
                    StockItemStatus stockStatus = StockItemStatus.valueOf(scanner.nextLine().toUpperCase());
                    service.saveStockItem(productName, productManufacturer, stockQuantity, stockLocation, stockStatus, stockDate);
                    System.out.println("Stock added successfully!");
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
                     System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter product manufacturer: ");
                    String productManufacturer = scanner.nextLine();
                    int stockQuantity = readIntInput("Enter stock quantity: ");
                    System.out.print("Enter stock date (YYYY-MM-DD): ");
                    LocalDate stockDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter stock location: ");
                    String stockLocation = scanner.nextLine();
                    System.out.print("Enter stock condition (OK, DEFECT, EXPIRED): ");
                    StockItemStatus stockStatus = StockItemStatus.valueOf(scanner.nextLine().toUpperCase());
                    service.saveStockItem(productName, productManufacturer, stockQuantity, stockLocation, stockStatus, stockDate);
                    System.out.println("Stock added successfully!");
                    break;
                case 8:
                    // Update Stock
                    break;
                case 9:
                    //Update Product
                    break;
                case 10:
                    // Print Stock Report (Category-Based)
                    break;
                case 11:
                    // Print Order List (Stock-Based)
                    break;
                case 12:
                    service.printDefectedStockItems();
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
                    System.out.print("Enter category's parent category ID (don't enter anything for no parent category): ");
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
        InventoryMenu inventoryMenu = new InventoryMenu();
        inventoryMenu.run();
    }
}
