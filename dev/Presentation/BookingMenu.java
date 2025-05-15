package Presentation;

import Service.BookingService;
import Transport_Module_Exceptions.ATransportModuleException;

import java.util.Scanner;

/**
 * Console-based presentation layer for customers to request new transports.
 */
public class BookingMenu {

    private final BookingService bookingService;

    public BookingMenu(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Displays the menu and handles user input for booking a new transport.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Booking Menu ===");
            System.out.println("1. Request new transport");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> handleBooking(scanner);
                case "E", "e" -> {
                    System.out.println("Exiting booking menu.");
                    running = false;
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Handles a single transport booking interaction.
     */
    private void handleBooking(Scanner scanner) {
        try {
            System.out.println("Enter delivery date (dd/MM/yyyy):");
            String date = scanner.nextLine();

            System.out.println("Enter delivery time (HH:mm):");
            String time = scanner.nextLine();

            System.out.println("Enter source site name:");
            String source = scanner.nextLine();

            System.out.println("Enter destination site name:");
            String destination = scanner.nextLine();

            bookingService.requestTransport(date, time, source, destination);

        } catch (ATransportModuleException e) {
            System.out.println("Booking failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
