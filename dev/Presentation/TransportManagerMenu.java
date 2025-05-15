package Presentation;

import Service.TransportManagerService;

import java.util.Scanner;

/**
 * Console-based UI for the Transport Manager.
 * Interacts with the user and delegates logic to the service layer.
 */
public class TransportManagerMenu {
    private final TransportManagerService transportService;

    public TransportManagerMenu(TransportManagerService transportService) {
        this.transportService = transportService;
    }

    /**
     * Displays menu and handles user input for transport management actions.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Transport Manager Menu ===");
            System.out.println("1. Add new Transport");
            System.out.println("2. Assign Driver to Transport");
            System.out.println("3. Attach Document to Transport");
            System.out.println("4. Send Transport Manually");
            System.out.println("5. Show Delayed Transports");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            try {
                switch (input) {
                    case "1" -> {
                        System.out.println("Enter date (dd/MM/yyyy):");
                        String date = scanner.nextLine();
                        System.out.println("Enter time (HH:mm):");
                        String time = scanner.nextLine();
                        System.out.println("Enter source site name:");
                        String site = scanner.nextLine();
                        transportService.addNewTransport(date, time, site);
                        System.out.println("Transport added.");
                    }

                    case "2" -> {
                        System.out.println("Enter driver ID:");
                        String driverId = scanner.nextLine();
                        System.out.println("Enter transport ID:");
                        int transportId = Integer.parseInt(scanner.nextLine());
                        transportService.assignDriverToTransport(driverId, transportId);
                        System.out.println("Driver assigned.");
                    }

                    case "3" -> {
                        System.out.println("Enter document ID:");
                        int docId = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter transport ID:");
                        int transportId = Integer.parseInt(scanner.nextLine());
                        transportService.attachDocumentToTransport(docId, transportId);
                        System.out.println("Document attached.");
                    }

                    case "4" -> {
                        System.out.println("Enter transport ID:");
                        int transportId = Integer.parseInt(scanner.nextLine());
                        transportService.sendTransportManually(transportId);
                        System.out.println("Transport sent.");
                    }

                    case "5" -> {
                        transportService.printAllDelayedTransports();
                    }

                    case "E", "e" -> {
                        running = false;
                        System.out.println("Exiting transport menu.");
                    }

                    default -> System.out.println("Invalid input. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
