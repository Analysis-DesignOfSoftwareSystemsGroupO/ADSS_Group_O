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
        service.updateDiscounts();
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
                "Print Stock By Product",
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
                "Print Stock By Product",
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
            String productName;
            String productManufacturer;
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
                    //Print stock by Product
                    System.out.println("\n***Print stock by Product***\n");
                    System.out.print("Enter product Name: ");
                    productName = scanner.nextLine();
                    System.out.print("Enter product Manufacturer: ");
                    productManufacturer = scanner.nextLine();
                    service.printStockItemByProductByName(productName, productManufacturer);
                    break;
                case 4:
                    // List Current Stock (REPORT)
                    System.out.println("\nCreating Stock report...\n");
                    service.printCurrentStock();
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
                            System.out.print("Enter stock name: ");
                            productName = scanner.nextLine();
                            System.out.print("Enter product manufacturer: ");
                            productManufacturer = scanner.nextLine();
                            System.out.print("Enter new location (in store/ storage): ");
                            String newLocation = scanner.nextLine();
                            int amount = readIntInput("Enter amount to move: ");
                            service.moveStockItem(productName,productManufacturer, newLocation,amount);
                            break;
                        case 2:
                            // Change Status
                            System.out.println("\n***Change Status***\n");
                            System.out.print("Enter stock name: ");
                            productName = scanner.nextLine();
                            System.out.print("Enter product manufacturer: ");
                            productManufacturer = scanner.nextLine();
                            System.out.print("Enter new status (OK, DAMAGED, EXPIRED): ");
                            StockItemStatus newStatus = StockItemStatus.valueOf(scanner.nextLine().toUpperCase());
                            System.out.print("Enter expiry date (YYYY-MM-DD): ");
                            LocalDate expiryDate = LocalDate.parse(scanner.nextLine());
                            service.changeStockItemStatus(productName,productManufacturer,expiryDate, newStatus);
                            break;
                    }

                case 7:
                    // Delete Stock
                    System.out.println("\n***Delete Stock***\n");
                    System.out.print("Enter Stock ID you'd like to delete: ");
                    String id = scanner.nextLine();
                    service.removeStock(id);
                    break;
                case 8:
                    // Print Defect List (REPORT)
                    service.printDefectedStockItems();
                    break;
                case 9:
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
                    System.out.print("Enter product cost price: ");
                    double costPrice = readDoubleInput("Enter product cost price: ");
                     System.out.print("Enter product location: ");
                    String location = scanner.nextLine();
                    System.out.print("Enter product Manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    // Assuming InventoryController is a class that handles product operations
                    service.saveProduct(name, minimumStock, categoryInfo, costPrice,location,manufacturer);
                    System.out.println("Product added successfully!");
                    break;
                case 10:
                    //Update Product
                    break;
                case 11:
                    // Delete Product
                    System.out.print("Enter product ID to delete: ");
                    String productIdToDelete = scanner.nextLine();
                    service.removeProduct(productIdToDelete);
                    break;
                case 12:
                    // Add Category
                    System.out.print("Enter category name: ");
                    String catName = scanner.nextLine();
                    System.out.print("Enter category's parent category ID (don't enter anything for no parent category): ");
                    String parentCatId = scanner.nextLine();
                    service.saveCategory(catName, parentCatId);
                    System.out.println("Category added successfully!");
                    break;
                case 13:
                    //Update Category // Why do we need ??
                    break;
                case 14:
                    // Delete Category
                    System.out.print("Enter category ID: ");
                    String toRemoveCatId = scanner.nextLine();
                    service.deleteCategory(toRemoveCatId);
                    System.out.println("Category added successfully!");
                    break;
                case 15:
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
                case 16:
                    //Update Discount
                case 17:
                    //Delete Discount
                    break;
                case 18:
                    //List Discount
                    service.listDiscounts();
                case 19:
                    // Upload Test Data
                    System.out.println("Uploading test data...");
                    service.uploadTestData();
                    break;
                case 20:
                    // Show discount for a product
                    System.out.print("Enter product ID: ");
                    String productId = scanner.nextLine();
                    double discountPercentageForProduct = service.getDiscountByProductId(productId);
                    System.out.println("Discount percentage for product " + productId + ": " + Math.round(discountPercentageForProduct) + "%");
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

    public void displayStockUpdateMenu(){
         System.out.println("Choose which of the following you wold like to do:" +
                 "\n 1. Move Items" + "\n 2. Change Status" );
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
