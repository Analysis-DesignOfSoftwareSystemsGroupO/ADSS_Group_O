package Presentation;
import Service.TruckManagerService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 *  UI menu class for interacting with truck management features.
 * Responsible for displaying a menu and delegating user actions to the TruckManagerService.
 */
public class TruckManagerMenu {

    // Service layer that handles truck-related business logic
    private TruckControllerPL controller;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs the TruckManagerMenu with the provided service instance.
     * @param controller the controller that provides truck management operations
     */
    public TruckManagerMenu(TruckControllerPL controller) {
        this.controller = controller;
    }

    private void addTruck(){
        System.out.println("Enter truck plate number:");
        String plate = scanner.nextLine();

        System.out.println("Enter max weight:");
        int maxWeight = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter driving licence code:");
        String licence = scanner.nextLine();

        controller.addTruck(plate, maxWeight, licence);
    }
    /**
     * Displays the truck manager menu and handles user input.
     * Supported actions: add truck, list all trucks, show available trucks on date, remove truck, and exit.
     */
    public void showMenu() {
        boolean running = true;
        while (running) {
            // Print menu options
            System.out.println("1. Add new Truck");
            System.out.println("2. Show all Trucks");
            System.out.println("3. Show available Trucks by date");
            System.out.println("4. Remove Truck");
            System.out.println("E. Exit");
            String input = scanner.nextLine();

            try {
                switch (input) {

                    // Option 1: Add a new truck to the system
                    case "1" -> addTruck();


                    // Option 2: Show all registered trucks
                    case "2" -> {
                        truckService.getAllTrucks().forEach(System.out::println);
                    }

                    // Option 3: Show trucks available on a specific date
                    case "3" -> {

                        System.out.println("Enter date (DD/MM/YYYY):");
                        String dateStr = scanner.nextLine();

                        // Parse date from string
                        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        var available = truckService.getAvailableTrucksOnDate(date);
                        if (available.isEmpty()) {
                            System.out.println("No trucks available");
                        } else {
                            available.forEach(System.out::println);
                        }
                    }

                    // Option 4: Remove a truck by plate number
                    case "4" -> {
                        System.out.println("Enter plate number:");
                        String plate = scanner.nextLine();
                        truckService.removeTruck(plate);
                        System.out.println("Truck removed.");
                    }

                    // Option E: Exit the menu
                    case "E", "e" -> {
                        running = false;
                        System.out.println("Goodbye.");
                    }

                    // Handle invalid input
                    default -> System.out.println("Invalid input.");
                }
            } catch (Exception e) {
                // Catch and report any runtime exceptions
                System.out.println("Error: " + e.getMessage()); // Show error message
            }
        }
    }
}
