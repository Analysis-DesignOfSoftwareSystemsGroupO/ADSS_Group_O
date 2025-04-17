package inventory.data;

import inventory.domain.Product;

import java.util.List;

public interface ProductRepository {
    void saveProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(String id);
    Product getProductById(String id);
    List<Product> getAllProducts();
}
