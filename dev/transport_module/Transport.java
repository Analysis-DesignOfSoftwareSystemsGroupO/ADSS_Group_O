package transport_module;

import java.util.ArrayList;
import java.util.Date;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Transport {
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

        truck.addWeight(p.getWeight()); // update the total weight of the truck
        if (truck.overWeight()) { // if the truck has over weight - need a decision
            System.out.println("Truck has Over load - item was");
            // todo - dropitem()
        }


    }


}
