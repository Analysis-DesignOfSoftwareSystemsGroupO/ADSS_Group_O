package transport_module;

import java.util.HashMap;
import java.util.Map;

public class ProductListDocument {
    public static int documentID = 0; // global variable for indexing documents.
    private final int id; // document id
    private Site destination; // document destination
    private final Map<Product, Integer> productHashMap; // a map of products and amount of each product
    private int totalWeight; // total weight of the products in document
    private int transportId;

    /**
     * a constructor - create a new document
     */
    public ProductListDocument(Site site) throws Exception {
        if (site == null) throw new Exception("InValid Input"); // if the site is null - don't create a document
        id = ++documentID; // give index to document
        destination = new Site(site); // set the destination of the document
        productHashMap = new HashMap<>(); // create a map for the document
        totalWeight = 0; // set the total weight to document
        transportId = -1;
        System.out.println("Document number "+id+" has successfully created!");
    }

    public void attachTransportToDocument(int id){
        if(id>-1){
            transportId = id;
        }
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
    public void addProduct(Product p, int amount) {
        if (p == null || amount < 1) {
            System.out.println("Invalid input");
            return;
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
    public void reduceAmountFromProduct(Product p, int amount) {
        if (p == null || amount < 1 || productHashMap.get(p) == null || amount > productHashMap.get(p)) {
            System.out.println("Invalid input");
            return;
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


}
