package Presentation;

import Service.TransportManagerService;

import java.util.Scanner;


/**
 * UI menu for the Transport Manager.
 * Provides a text-based menu to manage transport operations such as
 * creating transports, assigning drivers, attaching documents, and handling delays.
 */
import java.util.Scanner;

public class TransportManagerMenu {

    private final TransportManagerControllerPL controller;
    private final Scanner scanner = new Scanner(System.in);

    public TransportManagerMenu() {
        this.controller = new TransportManagerControllerPL();
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome to Transport Manager Menu");
            System.out.println("1. Request available drivers from HR");
            System.out.println("2. Assign driver to transport");
            System.out.println("E. Exit");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> requestDriversFromHR();
                case "2" -> assignDriverToTransport();
                case "E", "e" -> running = false;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    private void requestDriversFromHR() {
        try {
            controller.requestDriversFromHR();
        } catch (Exception e) {
            System.out.println("Failed to request drivers: " + e.getMessage());
        }
    }

    private void assignDriverToTransport() {
        try {
            System.out.println("Enter driver ID:");
            String driverId = scanner.nextLine();

            System.out.println("Enter transport ID:");
            int transportId = Integer.parseInt(scanner.nextLine());

            controller.assignDriverToTransport(driverId, transportId);
            System.out.println("Driver assigned successfully.");
        } catch (Exception e) {
            System.out.println("Error assigning driver: " + e.getMessage());
        }
    }
}