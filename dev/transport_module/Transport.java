package transport_module;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;

public class Transport {
    private static int staticTransportID = 0;
    private final int id;
    private final LocalDate date; // field for date of the transport
    private final LocalTime departure_time; // the hour of departure time
    private Truck truck; // the truck that connects to the transport
    private Driver driver; // the driver that will drive in the truck
    private Site source; // the source site the transport is start
    private Map<Site, ProductListDocument> destinations_document_map; // a map for each destination.

    private boolean isOutOfZone;


    /**
     * a constructor for Transport
     */
    public Transport(String d, String time, Truck t, Site s) throws Exception {
        // input check
        if (time == "" || d == "" || t == null || s == null) {
            throw new Exception("Invalid Error");
        }
        driver = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date =  LocalDate.parse(d, formatter);
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        if (hour < 1 || hour > 24) hour = 7;
        int minute = Integer.parseInt(parts[1]);
        if (minute < 0 || minute > 59) minute = 0;

        id = ++staticTransportID; // give index to transport
        departure_time = LocalTime.of(hour, minute); // set the hour
        truck = t; // save the truck as the original truck - not a copy of the truck.
        source = new Site(s); // save the source site as a copy of the site
        destinations_document_map = new HashMap<>();
        isOutOfZone = false;
        System.out.println("Transport has successfully created."); // Can't load the truck

    }

    /**
     * a function that adds a driver to a transport
     */
    public void addDriver(Driver d) {
        if (d == null) return;
        if (!truck.confirmDriver(d)) {
            System.out.println("Driver's licence doesn't match to truck's licence. please Assign another driver ");
            return;
        }
        if (!driver.isavailable()) {
            System.out.println("Driver is not available - please assign another driver ");
            return;
        }
        driver = d;
        driver.assignToMission();


    }

    /**
     * a function that sends a transport to its mission
     */
    public void sendTransport() {
        if (driver == null) {
            System.out.println("transport has no driver - please add driver first.");
            return;
        }
        int calcWeight = 0;
        for (Site site : destinations_document_map.keySet()) {
            calcWeight += destinations_document_map.get(site).getTotalWeight();
        }
        if (truck.getMaxWeight() < calcWeight) {
            System.out.println("Truck has Over Weight please remove products."); // Can't load the truck
            int difference = calcWeight - truck.getMaxWeight();
            System.out.println("Weight excess by " + difference + " kg");
            return;
        }
        truck.clear();
        driver.release();
    }

    public int getId() {
        return id;
    }

    /**
     * private function that returns all destination details
     */
    private String destinations_string() {
        StringBuilder str = new StringBuilder();
        for (Site site : destinations_document_map.keySet()) {
            str.append(site.toString()).append(" ");
        }
        return str.toString();
    }

    /**
     * a print function that prints all the details about the transport.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(); // build new string
        str.append("Transport num: ").append(id).append("\n"); // start with the transport id
        str.append("Date:  ").append(date).append(" ").append(departure_time).append("\n"); // print the date and hour
        str.append("Truck details: ").append(truck.toString()).append("\n"); // print all truck details
        str.append("Driver details: ").append(driver.toString()).append("\n"); // print all driver details
        str.append("From: ").append(source.toString()).append("\n"); // print the source site details
        str.append("To: ").append(destinations_string()).append("\n"); // print all destination details
        if (isOutOfZone) { // if transport has a destination out of area zone, it will show it.
            str.append("This Transport has a destination out of Area Zone!\n");
        }
        return str.toString();
    }

    /**
     * a function that gets a document and load the products' list to the truck
     */
    public void loadByDocument(ProductListDocument document) {
        if (document == null) return;
        if (truck.getMaxWeight() < truck.getWeight() + document.getTotalWeight()) { // if truck is in Over Weight
            System.out.println("Truck has Over Weight please remove products."); // Can't load the truck
            int difference = (truck.getWeight() + document.getTotalWeight()) - truck.getMaxWeight();
            System.out.println("Weight excess by " + difference + " kg");
        } else {
            destinations_document_map.put(document.getDestination(), document);
            truck.addWeight(document.getTotalWeight());
            System.out.println("Truck has successfully loaded."); // Can't load the truck

            if (!source.getArea().equals(document.getDestination().getArea())) {
                System.out.println("This destination is out of Area Zone, this is a special Transport");
                isOutOfZone = true;
            }
        }
    }
    public boolean isSiteInDestinations(Site s){
        return true;
    }


}
