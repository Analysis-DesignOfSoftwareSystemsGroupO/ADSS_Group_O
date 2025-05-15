package Presentation;
import Service.TruckManagerService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Console-based UI class for interacting with truck management features.
 * Responsible for displaying a menu and communicating with the TruckManagerService.
 */
public class TruckManagerMenu {
    private final TruckManagerService truckService;

    public TruckManagerMenu(TruckManagerService truckService) {
        this.truckService = truckService;
    }

    /**
     * Displays the truck manager menu and handles user input.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("1. Add new Truck");
            System.out.println("2. Show all Trucks");
            System.out.println("3. Show available Trucks by date");
            System.out.println("4. Remove Truck");
            System.out.println("E. Exit");
            String input = scanner.nextLine();

            try {
                switch (input) {
                    case "1" -> {
                        System.out.println("Enter plate number:");
                        String plate = scanner.nextLine();
                        System.out.println("Enter driving licence code:");
                        String code = scanner.nextLine();
                        System.out.println("Enter max weight:");
                        int weight = Integer.parseInt(scanner.nextLine());

                        truckService.addNewTruck(plate, code, weight);// Delegate to service
                        System.out.println("Truck added.");
                    }
                    case "2" -> {
                        // Show all trucks
                        truckService.getAllTrucks().forEach(System.out::println);
                    }
                    case "3" -> {
                        System.out.println("Enter date (DD/MM/YYYY):");
                        String dateStr = scanner.nextLine();
                        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        var available = truckService.getAvailableTrucksOnDate(date);
                        if (available.isEmpty()) {
                            System.out.println("No trucks available");
                        } else {
                            available.forEach(System.out::println);
                        }
                    }
                    case "4" -> {
                        System.out.println("Enter plate number:");
                        String plate = scanner.nextLine();
                        truckService.removeTruck(plate);
                        System.out.println("Truck removed.");
                    }
                    case "E", "e" -> {
                        running = false;
                        System.out.println("Goodbye.");
                    }
                    default -> System.out.println("Invalid input.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage()); // Show error message
            }
        }
    }
}
