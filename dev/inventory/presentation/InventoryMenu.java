package inventory.presentation;

//import inventory.domain.Product;

import inventory.domain.DiscountTargetType;
import inventory.domain.DiscountType;
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
        int worker = 0;
        int choice = 0;
        do {
            if (choice == 0) {
                worker = chooseWorkerType();
            }
            service.updateDiscounts();
            service.checkForExpiredStock();
            if (worker == 2) {
                displayMenuForWorker();
                choice = readIntInput("Please enter your choice: ");
                handleMenuChoice(choice);
            } else if (worker == 1) {
                displayMenuForManager();
                choice = readIntInput("Please enter your choice: ");
                handleMenuChoice(choice);
            }
        } while (worker != 0);
        System.out.println("Exiting the inventory Management Menu. Goodbye!");

    }

    private int chooseWorkerType() {
        int worker;
        while (true) {
            System.out.println("Choose which worker you are:\n 1. Manager\n 2. Worker\n 0. Return To Main Menu \nEnter choice: ");
            try {
                worker = Integer.parseInt(scanner.nextLine());
                if (worker == 1 || worker == 2 || worker == 0) {
                    return worker;
                }
                System.out.println("Invalid choice. Please enter 1 for Manager or 2 for Worker.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
            }
        }
    }

    private void displayMenuForManager() {
        List<String> menuOptions = Arrays.asList(
                "List Products",
                "List Categories",
                "Print Stock By Product",
                "Reports",
                "Add Stock",
                "Update Stock",
                "Delete Stock",
                "Add Product",
                "Delete Product",
                "Clear Stock (Expired/Defected)",
                "Delete Category",
                "Add Discount",
                "Delete Discount",
                "List Discounts",
                "Show discount for a product",
                "Sell Product");
        System.out.println("\n---- Inventory Worker Management Menu: ----");
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println((i + 1) + ".  " + menuOptions.get(i));
        }
        System.out.println("0.  Return To Worker Selection");
        System.out.println("\tEnter Code For Test Data Upload");
    }

    private void displayMenuForWorker() {
        List<String> menuOptions = Arrays.asList(
                "List Products",
                "List Categories",
                "Print Stock By Product",
                "Reports",
                "Add Stock",
                "Update Stock",
                "Delete Stock"
                );
        System.out.println("\n---- Inventory Management Menu: ----");
        for (int i = 0; i < menuOptions.size(); i++) {
            System.out.println((i + 1) + ".  " + menuOptions.get(i));
        }

        System.out.println("0.  Return To Worker Selection");
        System.out.println("\tEnter Code For Test Data Upload");
    }

    private void handleMenuChoice(int choice) {
        try {
            String productName;
            String productManufacturer;
            String productId;
            switch (choice) {
                case 1:
                    // List Products
                    displayProductsMenu();
                    int productChoice = readIntInput("Please enter your choice: ");
                    switch (productChoice) {
                        case 1:
                            // List All Products
                            System.out.println("\nListing all products...\n");
                            service.printAllProducts();
                            break;
                        case 2:
                            // List Products By Category
                            System.out.println("\nListing products by category...\n");
                            ArrayList<String> categories = new ArrayList<>();
                            while (true) {
                                System.out.println("Enter category name (or 0 to stop):");
                                String categoryInput = scanner.nextLine();
                                categories.add(categoryInput);
                                if (categoryInput.equals("0")) {
                                    break;
                                }
                                service.printProductsByCategories(categories);
                            }
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 2:
                    // List Categories
                    System.out.println("\nListing all categories...\n");
                    service.printAllCategories();
                    break;
                case 3:
                    //Print stock by Product
                    System.out.println("\n***Print stock by Product***\n");
                    System.out.print("Enter product Name: ");
                    productName = scanner.nextLine();
                    System.out.print("Enter product Manufacturer: ");
                    productManufacturer = scanner.nextLine();
                    System.out.println("\n");
                    service.printStockItemByProductByName(productName, productManufacturer);
                    break;
                case 4:
                    // Reports
                    System.out.println("\n***Reports***\n");
                    displayReportsMenu();
                    int reportChoice = readIntInput("Please enter your choice: ");

                    switch (reportChoice) {
                        case 1:
                            // Print Order List
                            System.out.println("\n***Print Order List***\n");
                            service.printOrderList();
                            break;
                        case 2:
                            // Print Current Stock Report
                            System.out.println("\n***Print Current Stock Report***\n");
                            service.printCurrentStock();
                            break;
                        case 3:
                            // Print Expired List (Product-Based)
                            System.out.println("\n***Print Expired List***\n");
                            service.printExpiredStockItems();
                            break;
                        case 4:
                            // Print Defect List (Product-Based)
                            System.out.println("\n***Print Defect List***\n");
                            service.printDefectedStockItems();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 5:
                    // Add Stock
                    System.out.println("\nStarting new Stock creation...\n");
                    System.out.print("Enter product name: ");
                    productName = scanner.nextLine();
                    System.out.print("Enter product manufacturer: ");
                    productManufacturer = scanner.nextLine();
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
                case 6:
                    // Update Stock
                    displayStockUpdateMenu();
                    int updateChoice = readIntInput("Please enter your choice: ");
                    switch (updateChoice) {
                        case 1:
                            // Move Items
                            System.out.println("\n***Move Items***\n");
                            System.out.print("Enter Product name: ");
                            productName = scanner.nextLine();
                            System.out.print("Enter product manufacturer: ");
                            productManufacturer = scanner.nextLine();
                            System.out.print("Enter new location (in store/ storage): ");
                            String newLocation = scanner.nextLine();
                            LocalDate expiryDate = null;
                            if (Objects.equals(newLocation, "storage")) {
                                System.out.println("Enter expiry date (YYYY-MM-DD): ");
                                expiryDate = LocalDate.parse(scanner.nextLine());
                            }
                            int amount = readIntInput("Enter amount to move: ");
                            service.moveStockItem(productName, productManufacturer, newLocation, amount, expiryDate);
                            break;
                        case 2:
                            // Update Defected Items
                            System.out.println("\n***Update Defected Items***\n");
                            System.out.print("Enter Product name: ");
                            productName = scanner.nextLine();
                            System.out.print("Enter product manufacturer: ");
                            productManufacturer = scanner.nextLine();
                            System.out.print("Enter expiry date (YYYY-MM-DD): ");
                            expiryDate = LocalDate.parse(scanner.nextLine());
                            System.out.println("Enter Current location (in store/ storage): ");
                            String currentLocation = scanner.nextLine();
                            int defectedAmount = readIntInput("Enter amount of defected items: ");
                            service.updateInventoryWithDefectiveItems(productName, productManufacturer, currentLocation, expiryDate, defectedAmount);
                            break;
                        case 3:
                            // Check for expired items
                            System.out.println("\nChecking for expired items...\n");
                            service.checkForExpiredStock();
                            System.out.println("Expired items updated successfully!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 7:
                    // Delete Stock
                    System.out.println("\n***Delete Stock***\n");
                    System.out.print("Enter Stock ID you'd like to delete: ");
                    String id = scanner.nextLine();
                    service.removeStock(id);
                    break;
                case 8:
                    // Add Product
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter product's main Category name: ");
                    String prodMainCat = scanner.nextLine();
                    System.out.println("Enter product's sub-Category name: ");
                    String prodSubCat = scanner.nextLine();
                    System.out.println("Enter product's size: ");
                    String prodSizeCat = scanner.nextLine();
                    String[] categoryInfo = {prodMainCat, prodSubCat, prodSizeCat};
                    int minimumStock = readIntInput("Enter minimum stock: ");
                    double costPrice = readDoubleInput("Enter product cost price: ");
                    System.out.print("Enter product location: ");
                    String location = scanner.nextLine();
                    System.out.print("Enter product Manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    // Assuming InventoryController is a class that handles product operations
                    service.saveProduct(name, minimumStock, categoryInfo, costPrice, location, manufacturer);
                    System.out.println("Product added successfully!");
                    break;
                case 9:
                    // Delete Product
                    System.out.print("Enter product ID to delete: ");
                    String productIdToDelete = scanner.nextLine();
                    service.removeProduct(productIdToDelete);
                    break;
                case 10:
                    // Clear Stock (Expired/Defected)
                    System.out.println("Choose which of the following you wold like to do: ");
                    System.out.println("1. Clear Expired Items");
                    System.out.println("2. Clear Defected Items");
                    System.out.println("3. Clear All Items (Expired and Defected)");
                    int clearChoice = readIntInput("Please enter your choice: ");
                    switch (clearChoice) {
                        case 1:
                            System.out.println("Clearing expired items...");
                            service.clearExpiredStock();
                            break;
                        case 2:
                            System.out.println("Clearing defected items...");
                            service.clearDefectedStockItems();
                            break;
                        case 3:
                            System.out.println("Clearing all items...");
                            service.clearExpiredStock();
                            service.clearDefectedStockItems();
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 11:
                    // Delete Category
                    System.out.print("Enter category ID: ");
                    String toRemoveCatId = scanner.nextLine();
                    service.deleteCategory(toRemoveCatId);
                    System.out.println("Category added successfully!");
                    break;
                case 12:
                    // Add Discount
                    System.out.print("Enter 1 for a product discount and 2 for a category discount: ");
                    DiscountTargetType type;
                    DiscountType discountType;
                    String discountChoiceInput = scanner.nextLine();
                    if (discountChoiceInput.equals("1")) {
                        type = DiscountTargetType.PRODUCT;
                    } else if (discountChoiceInput.equals("2")) {
                        type = DiscountTargetType.CATEGORY;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                        break;
                    }
                    double discountPercentage = readDoubleInput("Enter discount percentage: ");
                    System.out.print("Enter discount description: ");
                    String discountDescription = scanner.nextLine();
                    System.out.print("Enter target ID (product or category ID): ");
                    String targetID = scanner.nextLine();
                    System.out.print("Enter discount start date (in the format of YYYY-MM-DD): ");
                    LocalDate discountStartDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("Enter discount end date (in the format of YYYY-MM-DD): ");
                    LocalDate discountEndDate = LocalDate.parse(scanner.nextLine());
                    System.out.println("What is the discount type? (Manufacturer/Store): ");
                    String discountTypeChoice = scanner.nextLine();
                    if (discountTypeChoice.equalsIgnoreCase("manufacturer")) {
                        discountType = DiscountType.MANUFACTURER;
                    } else if (discountTypeChoice.equalsIgnoreCase("store")) {
                        discountType = DiscountType.STORE;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                        break;
                    }
                    service.addDiscount(targetID, discountPercentage, discountDescription, type,
                            discountStartDate, discountEndDate,discountType);
                    System.out.println("Discount added successfully!");
                    break;
                case 13:
                    //Delete Discount
                    System.out.print("Enter discount ID: ");
                    String discountId = scanner.nextLine();
                    service.removeDiscount(discountId);
                    break;
                case 14:
                    //List Discount
                    service.listDiscounts();
                    break;
                case 15:
                    // Show discount for a product
                    System.out.print("Enter product ID: ");
                    productId = scanner.nextLine();
                    double discountPercentageForProduct = service.getDiscountByProductId(productId);
                    System.out.println("Discount percentage for product " + productId + ": " + Math.round(discountPercentageForProduct) + "%");
                    break;
                case 16:
                    //Sell Product
                    System.out.println("Enter product ID: ");
                    productId = scanner.nextLine();
                    int sellAmount = readIntInput("Enter Sell amount: ");
                    service.sellProduct(productId, sellAmount);
                    break;
                case 0:
                    // Return to worker selection
                    break;
                case 1234:
                    // Upload Test Data
                    System.out.println("Uploading test data...");
                    service.uploadTestData();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void displayStockUpdateMenu() {
        System.out.println("""
                Choose which of the following you wold like to do:\
                
                 1. Move Items (same Status)
                 2. Update Defected Items (change status)
                 3. Check For Expired Items""");
    }

    public void displayReportsMenu() {
        System.out.println("""
                Choose which of the following you wold like to do:\
                
                 1. Print Order List
                 2. Print Current Stock Report
                 3. Print Expired List (Product Based)
                 4. Print Defect List (Product Based)""");
    }


    public void displayProductsMenu() {
        System.out.println("""
                
                Choose which of the following you would like to do:
                
                 1. All Products
                 2. Products By Category (Enter multiple categories, 0 to return)""");
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
