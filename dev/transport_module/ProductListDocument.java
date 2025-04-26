package transport_module;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import Transport_Module_Exceptions.*;

public class ProductListDocument {
    public static int documentID = 0; // global variable for indexing documents.
    private final int id; // document id
    private Site destination; // document destination
    private final Map<Product, Integer> productHashMap; // a map of products and amount of each product
    private int totalWeight; // total weight of the products in document
    private int transportId;
    private LocalDate date;

    /***
     * Constructor - creates a new ProductListDocument
     * @param site Destination site
     * @param d Date string in "dd/MM/yyyy" format
     * @throws ATransportModuleException if input is invalid
     */
    public ProductListDocument(Site site, String d) throws ATransportModuleException {
        if (site == null)
            throw new InvalidInputException(); // if the site is null - don't create a document

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

        id = ++documentID; // give index to document
        destination = new Site(site); // set the destination of the document
        productHashMap = new HashMap<>(); // create a map for the document
        totalWeight = 0; // set the total weight to document
        transportId = -1;
        System.out.println("Document number " + id + " has successfully created!");
    }
    //****************************************************************************************************************** Get functions

    /***
     * @return Date of the shipment
     */
    public LocalDate getDate() {
        return date;
    }
    /***
     * @return Transport ID attached to the document, or -1 if none
     */
    public int getTransportId() {
        return transportId;
    }

    /***
     * @return Total weight of all products in the document
     */
    public int getTotalWeight() {
        return totalWeight;
    }

    /***
     * @return Document ID
     */
    public int getId() {
        return id;
    }

    /***
     * @return Destination site
     */
    public Site getDestination() {
        return destination;
    }

//********************************************************************************************************************** Set functions


    /***
     * Attaches the document to a given transport
     * @param transport Transport to attach
     * @throws ATransportModuleException if transport is null or dates mismatch
     */
    public void attachTransportToDocument(Transport transport) throws ATransportModuleException {
        if (transport == null) {
            throw new InvalidInputException();
        }
        if (!transport.getDate().equals(date)) {
            throw new TransportMismatchException("Transport date doesn't match to document shipment date.");
        }
        transportId = transport.getId();
    }

    /***
     * Adds a product with a specific amount to the document
     * @param p Product to add
     * @param amount Amount to add
     * @throws ATransportModuleException if input is invalid
     */
    public void addProduct(Product p, int amount) throws ATransportModuleException {
        if (p == null || amount < 1) {
            throw new InvalidInputException();
        }
        Integer newAmount = productHashMap.get(p);
        if (newAmount != null)
            newAmount += amount;
        else
            newAmount = amount;
        productHashMap.put(p, newAmount);
        totalWeight += amount * p.getWeight();
        System.out.println("Item has successfully added to document ");


    }

    /***
     * Reduces the amount of a product from the document.
     * Removes the product entirely if quantity reaches zero.
     * @param p Product to reduce
     * @param amount Amount to remove
     * @throws ATransportModuleException if invalid operation
     */
    public void reduceAmountFromProduct(Product p, int amount) throws ATransportModuleException {
        if (p == null || amount < 1) {
            throw new InvalidInputException();

        }
        if (productHashMap.get(p) == null) {
            throw new ProductNotFoundException();
        }
        if (amount > productHashMap.get(p)) {
            throw new InvalidAmountException("amount is not match to product amount in document");

        }

        if (amount == productHashMap.get(p)) {
            System.out.println("Product has removed");
            productHashMap.remove(p);
            return;
        }
        int newAmount = productHashMap.get(p) - amount;
        productHashMap.put(p, newAmount);
        totalWeight -= amount * p.getWeight();
        System.out.println("Product has reduced by " + amount + " parts");

    }

    /***
     * Changes the destination of the document
     * @param site New destination site
     */
    public void changeDestination(Site site) {
        destination = new Site(site);
    }

    /***
     * Changes the shipment date of the document (if not assigned to transport)
     * @param date New date
     * @throws ATransportModuleException if the document is already attached to a transport
     */
    public void changeDate(LocalDate date) throws ATransportModuleException {
        if (transportId != -1) {
            throw new ChangeDateException("Please remove document from transport " + transportId + " first");

        }
        this.date = date;
    }

    //****************************************************************************************************************** Print functions


    /***
     * Prints the full details of the document including destination and product list
     * @return Formatted string with document information
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(); // build new string
        str.append("Document num ").append(id).append("\n");
        str.append("To destination: ").append(destination.getName()).append("\n");
        str.append("Products: \n");
        for (Product product : productHashMap.keySet())
            // print all products with columns: product name , amount, total weight of product
            str.append(product.getName()).append(productHashMap.get(product)).append(productHashMap.get(product) * product.getWeight()).append("\n");
        str.append("Total weight: ").append(totalWeight);

        return str.toString();


    }

    /***
     * Compares two documents based on their ID
     */
    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ProductListDocument that = (ProductListDocument) other;
        return this.getId() == that.getId();
    }

    /***
     * @return Hash code based on document ID
     */
    @Override
    public final int hashCode() {
        return this.getId();
    }




}
