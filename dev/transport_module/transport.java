package transport_module;

import Transport_Module_Exceptions.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDate;

public class Transport {
    private static int staticTransportID = 0;
    private final int id;
    private LocalDate date; // field for date of the transport
    private LocalTime departure_time; // the hour of departure time
    private Truck truck; // the truck that connects to the transport
    private Driver driver; // the driver that will drive in the truck
    private Site source; // the source site the transport is start
    private final Map<Site, ProductListDocument> destinations_document_map; // a map for each destination.
    private boolean isSent;
    private int currWeight;
    private int maxWeight;

    private boolean isOutOfZone;


    /***
     * Constructor for Transport
     * Initializes a new transport instance with given parameters and checks input validity.
     */
    public Transport(String d, String time, Truck t, Site s) throws ATransportModuleException {
        // input check
        if (time.isEmpty() || d.isEmpty() || t == null || s == null) {
            throw new InvalidInputException();
        }
        driver = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(d, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
        if (parsedDate.isAfter(LocalDate.now())) {
            date = parsedDate;
        } else {
            throw new InvalidDateException("the input date is older than now"); // throw exception invalid date
        }

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour < 1 || hour > 24) {
            System.out.println("Hour is wrong - changed to default hour - 7");
            hour = 7;
        }
        if (minute < 0 || minute > 59) {
            System.out.println("Minutes is wrong - changed to default minutes - 00");

            minute = 0;
        }
        departure_time = LocalTime.of(hour, minute); // set the hour


        if (!t.getAvailablity(date))
            throw new UnAvailableTruckException();
        id = ++staticTransportID; // give index to transport

        truck = t; // save the truck as the original truck - not a copy of the truck.
        truck.setDate(date); // set date at truck schedule
        currWeight = 0;
        maxWeight = t.getMaxWeight();
        source = new Site(s); // save the source site as a copy of the site
        destinations_document_map = new HashMap<>();
        isOutOfZone = false;
        isSent = false;

    }

    //****************************************************************************************************************** Get functions

    /**@return Transport ID
     */
    public int getId() {
        return id;
    }

    /***
     * @return true if the transport was already sent, false otherwise
     */
    public boolean isSent() {
        return isSent;
    }

    /***
     * @return the departure time
     */
    public LocalTime getDeparture_time() {
        return departure_time;
    }

    /***
     * Gets the document associated with a specific destination site name.
     * @param site_name Destination site name
     * @return ProductListDocument if exists, null otherwise
     */
    public ProductListDocument getDocument(String site_name) {

        return destinations_document_map.get(site_name);

    }

    /***
     * @return Date of the transport
     */
    public LocalDate getDate() {
        return date;
    }

    /***
     * Checks if a site is one of the transport's destinations.
     * @param site Site name
     * @return true if site is destination, false otherwise
     */
    public boolean isSiteIsDestination(String site) {
        return destinations_document_map.get(site) != null;

    }
    //****************************************************************************************************************** Set functions

    /***
     * Adds a driver to the transport after verifying license and availability.
     * @param d Driver to add
     * @throws ATransportModuleException if driver's license is invalid or not available
     */
    public void addDriver(Driver d) throws ATransportModuleException {
        if (d == null) return;
        if (!truck.confirmDriver(d)) {
            throw new DriverMismatchException("Driver's licence doesn't match to truck's licence. please Assign another driver");
        }
        if (!d.isavailable(date)) {
            throw new InvalidDriverException("New Driver is not available - please assign another driver ");
        }
        driver = d;
        driver.assignToMission(date);


    }
    /***
     * Changes the truck assigned to this transport.
     * @param t New truck
     * @throws ATransportModuleException if the truck is unavailable or driver license mismatch occurs
     */
    public void changeTruck(Truck t) throws ATransportModuleException {
        if (t != this.truck && t != null) {
            if (!t.getAvailablity(date)) {
                throw new UnAvailableTruckException();

            }
            if (driver != null) {
                try {
                    if (!t.confirmDriver(driver)) {
                        throw new DriverMismatchException("Driver's licence doesn't match to truck's licence. please Assign another driver");
                    }
                } catch (ATransportModuleException e) {
                    throw e;

                }
            }
            truck.releaseTruck(date); // release the previous truck from transport
            truck = t; // save new truck to this transport
            truck.setDate(date); // save the new date in new truck
            maxWeight = t.getMaxWeight(); // change the maximum weight of transport
            if (maxWeight < currWeight)
                System.out.println("Truck has Over Weight");
        }
    }

    /***
     * Sends the transport if all conditions are met: not already sent, has a driver, not overweight.
     * @throws ATransportModuleException if transport cannot be sent
     */
    public void sendTransport() throws ATransportModuleException {
        if (isSent) {
            throw new TransportAlreadySentException();
        }
        if (driver == null) {
            throw new InvalidDriverException("Transport has no driver - please add driver first.");
        }
        int calcWeight = 0;
        for (Site site : destinations_document_map.keySet()) {
            calcWeight += destinations_document_map.get(site).getTotalWeight();
        }
        if (truck.getMaxWeight() < calcWeight) {// Can't load the truck

            throw new OverWeightException(calcWeight - truck.getMaxWeight()); // throw over weight exception

        }
        truck.clear();
        isSent = true;
    }
    /***
     * Loads a document to the transport after weight validation.
     * @param document ProductListDocument to load
     * @throws ATransportModuleException if document is invalid or causes overweight
     */
    public void loadByDocument(ProductListDocument document) throws ATransportModuleException {
        if (document == null)
            throw new InvalidInputException();
        if (maxWeight < currWeight + document.getTotalWeight()) { // if truck is in Over Weight
            throw new OverWeightException((currWeight + document.getTotalWeight()) - maxWeight);

        } else {
            if( destinations_document_map.get(document.getDestination())!= null){ // if destination is already a destination in transport - throw exception
                throw new AlreadyExistDestinationException();
            }
            document.attachTransportToDocument(this);
            destinations_document_map.put(document.getDestination(), document);
            currWeight += document.getTotalWeight();

            if (!source.getArea().equals(document.getDestination().getArea())) {
                System.out.println("This destination is out of Area Zone, this is a special Transport");
                isOutOfZone = true;
            }
        }

    }


    /***
     * Reduces a specified amount of product from a given destination document.
     * @param destination Destination site name
     * @param p Product to reduce
     * @param amount Amount to reduce
     * @throws ATransportModuleException if product cannot be reduced
     */
    public void reduceAmountFromProduct(String destination, Product p, int amount) throws ATransportModuleException {
        if (destinations_document_map.get(destination) != null) {
            destinations_document_map.get(destination).reduceAmountFromProduct(p, amount);
        }
    }


    public void removeDocumentFromTransport(ProductListDocument document) throws ATransportModuleException{
        if(document == null)
            throw new InvalidInputException();
        if(destinations_document_map.get(document.getDestination().getName()) == null){
            return;
        }
        if(!destinations_document_map.get(document.getDestination().getName()).equals(document))
            throw new InvalidInputException();
        if(document.getTransport().equals(this)){
            destinations_document_map.remove(document.getDestination().getName()); // remove document from map
            currWeight-=document.getTotalWeight();// reduce weight from transport

            document.realiseFromTransport(this);
        }

    }



    /***
     * Changes the date of the transport.
     * @param d New date string in "dd/MM/yyyy" format
     * @throws ATransportModuleException if date is invalid
     */
    public void changeDate(String d) throws ATransportModuleException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(d, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }

        if (parsedDate.isAfter(LocalDate.now())) {
            date = parsedDate;
            for(Site site: destinations_document_map.keySet()){ // for each document in transport map - update their date
                destinations_document_map.get(site).changeDate(date);
            }
        } else {
            throw new InvalidDateException("the input date is older than now"); // throw exception invalid date
        }


    }

    /***
     * Changes the hour of the transport.
     * @param time New time string in "HH:MM" format
     */
    public void changeHour(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour < 1 || hour > 24) {
            System.out.println("Hour is wrong - changed to default hour - 7");
            hour = 7;
        }
        if (minute < 0 || minute > 59) {
            System.out.println("Minutes is wrong - changed to default minutes - 00");

            minute = 0;
        }
        departure_time = LocalTime.of(hour, minute); // set the hour
        System.out.println("Changed delivery time to: " + departure_time);

    }
    /***
     * Changes the source site of the transport.
     * @param s New source site
     */
    public void changeSourceSite(Site s) {
        if (!s.equals(source)) {
            source = new Site(s);
        }
    }


    //****************************************************************************************************************** Print functions
    /***
     * Builds a string of all destination site names.
     * @return String listing all destinations
     */
    private String destinations_string() {
        StringBuilder str = new StringBuilder();
        for (Site site : destinations_document_map.keySet()) {
            str.append(site.toString()).append(" ");
        }
        return str.toString();
    }

    /***
     * @return String representation of the transport's details
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(); // build new string
        str.append("Transport num: ").append(id).append("\n"); // start with the transport id
        str.append("Date:  ").append(date).append(" ").append(departure_time).append("\n"); // print the date and hour
        str.append("Truck details: ").append(truck.toString()).append("\n"); // print all truck details
        str.append("Driver details: ");
        if (driver == null)
            str.append("There is no driver\n"); // print all driver details
        else
            str.append(driver.toString()).append("\n"); // print all driver details

        str.append("From: ").append(source.toString()).append("\n"); // print the source site details
        str.append("To: ").append(destinations_string()).append("\n"); // print all destination details
        if (isOutOfZone) { // if transport has a destination out of area zone, it will show it.
            str.append("This Transport has a destination out of Area Zone!\n");
        }
        return str.toString();
    }

    /***
     * Compares two Transport objects based on their ID.
     */
    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Transport that = (Transport) other;
        return this.getId() == that.getId();
    }

    /***
     * @return Hash code based on transport ID
     */
    @Override
    public final int hashCode() {
        return this.getId();
    }


}
