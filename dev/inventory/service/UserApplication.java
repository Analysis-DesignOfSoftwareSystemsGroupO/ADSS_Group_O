package inventory.service;

import inventory.domain.*;

import java.util.List;

public class UserApplication {
    InventoryController inventoryController = new InventoryControllerImpl();

    public void addProduct(String id, String name, int minimumStock){
        inventoryController.addProduct(id, name, minimumStock);
    }


    public List<Product> getAllProductsDefinitions() {
        return inventoryController.getAllProductsDefinitions();

    }
}
