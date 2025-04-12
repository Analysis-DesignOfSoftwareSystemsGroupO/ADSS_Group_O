package transport_module;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Transport {
    private static int staticTransportID = 0;
    private int id;
    private Date date; // field for date of the transport
    private LocalTime departure_time; // the hour of departure time
    private Truck truck; // the truck which connect to the transport
    private Employee driver; // the driver taht will drive in the truck
    private Site source; // the source site the transport is start
    private Map<Site, List<Product>> destinations_products_map; // a list of all destinations
    private boolean isOutOfZone;

    /**
     * a constructor for Transport
     */
    public Transport(Date d, int h, int m, Truck t, Employee e, Site s) throws Exception {
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
            System.out.println("The truck is overweight - the item was not loaded onto the truck.");

        } else {
            truck.addWeight(p.getWeight()); // update the total weight of the truck
            System.out.println("The item was inserted successfully.");
        }


    }
    private String destinations_string(){
        StringBuilder str = new StringBuilder();
        for (Site site : destinations_products_map.keySet()){
            str.append(site.toString()).append(" ");
        }
        return str.toString();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Transport num: ").append(id).append("\n");
        str.append("Date:  ").append(date).append(" ").append(departure_time).append("\n");
        str.append("Truck details: ").append(truck.toString()).append("\n");
        str.append("Driver details: ").append(driver.toString()).append("\n");
        str.append("From: ").append(source.toString()).append("\n");
        str.append("To: ").append(destinations_string()).append("\n");
        if (isOutOfZone){
            str.append("This Transport has a destination out of Area Zone!");
        }
        return str.toString();
    }
    /**a function that create a Form to destination*/
    public String createDestinationForm(Site destination){
        if(destination == null) return ""; // for wrong input return ""
        StringBuilder str = new StringBuilder();
        for (Site site : destinations_products_map.keySet()){//search for the destination in the transport
            if (destination.equals(site)){ // if the site is in the destination list
                str.append(site.toString()).append(" \nProducts list: "); //add all the products in transport into the string
                for (Product product : destinations_products_map.get(destination)){
                    str.append(product.toString()).append("\n");
                }

            }
        }
        return str.toString();
    }


}
