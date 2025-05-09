
import transport_module.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    public void testProductCreation() {
        Product product = new Product(1001, "Chair", 10);
        assertEquals(1001, product.getCode());
        assertEquals("Chair", product.getName());
        assertEquals(10, product.getWeight());
    }

    @Test
    public void testProductEqualsSameCode() {
        Product p1 = new Product(1001, "Chair", 10);
        Product p2 = new Product(1001, "Chair", 12); // different weight same code
        assertEquals(p1, p2);
    }

    @Test
    public void testProductNotEqualsDifferentCode() {
        Product p1 = new Product(1001, "Chair", 10);
        Product p2 = new Product(1002, "Table", 10);
        assertNotEquals(p1, p2);
    }

    @Test
    public void testProductToString() {
        Product product = new Product(1001, "Chair", 10);
        assertTrue(product.toString().contains("Chair"));
        assertTrue(product.toString().contains("1001"));
    }
}
