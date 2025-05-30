package Presentation;

import DTO.ProductDTO;
import Service.BookingService;
import Transport_Module_Exceptions.ATransportModuleException;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Object.*;

/**
 * Console-based presentation layer for customers to request new transports.
 * This class provides a simple text-based interface to interact with the BookingService.
 */
public class BookingMenu {

    // Dependency on the BookingService to process transport requests
    private BookingControllerPL controller;
    private final Scanner scanner = new Scanner(System.in);
    private String d; // date variable for Transport request
    private int transportId;
    private List<Integer> productsDocumentIdList;
    private int maxWeight;


    /**
     * Constructs a new BookingMenu instance with the provided BookingService.
     */
    public BookingMenu() {

        controller = new BookingControllerPL();
        this.controller = controller;
        d = "";
        transportId = -1;
        maxWeight = 0;
        productsDocumentIdList = new ArrayList<>();
    }

    /**
     * Displays the booking menu and handles user interaction via console input.
     * Allows users to request a new transport or exit the menu.
     */
    public void showMenu() {
        boolean running = true;

        while (running) {

            // Display the main menu options
            System.out.println("Welcome to Booking Menu!");
            System.out.println("Press 1 to Request new transport");
            System.out.println("Press E to Exit Menu");

            String input = scanner.nextLine();

            switch (input) {

                // Option to request a new transport
                case "1" -> handleBooking();

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
     */
    private void handleBooking() {
        try {

            // Ask user for transport details
            System.out.println("Enter delivery date (dd/MM/yyyy):");
            String datestr = scanner.nextLine();
            LocalDate date;
            try
            {
                date = LocalDate.parse(datestr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                return;
            }

            System.out.println("Enter delivery time (HH:mm):");
            String outtime = scanner.nextLine();

            String[] parts = outtime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            LocalTime departure_time = LocalTime.of(hour, minute); // set the hour


            System.out.println("Enter source site name:");
            String source = scanner.nextLine();

            // let the user choose if send it empty or not
            System.out.println("Press 1 To add Products to transport");
            System.out.println("Press any key to return to Booking Transport Menu");
            String input = scanner.nextLine();
            if(Objects.equals(input, "1")){
                ProductListDocumentMenu();
            }
            else {
                productsDocumentIdList.add(createEmptyProductListDocument());
            }


            // Submit the transport request to the service
            this.transportId = controller.createTransport(date,departure_time,source,maxWeight);
            this.d = datestr;

            for (int ProductListDocumentId : productsDocumentIdList) // attach each product list document to transport( even if its empty one)
                controller.attachProductListDocumentToTransport(ProductListDocumentId, transportId);



        } catch (ATransportModuleException e) {
            // Catch and display known transport-related exceptions
            System.out.println("Booking failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch all other unexpected errors
            System.out.println("Unexpected error: " + e.getMessage());
        }
        productsDocumentIdList.clear();
        transportId = -1;
        d = "";
    }



    private void ProductListDocumentMenu(){
        boolean running = true;
        while (running){
            System.out.println("Welcome to Delivery document!");
            System.out.println("press 1 to create new Delivery document");
            System.out.println("press E to Finish");
            String input = scanner.nextLine();
            switch (input){
                case "1"-> {
                    try {
                        productsDocumentIdList.add(createProductListDocument());

                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case "E","e" -> {
                    System.out.println("Thank you!");
                    running = false;
                }
                default -> System.out.println("Invalid input. Please try again");
            }
        }
    }


    private int createEmptyProductListDocument() throws Exception{
        System.out.println("Please enter your site destination");
        String site = scanner.nextLine();
        System.out.println("Please enter wanted hour ");
        String wantedhour = scanner.nextLine();
        int ProductListDocumentId = 0;
        ProductListDocumentId = controller.createProductListDocument(site, wantedhour, d);

        return ProductListDocumentId;

    }

    private int createProductListDocument() throws Exception{
        boolean running = true;
        System.out.println("Please enter your site destination");
        String site = scanner.nextLine();
        System.out.println("Please enter wanted hour ");
        String wantedhour = scanner.nextLine();
        int ProductListDocumentId = 0;
        ProductListDocumentId = controller.createProductListDocument(site, wantedhour, d);
        while (running) {
            System.out.println("Press 1 to add Product");
            System.out.println("Press E to return");
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> {
                    System.out.println("Please enter product id");
                    String productId = scanner.nextLine();
                    System.out.println("Please enter amount");
                    int amount = Integer.parseInt(scanner.nextLine());
                    ProductDTO productDTO = new ProductDTO(productId,5, amount);
                    try {
                        controller.addProductToDocument(productDTO, ProductListDocumentId);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case "E", "e" -> {
                    System.out.println("Thank you!");
                    running = false;
                }
                default -> System.out.println("Invalid input please try again");
            }
        }

        maxWeight+= controller.getProductListDocumentWeight(ProductListDocumentId); // add the weight to total weight
        return ProductListDocumentId;

    }




}
