package inventory.domain;

import inventory.data.*;

import java.util.List;

public class InventoryControllerImpl implements InventoryController {
    // Assuming ProductRepository is a class that provides access to product data
    private final ProductRepository productRepository = new InMemoryProductRepository();

    public InventoryControllerImpl() {
        // Constructor can be used for dependency injection if needed
    }

    public void addProduct(String id, String name, int minimumStock) {
        System.out.println("Adding product: " + name);
        Product productToAdd = new Product(id, name, minimumStock);
        productRepository.saveProduct(productToAdd);
    }

    public List<Product> getAllProductsDefinitions() {
        System.out.println("Getting all products...");
        return productRepository.getAllProducts();
    }


}
