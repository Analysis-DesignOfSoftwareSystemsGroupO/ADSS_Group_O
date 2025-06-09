package TransportModule.Presentation;

import TransportModule.DTO.ProductListDocumentDto;
import TransportModule.DTO.TransportDTO;

import java.util.List;
import java.util.Objects;
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

            System.out.println("\nWelcome to Transport Manager Menu.");
            System.out.println("1. Show all next week Transports.");
            System.out.println("2. Show all next week Transports With no Trucks.");
            System.out.println("3. Show all next week Transports With no Driver.");
            System.out.println("4. Remove Transport.");
            System.out.println("E. Exit.");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> ShowAllNextWeekTransports();
                case "2" -> ShowAllNextWeekTransportsWithNoTrucks();
                case "3" -> ShowAllNextWeekTransportsWithNoDrivers();
                case "4" -> RemoveTransportById();
                case "E", "e" -> running = false;
                default -> System.out.println("Invalid option, try again.\n");
            }
        }
    }


    private void ShowAllNextWeekTransports() {
        try {
            controller.PrintTransportDTOList(controller.getNextWeekTransports());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void ShowAllNextWeekTransportsWithNoTrucks() {
        try {
            controller.PrintTransportDTOList(controller.getNextWeekTransportsWithNoTrucks());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void ShowAllNextWeekTransportsWithNoDrivers() {
        try {
            controller.PrintTransportDTOList(controller.getNextWeekTransportsWithNoDrivers());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void RemoveTransportById(){
        System.out.println("Please enter Transport id you want to delete: ");
        String transportId = scanner.nextLine();
        try{
            controller.removeTransportById(transportId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}