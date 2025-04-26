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

    /**
     * a constructor - create a new document
     */
    public ProductListDocument(Site site, String d) throws ATransportModuleException {
        if (site == null)
            throw new InvalidInputException(); // if the site is null - don't create a document

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(d, formatter);
        }
        catch (DateTimeParseException e){
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
        System.out.println("Document number "+id+" has successfully created!");
    }

    public void attachTransportToDocument(Transport transport) throws ATransportModuleException {
        if (transport == null){
            throw new InvalidInputException();
        }
        if(!transport.getDate().equals(date)){
            throw new ATransportMismatchException("Transport date doesn't match to document shipment date.");
        }
        transportId = transport.getId();
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTransportId(){
        return transportId;
    }

    /**
     * get the total weight of the products in document
     */
    public int getTotalWeight() {
        return totalWeight;
    }

    /**
     * get the document id
     */
    public int getId() {
        return id;
    }

    /**
     * get the destination
     */
    public Site getDestination() {
        return destination;
    }

    /**
     * a function that prints the document details
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

    /**
     * a function that adds product to document, if
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

    /**
     * a function that reduces the amount of product from the list - if remove all amounts of product, product will be deleted
     */
    public void reduceAmountFromProduct(Product p, int amount) throws ATransportModuleException {
        if (p == null || amount < 1 ) {
            throw new InvalidInputException();

        }
        if( productHashMap.get(p) == null)
        {
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
        System.out.println("Product has reduced by "+amount+" parts");

    }
    public void changeDestination(Site site){
        destination = new Site(site);
    }

    @Override
    public final boolean equals(Object other){
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ProductListDocument that = (ProductListDocument) other;
        return this.getId()==that.getId();
    }

    @Override
    public final int hashCode(){
        return this.getId();
    }

    public void changeDate(LocalDate date) throws ATransportModuleException {
        if( transportId != -1){
            throw new ChangeDateException("Please remove document from transport "+transportId+" first");

        }
        this.date = date;
    }


}
