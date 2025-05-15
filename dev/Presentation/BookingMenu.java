package Presentation;

import Service.BookingService;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Scanner;

/**
 * Console-based presentation layer for customers to request new transports.
 * This class provides a simple text-based interface to interact with the BookingService.
 */
public class BookingMenu {

    // Dependency on the BookingService to process transport requests
    private final BookingService bookingService;

    /**
     * Constructs a new BookingMenu instance with the provided BookingService.
     * @param bookingService the service responsible for handling transport bookings
     */
    public BookingMenu(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Displays the booking menu and handles user interaction via console input.
     * Allows users to request a new transport or exit the menu.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            // Display the main menu options
            System.out.println("Welcome to Booking Menu!");
            System.out.println("1. Request new transport");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            switch (input) {

                // Option to request a new transport
                case "1" -> handleBooking(scanner);

                // Exit the menu (case-insensitive)
                case "E", "e" -> {
                    System.out.println("Exiting booking menu.");
                    running = false;
                }

                // Handle invalid inputs
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Handles the flow for booking a new transport.
     * Prompts the user for date, time, source, and destination site names,
     * and delegates the request to the BookingService.
     *
     * @param scanner the Scanner used to read user input
     */
    private void handleBooking(Scanner scanner) {
        try {
            // Ask user for transport details
            System.out.println("Enter delivery date (dd/MM/yyyy):");
            String date = scanner.nextLine();

            System.out.println("Enter delivery time (HH:mm):");
            String time = scanner.nextLine();

            System.out.println("Enter source site name:");
            String source = scanner.nextLine();

            System.out.println("Enter destination site name:");
            String destination = scanner.nextLine();

            // Submit the transport request to the service
            bookingService.requestTransport(date, time, source, destination);

        } catch (ATransportModuleException e) {
            // Catch and display known transport-related exceptions
            System.out.println("Booking failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch all other unexpected errors
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
