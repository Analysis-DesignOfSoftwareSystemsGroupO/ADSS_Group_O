package inventory.domain;

import java.util.List;

public interface InventoryController {


    void addProduct(String id, String name, int minimumStock);
    List<Product> getAllProductsDefinitions();
}
