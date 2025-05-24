package Presentation;
import DTO.TruckDto;
import Service.TruckManagerService;
import Transport_Module_Exceptions.ATransportModuleException;

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

        try { // try to add new truck to system
            controller.addTruck(plate, maxWeight, licence);
            System.out.println("Truck has added successfully");
        }
        catch (Exception e){ // catch the Exception if threw
            System.out.println(e.getMessage());
        }

    }
    private void printTrucksArrayOfDTO(TruckDto[] trucks){
        if (trucks == null)
            return;
        for (TruckDto currTruck : trucks) {
            System.out.println("Truck number: " + currTruck.getPlateNumber() + " Licence " + currTruck.getLiceenceReq());
            System.out.println(currTruck.getWeight() + "/" + currTruck.getMaxWeight() + " weight");
        }
    }

    private void showAllTrucks(){
        try {
            // Get from controller all trucks
            TruckDto[] trucks = controller.getAllTrucks();

            // for each truck - print its details
            printTrucksArrayOfDTO(trucks);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    private void showavailableTrucks(){
        System.out.println("Enter date (DD/MM/YYYY):");
        String dateStr = scanner.nextLine();

        // Parse date from string
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        try {
            // get all trucks from controller
            TruckDto[] trucks = controller.getAllAvailableTrucks(dateStr);
            printTrucksArrayOfDTO(trucks); // print all trucks
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void deleteTruck(){
        System.out.println("Enter plate number:");
        String plate = scanner.nextLine();
        try{
            controller.deleteTruck(plate);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


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

                    case "1" -> addTruck();

                    case "2" -> showAllTrucks();

                    case "3" -> showAllTrucks();

                    case "4" -> deleteTruck();


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
