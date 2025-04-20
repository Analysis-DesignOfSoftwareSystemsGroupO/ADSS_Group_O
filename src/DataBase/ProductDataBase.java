package DataBase;

import Domain.Order;
import Domain.Product;
import Domain.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDataBase {
    private Map<String, Product> products;
    private int productID = 3000;
    //singleton database
    private static ProductDataBase productDataBase = null;
    public static ProductDataBase getInstance() {
        if (productDataBase == null) {
            productDataBase = new ProductDataBase();
        }
        return productDataBase;
    }
    private ProductDataBase(){
        products = new HashMap<>();
    }

public void addProduct(String productName) {
        if (productName == null){
            return;
        }
        if (!products.containsKey(productName)) {
            Product p = new Product(Integer.toString(productID), productName);
            products.put(Integer.toString(productID), p);
            productID++;
        }
    }



}
