package transport_module;

import javax.swing.text.Document;
import java.util.*;
import java.time.LocalTime;

public class Transport {
    private static int staticTransportID = 0;
    private int id;
    private Date date; // field for date of the transport
    private LocalTime departure_time; // the hour of departure time
    private Truck truck; // the truck which connect to the transport
    private Driver driver; // the driver taht will drive in the truck
    private Site source; // the source site the transport is start
    private Map<Site, List<Product>> destinations_products_map; // a list of all destinations
    private boolean isOutOfZone;

    /**
     * a constructor for Transport
     */
    public Transport(Date d, int h, int m, Truck t, Driver e, Site s) throws Exception {
        // input check
        if (d == null || h < 1 || h > 24 || m < 0 || m > 59 || t == null || e == null || s == null) {
            throw new Exception("Invalid Error");
        }
        // check for match between the driver's license and the truck license
        if (t.confirm_driver(e) == false) {
            throw new Exception("The driver does not have an appropriate license for the type of truck");
        }

        date = new Date(d.getTime()); // set the date as a new date.
        id = ++staticTransportID; // give index to transport
        departure_time = LocalTime.of(h, m); // set the hour
        truck = t; // save the truck as the original truck - not a copy of the truck.
        driver = e; // save the driver as the original employee - not a copy of the employee
        source = new Site(s); // save the source site as a copy of the site
        destinations_products_map = new HashMap<>(); // a map for all destinations. the key is destination and value is product's list
        isOutOfZone = false;
    }

    /**
     * a function that adds new destination to map - if the destination already exists - nothing happens
     */
    public void add_destination(Site d) {
        if (null == d) return; // input check
        if (d.getArea() != source.getArea()) { // if the new destination is from a different area (not as the source's area)
            isOutOfZone = true;
            System.out.println("A destination outside the distribution area was entered.");
        }
        // if the destination is not in the map
        destinations_products_map.computeIfAbsent(d, k -> new ArrayList<>());
    }

    /**
     * function that adds a product to load on track
     */
    public void add_product(Site d, Product p) {

        if (null == d || null == p) { // input check
            return;
        }

        add_destination(d); // add the destination if needed
        List<Product> products_list = destinations_products_map.get(d);
        boolean is_update = false;

        for (Product product : products_list) {

            if (product.getName() == p.getName()) { // if the product is already in the list
                product.addWeight(p.getWeight()); // raise the total weight of the product on truck
                is_update = true;
                break;
            }

        }
        if (!is_update) {
            destinations_products_map.get(d).add(new Product(p)); // add the product to the destination list if its not in the list
        }
        if (truck.getMaxWeight() < truck.getWeight() + p.getWeight()) {// if the truck has over weight - need a decision
            System.out.print("The truck is overweight - the item ");
            System.out.print(p.getName());
            System.out.println(" was not loaded onto the truck.");

        } else {
            truck.addWeight(p.getWeight()); // update the total weight of the truck
            System.out.print("The item ");
            System.out.print(p.getName());
            System.out.println(" was inserted successfully.");
        }


    }

    /**
     * private function that returns all destination details
     */
    private String destinations_string() {
        StringBuilder str = new StringBuilder();
        for (Site site : destinations_products_map.keySet()) {
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
     * a function that creates a Form to destination
     */
    public String createDestinationForm(String d) {
        if (Objects.equals(d, "")) return ""; // for wrong input return ""
        Site destination = new Site(d);
        StringBuilder str = new StringBuilder();
        for (Site site : destinations_products_map.keySet()) {//search for the destination in the transport
            if (site.equals(destination)) { // if the site is in the destination list
                str.append(site.toString()).append(" \nProducts list: "); //add all the products in transport into the string
                for (Product product : destinations_products_map.get(destination)) {
                    str.append(product.toString()).append("\n");
                }

            }
        }
        return str.toString();
    }
    public void addPruductsByList(ProductListDocument document) throws Exception{
        if (document == null) throw new Exception("invalid Document");
        ArrayList<Product> currList = document.getProductsList();
        for (Product product : currList){
            add_product(document.getDestination(),product);
        }

    }


}
