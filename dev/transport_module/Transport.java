package transport_module;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private final Map<String, ProductListDocument> destinations_document_map; // a map for each destination.
    private boolean isSent;

    private boolean isOutOfZone;


    /**
     * a constructor for Transport
     */
    public Transport(String d, String time, Truck t, Site s) throws Exception {
        // input check
        if (time.isEmpty() || d.isEmpty() || t == null || s == null) {
            throw new Exception("Invalid Error");
        }
        driver = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(d, formatter);

        if (parsedDate.isAfter(LocalDate.now())) {
            date = parsedDate;
        } else {
          throw new Exception("Invalid date");
        }

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour < 1 || hour > 24){
            System.out.println("Hour is wrong - changed to default hour - 7");
            hour = 7;
        }
        if (minute < 0 || minute > 59) {
            System.out.println("Minutes is wrong - changed to default minutes - 00");

            minute = 0;
        }
        departure_time = LocalTime.of(hour, minute); // set the hour


        id = ++staticTransportID; // give index to transport
        truck = t; // save the truck as the original truck - not a copy of the truck.
        source = new Site(s); // save the source site as a copy of the site
        destinations_document_map = new HashMap<>();
        isOutOfZone = false;
        isSent = false;
        System.out.println("Transport number " +id+" has successfully created."); // Can't load the truck

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
     * a function that sends transport to its mission
     */
    public void sendTransport() {
        if(isSent)
        {
            System.out.println("Transport has already sent.");
            return;
        }
        if (driver == null) {
            System.out.println("Transport has no driver - please add driver first.");
            return;
        }
        int calcWeight = 0;
        for (String site : destinations_document_map.keySet()) {
            calcWeight += destinations_document_map.get(site).getTotalWeight();
        }
        if (truck.getMaxWeight() < calcWeight) {
            System.out.println("Truck has Over Weight please remove products."); // Can't load the truck
            int difference = calcWeight - truck.getMaxWeight();
            System.out.println("Weight excess by " + difference + " kg");
            return;
        }
        truck.clear();
        isSent = true;
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
        for (String site : destinations_document_map.keySet()) {
            str.append(site).append(" ");
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
    public void loadByDocument(ProductListDocument document) throws Exception {
        if (document == null) throw new Exception("Invalid input");
        if (truck.getMaxWeight() < truck.getWeight() + document.getTotalWeight()) { // if truck is in Over Weight
            System.out.println("Truck has Over Weight please remove products."); // Can't load the truck
            int difference = (truck.getWeight() + document.getTotalWeight()) - truck.getMaxWeight();
            System.out.println("Weight excess by " + difference + " kg");
        } else {
            document.attachTransportToDocument(id);
            destinations_document_map.put(document.getDestination().getName(), document);
            truck.addWeight(document.getTotalWeight());
            System.out.println("Truck has successfully loaded."); // Can't load the truck

            if (!source.getArea().equals(document.getDestination().getArea())) {
                System.out.println("This destination is out of Area Zone, this is a special Transport");
                isOutOfZone = true;
            }
        }

    }

    public ProductListDocument getDocument(String site_name){

        return destinations_document_map.get(site_name);

    }
    public void reduceAmountFromProduct(String destination, Product p, int amount){
        if(destinations_document_map.get(destination)!=null){
            destinations_document_map.get(destination).reduceAmountFromProduct(p,amount);
        }
    }
    public boolean isSiteIsDestination(String site){
        return destinations_document_map.get(site)!=null;

    }
    public void changeDate(String d){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(d, formatter);

        if (parsedDate.isAfter(LocalDate.now())) {
            date = parsedDate;
            System.out.println("Date has been updated to: "+ date);
        } else {
            System.out.println("wrong date - please enter new one");
        }


    }
    public void changeHour(String time){
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        if (hour < 1 || hour > 24){
            System.out.println("Hour is wrong - changed to default hour - 7");
            hour = 7;
        }
        if (minute < 0 || minute > 59) {
            System.out.println("Minutes is wrong - changed to default minutes - 00");

            minute = 0;
        }
        departure_time = LocalTime.of(hour, minute); // set the hour
        System.out.println("Changed delivery time to: "+departure_time);

    }
    public void changeTruck(Truck t){
        if(t!= truck){
            truck = t;
            System.out.println("Truck has ben changed successfully");
        }

    }
    public void changeSourceSite(Site s){
        if (!s.equals(source)){
            source = new Site(s);
        }
    }



}
