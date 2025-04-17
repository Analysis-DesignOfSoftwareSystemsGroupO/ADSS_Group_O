package inventory.data;

import inventory.domain.Product;
import java.util.ArrayList;
import java.util.List;

public class InMemoryProductRepository implements ProductRepository {
    // This class will handle the data access for products
    // It will interact with the database to perform CRUD operations
    // on product data
    private final List<Product> products= new ArrayList<>();

    public void saveProduct(Product product) {
        products.add(product);

        // Code to add a product to the database
    }

    public Product getProductById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        // Code to get product by ID from the database
        return null;
    }

    public List<Product> getAllProducts() {
        // Code to get all products from the database
        return products;
    }

    public void updateProduct(Product product) {
        // Code to update the product in the database
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                break;
            }
        }
    }

    public void deleteProduct(String id) {
        // Code to delete the product from the database
        products.removeIf(product -> product.getId().equals(id));
    }
}
