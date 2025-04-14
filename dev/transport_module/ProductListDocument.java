package transport_module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListDocument {
    public static int documentID = 0;
    private int id;
    private Site destination;
    private ArrayList<Product> productsList;
    private Map<Product, Integer> productHashMap;
    private int totalWeight;

    public ProductListDocument(Site site) throws Exception{
        if (site ==null) throw new Exception("InValid Input");
        id=++documentID;
        destination = new Site(site);
        productsList = new ArrayList<>();
        productHashMap = new HashMap<>();
        totalWeight = 0;
    }

    public int getTotalWeight(){return totalWeight;}

    public int getId() {
        return id;
    }

    public Site getDestination() {
        return destination;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(); // build new string
        str.append("Document num ").append(id).append("\n");
        str.append("To destination: ").append(destination.getName()).append("\n");
        str.append("Products: \n");
        int i=1;
        for (Product product : productsList)
            str.append(++i).append(product.toString()).append("\n");
        str.append("Total weight: ").append(totalWeight);

        return str.toString();


    }
    public void addProductWithAmount(Product p, int ammount){

    }

    public void addProduct(Product p){
        if(p == null) return;
        boolean is_update = false;

        for (Product product : productsList) {

            if (product.getName().equals(p.getName())) { // if the product is already in the list
                product.addWeight(p.getWeight()); // raise the total weight of the product on truck
                is_update = true;
                break;
            }

        }
        if(!is_update){
            productsList.add(new Product(p));
        }
        totalWeight+=p.getWeight();
        System.out.println("Item has successfully added to document ");

    }
    public void removeProduct(int index){
        if(index<1 || index>productsList.size())
        {
            System.out.println("index out of range - please choose another product to remove");
            return;
        }
        Product p = productsList.get(index-1);
        productsList.remove(index-1);
        totalWeight-=p.getWeight();
        System.out.println("Product has successfully removed from list.");

    }
}
