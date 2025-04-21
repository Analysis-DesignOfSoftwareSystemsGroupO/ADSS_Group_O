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

public void addProduct(Product product) {
        if (product == null){
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!products.containsKey(product.getProductName())) {
            products.put(product.getProductName(), product);
        }
    }
    public Product getProduct(String productname){
        return products.get(productname);
    }



}
