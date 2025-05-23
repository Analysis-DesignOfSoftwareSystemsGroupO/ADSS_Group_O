package Presentation;

import Service.TransportManagerService;

import java.util.Scanner;


/**
 * UI menu for the Transport Manager.
 * Provides a text-based menu to manage transport operations such as
 * creating transports, assigning drivers, attaching documents, and handling delays.
 */
public class TransportManagerMenu {

    // Reference to the service layer handling transport logic
    private final TransportManagerService transportService;


    /**
     * Constructor that initializes the transport manager menu with a service instance.
     * @param transportService the service layer used to handle business logic for transport management
     */
    public TransportManagerMenu(TransportManagerService transportService) {
        this.transportService = transportService;
    }


    /**
     * Displays the menu and handles user input for transport management actions.
     * Supported actions: add transport, assign driver, attach document,
     * manually send transport, and view delayed transports.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display menu options
            System.out.println("\nWelcome to Transport Manager Menu!");
            System.out.println("1. Assign Truck to Transport");
            System.out.println("2. Assign Driver to Transport");
            System.out.println("3. Send Transport Manually");
            System.out.println("4. Show Delayed Transports");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            try {
                switch (input) {
                    // Option 1:  Assign truck to transport
                    case "1" -> {
                        System.out.println("Enter truck plate number:");
                        String plate = scanner.nextLine();
                        System.out.println("Enter transport ID:");
                        int transportId = Integer.parseInt(scanner.nextLine());

                        transportService.assignTruckToTransport(plate, transportId);
                        System.out.println("Truck assigned.");
                    }

                    // Option 2: Assign driver to existing transport
                    case "2" -> {
                        System.out.println("Enter driver ID:");
                        String driverId = scanner.nextLine();
                        System.out.println("Enter transport ID:");
                        int transportId = Integer.parseInt(scanner.nextLine());

                        // Delegate to service
                        transportService.assignDriverToTransport(driverId, transportId);
                        System.out.println("Driver assigned.");
                    }

                    // Option 3: Send a transport manually (In case of delay)
                    case "3" -> {
                        System.out.println("Enter transport ID:");
                        int transportId = Integer.parseInt(scanner.nextLine());

                        // Delegate to service
                        transportService.sendTransportManually(transportId);
                        System.out.println("Transport sent.");
                    }

                    // Option 4: Show all delayed transports
                    case "4" -> {
                        // Prints delayed transports (internally handled by service)
                        transportService.printAllDelayedTransports();
                    }

                    // Exit the menu
                    case "E", "e" -> {
                        running = false;
                        System.out.println("Exiting transport menu.");
                    }

                    // Catch invalid input
                    default -> System.out.println("Invalid input. Try again.");
                }
            } catch (Exception e) {
                // General error handler for all menu options
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
