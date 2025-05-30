package SupplierMoudleSource.Repository;


import DTO.ProductDTO;
import SupplierMoudleSource.DAO.ProductDAO;
import SupplierMoudleSource.Domain.Product;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private Map<String, ProductDTO> products;
    private ProductDAO productDAO;

    //singleton database
    private static ProductRepository productRepository = null;
    public static ProductRepository getInstance() {
        if (productRepository == null) {
            productRepository = new ProductRepository();
        }
        return productRepository;
    }
    private ProductRepository(){
        products = new HashMap<>();
        productDAO = new ProductDAO();
    }

public String addProduct(String productName, String manufacturer, int shelfLifeDays) throws Exception {
        if (shelfLifeDays < 1){
            throw new IllegalArgumentException("Product cannot be have shelfLife < 1");
        }
        String id = "-1";
        try {
            id = productDAO.addProduct(productName, manufacturer, shelfLifeDays);

        }catch (Exception e){

        }
    if (id.equals("-1")){
        throw new Exception("DB ERROR");
    }
    return id;
}

    public Product getProduct(String productID) throws Exception {
        if (products.get(productID) != null) {
            return new Product(products.get(productID));
        }
        ;
        ProductDTO productDTO;
        try {
            productDTO = productDAO.getProduct(productID);

        } catch (Exception e) {
            throw new Exception("Product does not exist");
        }
        return new Product(productDTO);
    }

}
