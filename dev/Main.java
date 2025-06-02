import SupplierMoudleSource.DataBase.LoadData.LoadData;
import SupplierMoudleSource.Presentation.PresentMainMenu;
import inventory.presentation.InventoryMenu;
import inventory.service.UserApplication;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

import static SupplierMoudleSource.DataBase.DatabaseInitializer.createSupplierTables;
import static SupplierMoudleSource.DataBase.DatabaseInitializer.dropAllSupplierTable;
import static inventory.data.connection.DatabaseInitializer.createAllTablesIfNotExists;
import static inventory.data.connection.DatabaseInitializer.dropAllInventoryTables;

public class Main {
    public static void main(String[] args) throws Exception {
        PresentMainMenu supplierMenu = new PresentMainMenu();
        InventoryMenu inventoryMenu = new InventoryMenu("1");
        loadData();

        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.println("*****Checking for Immediate Orders*****");
        inventoryMenu.StartTaskClock();
        Thread.sleep(22000);
        do {
            choice = readIntInput("Welcome to the Application!\n" +
                    "1. Supplier Module\n" +
                    "2. Inventory Module\n" +
                    "3. Exit\n" +
                    "Please enter your choice: \n");

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
        } while (choice != 3);

        scanner.close();
    }

    private static void loadData() throws Exception {
        while (true) {
            Scanner input = new Scanner(System.in);
            while (true) { //load data
                System.out.println("1.Load Data");
                System.out.println("2.Empty Data");
                System.out.println("3.Existing Data");
                System.out.println("4.Exit");
                System.out.println("Enter your choice");
                int choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        dropAllSupplierTable();
                        createSupplierTables();
                        dropAllInventoryTables();
                        createAllTablesIfNotExists();
                        UserApplication service = new UserApplication("1");
                        service.uploadTestData();
                        LoadData loadData = new LoadData();
                        loadData.LoadData();

                        return;
                    case 2:
                        dropAllSupplierTable();
                        createSupplierTables();
                        dropAllInventoryTables();
                        createAllTablesIfNotExists();
                        return;
                    case 3:
                        return;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice !");
                        break;

                }
            }
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

