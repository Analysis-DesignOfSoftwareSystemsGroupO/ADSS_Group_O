package Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.Product;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("1", "Bamba", "Osem");
    }

    @Test
    void getProductID() {
        assertEquals("1", product.getProductID());
    }

    @Test
    void getProductName() {
        assertEquals("Bamba", product.getProductName());
    }

    @Test
    void getProductManufacturer() {
        assertEquals("Osem", product.getProductManufacturer());
    }
}