package Users;

import Transport_Module_Exceptions.ATransportModuleException;
import Transport_Module_Exceptions.InvalidDateFormatException;
import Transport_Module_Exceptions.InvalidInputException;
import Transport_Module_Exceptions.UnAvailableTruckException;
import transport_module.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface IBookingSystem {

    /**
     * Add new Transport function
     */
    default void AddNewTransport(Map<String, Truck> trucks, Map<Integer, Transport> transports, Map<String, Site> sites, Map<LocalDate, List<Transport>> transportsPerDate, Scanner scanner) throws ATransportModuleException {
        if (trucks == null || transports == null || sites == null || transportsPerDate == null || scanner == null)
            throw new InvalidInputException();
        boolean allTrucksUnAvailabl = true; // boolean variable for
        Truck t = null; // variable for saving truck if available


        System.out.println("Please enter date by format: DD/MM/YYYY");
        String dateStr = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }


        for (String platenumber : trucks.keySet()) // check if there is an available truck.
        {
            Truck truck = trucks.get(platenumber);
            if (truck.getAvailablity(parsedDate)) { // if there is an available truck - stop searching
                allTrucksUnAvailabl = false;
                t = truck;
                break;
            }
        }
        if (allTrucksUnAvailabl) { // if all trucks are not available - throw exception
            throw new UnAvailableTruckException();

        }

        // time input from user
        System.out.println("Please enter date by format: HH:MM");
        String timeStr = scanner.nextLine();

        // source site input from user
        System.out.println("Please enter source site name");
        String site_name = scanner.nextLine();
        if (sites.get(site_name) == null) { // check if there is a site in the system
            throw new InvalidInputException("There is no site " + site_name + " please try again");
        }

        Transport transport = new Transport(dateStr, timeStr, t, sites.get(site_name)); // create new transport with
        transports.put(transport.getId(), transport); // if transport created - add it to transport list
        transportsPerDate.computeIfAbsent(parsedDate, k -> new ArrayList<>()); // if transport created - add it to transport date map
        transportsPerDate.get(parsedDate).add(transport); // add the transport to scheduale


    }


    default void AddDriverToTransport(Map<String, Driver> drivers, Map<Integer, Transport> transports, Scanner scanner) throws ATransportModuleException {
        if (drivers == null || transports == null || scanner == null)
            throw new InvalidInputException();
        // ask for drivers id input

        System.out.println("Please enter driver's id");
        String id = scanner.nextLine();
        Driver driver = drivers.get(id); // search for driver in database
        if (driver == null) { // if driver is not in system
            throw new InvalidInputException("Driver " + id + " is not in system - please try again");
        }
        // ask for transport id from user
        System.out.println("Please enter Transport number");
        Integer transport_id = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        Transport transport = transports.get(transport_id);// get transport from database by transport id
        if (transport == null) { // if transport is not in system
            throw new InvalidInputException("Transport number " + transport_id + " is not in system - please try again");
        }

        transport.addDriver(driver);// try to add driver to transport

    }

    /**
     * a function that attaches a document to transport
     */
    default void AttachDocumentToTransport(Map<Integer, ProductListDocument> documents, Map<Integer, Transport> transports, Scanner scanner) throws ATransportModuleException {
        if (documents == null || transports == null || scanner == null)
            throw new InvalidInputException();
        // ask for transport id from user

        System.out.println("Please enter Transport number");
        Integer transport_id = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        Transport transport = transports.get(transport_id); // search for transport id in database
        if (transport == null) { // if transport not in database - message to user
            throw new InvalidInputException("Transport number " + transport_id + " is not in system - please try again");

        }
        // ask for document id from user
        System.out.println("Please enter document number");
        Integer documentId = scanner.nextInt(); // search document in database
        scanner.nextLine(); // Clear the newline

        if (documents.get(documentId) == null) {
            throw new InvalidInputException("There is no document " + documentId + " Please try again");
        }
        transport.loadByDocument(documents.get(documentId));

    }

    /** a function that sends manually transport*/
    default void sendTransportManual(Map<Integer, Transport> transports, Scanner scanner) throws ATransportModuleException {
        if (transports == null || scanner == null)
            throw new InvalidInputException();
        System.out.println("Please enter Transport number"); // ask for input from user
        int transport_id = scanner.nextInt();
        scanner.nextLine(); // Clear the newline

        Transport transport = transports.get(transport_id); // search transport in database

        if (transport == null) { // if transport not in data base - message to user
            throw new InvalidInputException("Transport number " + transport_id + " is not in system - please try again");
        }
        if(transport.getStatus() == Transport.Status.delayed) {
            try {
                transport.sendTransport();


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


    }


}
