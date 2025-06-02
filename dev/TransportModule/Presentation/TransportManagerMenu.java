package TransportModule.Presentation;

import java.util.Scanner;


/**
 * UI menu for the Transport Manager.
 * Provides a text-based menu to manage transport operations such as
 * creating transports, assigning drivers, attaching documents, and handling delays.
 */

public class TransportManagerMenu {

    private final TransportManagerControllerPL controller;
    private final Scanner scanner = new Scanner(System.in);

    public TransportManagerMenu() throws Exception {
        this.controller = new TransportManagerControllerPL();
    }

    public void showMenu() {
        boolean running = true;
        while (running) {

            System.out.println("\nWelcome to Transport Manager Menu");
            System.out.println("E. Exit");

            String input = scanner.nextLine();

            switch (input) {
                case "E", "e" -> running = false;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }




}