import SupplierMoudleSource.Presentation.PresentMainMenu;
import inventory.presentation.InventoryMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        PresentMainMenu supplierMenu = new PresentMainMenu();
        InventoryMenu inventoryMenu = new InventoryMenu();
        int choice = readIntInput("Welcome to the Application!\n" +
                "1. Supplier Module\n" +
                "2. Inventory Module\n" +
                "3. Exit\n" +
                "Please enter your choice: ");

        switch (choice) {
            case 1:
                // Run Supplier Module
                supplierMenu.runMainPresentation();
                break;
            case 2:
                // Run Inventory Module
                inventoryMenu.run();
                break;
            case 3:
                // Exit the application
                System.out.println("Exiting the application. Goodbye!");
                return;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private static int readIntInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

}
