package Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.*;
import static org.junit.jupiter.api.Assertions.*;

class SuppliedItemTest {
    private SuppliedItem suppliedItem;
    private Product product;
    @BeforeEach
    void setUp() {
         product = new Product("1", "Bamba", "Lulu");
         suppliedItem = new SuppliedItem(15, product);
    }

    @Test
    void testConstructor() {
        assertThrows(Exception.class, () -> suppliedItem = new SuppliedItem(-15, product),
                "cannot create a supplied item with a negative price");
        assertThrows(Exception.class, () -> suppliedItem = new SuppliedItem(15, null),
                "cannot create a supplied item with null product");
    }

    @Test
    void getProduct() {
        assertEquals(product, suppliedItem.getProduct());
    }

    @Test
    void getSuppliedItemID() {
        assertEquals("1", suppliedItem.getSuppliedItemID());
    }

    @Test
    void getSuppliedItemPrice() {
        assertEquals(15, suppliedItem.getSuppliedItemPrice());
    }
}