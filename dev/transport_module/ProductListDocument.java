package transport_module;

import java.util.ArrayList;

public class ProductListDocument {
    public static int documentID = 0;
    private int id;
    private Site destination;
    private ArrayList<Product> productsList;

    public ProductListDocument(Site site) throws Exception{
        if (site ==null) throw new Exception("InValid Input");
        id=++documentID;
        destination = new Site(site);
        productsList = new ArrayList<>();
    }
    public void add_product(Product p) throws Exception{
        if (p == null) throw new Exception("Invalid Product");
        productsList.add(new Product (p));
    }

    public int getId() {
        return id;
    }

    public Site getDestination() {
        return destination;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }
}
