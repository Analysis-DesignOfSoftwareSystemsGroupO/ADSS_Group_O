package inventory.presentation;
import inventory.domain.Product;
import inventory.service.UserApplication;

import java.util.*;

public class InventoryManu {

    private final Scanner scanner;
    private final UserApplication userApplication = new UserApplication();


    public InventoryManu() {
        this.scanner = new Scanner(System.in);
    }


    public void run(){
        System.out.println("\nWelcome to the inventory Management Manu!");;
        int choice = 0;
        do {
            displayMenu();
            choice = readIntInput("Please enter your choice: ");
            handleManuChoice(choice);
        } while (choice != 0);
        System.out.println("Exiting the inventory Management Manu. Goodbye!");

    }

    private void displayMenu() {
        System.out.println("\n---- Inventory Management Menu: ----");
        System.out.println("1.  List Products");
        System.out.println("2.  List Categories");
        System.out.println("3.  Add Product");
        System.out.println("4.  Update Product");
        System.out.println("5.  Delete Product");
        System.out.println("6.  Receive Stock");
        System.out.println("7.  Update Stock");
        System.out.println("8.  Delete Stock");
        System.out.println("9.  Print Stock Report (Category Based)");
        System.out.println("10. Print Order List (Stock Based)");
        System.out.println("11. Print Defect List (Product Based)");
        System.out.println("12. Delete Product");
        System.out.println("13. Delete Product");
        System.out.println("0.  Exit");
    }

    private void handleManuChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    // List Products
                    System.out.println("Listing all products...");
                    List<Product> products = userApplication.getAllProductsDefinitions();
                    for (Product product : products) {
                        System.out.println(product);
                    }
                    break;
                case 2:
                    // List Categories
                    break;
                case 3:
                    // Add Product
                    System.out.print("Enter product ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    int minimumStock = readIntInput("Enter minimum stock: ");
                    // Assuming InventoryController is a class that handles product operations
                    userApplication.addProduct(id, name, minimumStock);
                    System.out.println("Product added successfully!");
                    break;
                case 4:
                    // Update Product
                    break;
                case 5:
                    // Delete Product
                    break;
                case 6:
                    // Receive Stock
                    break;
                case 7:
                    // Update Stock
                    break;
                case 8:
                    // Delete Stock
                    break;
                case 9:
                    // Print Stock Report (Category-Based)
                    break;
                case 10:
                    // Print Order List (Stock-Based)
                    break;
                case 11:
                    // Print Defect List (Product-Based)
                    break;
                case 12:
                    // Delete Product
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

    public static void main(String[] args) {
        // This is the main method where the program starts
        System.out.println("Hello, Inventory!");
        InventoryManu inventoryManu = new InventoryManu();
        inventoryManu.run();






    }
}
