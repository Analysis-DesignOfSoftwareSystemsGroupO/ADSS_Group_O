package TransportModule.Presentation;

import TransportModule.DTO.ProductDTO;
import TransportModule.Transport_Module_Exceptions.ATransportModuleException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


/**
 * Console-based presentation layer for customers to request new transports.
 * This class provides a simple text-based interface to interact with the BookingService.
 */
public class BookingMenu{

    // Dependency on the BookingService to process transport requests
    private final BookingControllerPL controller;
    private final Scanner scanner = new Scanner(System.in);
    private int transportId;
    private final List<Integer> productsDocumentIdList;
    private int maxWeight;


    /**
     * Constructs a new BookingMenu instance with the provided BookingService.
     */
    public BookingMenu() throws Exception{

        controller = new BookingControllerPL();
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
            System.out.println("Press 1 to Request new transport.");
            System.out.println("Press E to Exit Menu.");

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
                default -> System.out.println("Invalid input. Please try again.\n");
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
            transportId = controller.getNewTransportId();
            // Ask user for transport details
            System.out.println("Enter delivery date (DD/MM/YYYY): ");
            String datestr = scanner.nextLine();
            LocalDate date = LocalDate.parse(datestr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.println("Enter delivery time (HH:MM): ");
            String outtime = scanner.nextLine();

            String[] parts = outtime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            LocalTime departure_time = LocalTime.of(hour, minute); // set the hour


            System.out.println("Enter source site name: ");
            String source = scanner.nextLine();

            // create default PLD function and
            productsDocumentIdList.add(createProductListDocument(date));

            // optional - add more PLD
            ProductListDocumentMenu(date);


            // Submit the transport request to the service

            controller.createTransport(transportId,date,source,maxWeight,departure_time);

            // attach each product list document to transport (even if its empty one)
            controller.attachProductListDocumentsToTransport(productsDocumentIdList, transportId);



        } catch (ATransportModuleException e) {
            // Catch and display known transport-related exceptions
            System.out.println("Booking failed: " + e.getMessage());
        } catch (Exception e) {
            // Catch all other unexpected errors
            System.out.println("Unexpected error: " + e.getMessage());
        }

        // Clear all data for next booking
        productsDocumentIdList.clear();
        transportId = -1;
        maxWeight = 0;
    }



    private void ProductListDocumentMenu(LocalDate date) throws Exception{
        boolean running = true;
        while (running){

            System.out.println("press 1 to create new Delivery document.");
            System.out.println("press E to Finish.");
            String input = scanner.nextLine();
            switch (input){
                case "1"-> {
                    try {
                        productsDocumentIdList.add(createProductListDocument(date));

                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case "E","e" -> {
                    System.out.println("Thank you!");
                    running = false;
                }
                default -> System.out.println("Invalid input. Please try again\n");
            }
        }


    }


    private int createEmptyProductListDocument(LocalDate date) throws Exception{
        System.out.println("Please enter your site destination: ");
        String site = scanner.nextLine();
        System.out.println("Please enter wanted arrival time (HH:MM): ");
        String wantedhour = scanner.nextLine();
        String[] parts = wantedhour.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        LocalTime departure_time = LocalTime.of(hour, minute); // set the hour
        List<ProductDTO> productDTOList = new ArrayList<>();
        return controller.createProductListDocument(transportId,site,productDTOList,0,date,departure_time);



    }



    private int createProductListDocument(LocalDate date) throws Exception{
        boolean running = true;
        System.out.println("Please enter your site destination: ");
        String site = scanner.nextLine();
        System.out.println("Please enter wanted arrival time (HH:MM): ");
        String wantedhour = scanner.nextLine();
        String[] parts = wantedhour.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        LocalTime departure_time = LocalTime.of(hour, minute); // set the hour
        System.out.println("Press 1 if you need Empty transport.");
        String choice = scanner.nextLine();
        if(Objects.equals(choice, "1"))
            running = false;

        int totalWeight = 0;
        List<ProductDTO> productDTOS = new ArrayList<>();
        while (running) {
            System.out.println("Press 1 to add Product.");
            System.out.println("Press E to return.");
            String input = scanner.nextLine();
            switch (input) {
                case "1" -> {
                    System.out.println("Please enter product id: ");
                    String productId = scanner.nextLine();
                    System.out.println("Please enter amount: ");
                    int amount = Integer.parseInt(scanner.nextLine());
                    ProductDTO productDTO = new ProductDTO(productId,5, amount);
                    try {
                        productDTOS.add(productDTO);
                        totalWeight+=productDTO.weight();
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                case "E", "e" -> {
                    System.out.println("Thank you!");
                    running = false;
                }
                default -> System.out.println("Invalid input please try again.\n");
            }
        }

        int ProductListDocumentId = controller.createProductListDocument(-1,site,productDTOS,totalWeight,date,departure_time);

        maxWeight+= totalWeight; // add the weight to total weight
        return ProductListDocumentId;

    }




}