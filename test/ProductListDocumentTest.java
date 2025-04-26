import transport_module.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class ProductListDocumentTest {

    @Test
    public void testDocumentCreation() throws Exception {
        Site destination = new Site("Haifa", "North");
        ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        assertEquals(destination.getName(), document.getDestination().getName());
    }

    @Test
    public void testAddProduct() throws Exception {
        Site destination = new Site("Haifa", "North");
        ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Product product = new Product(1001, "Chair", 10);

        document.addProduct(product, 5);
        assertEquals(50, document.getTotalWeight());
    }

    @Test
    public void testReduceProduct() throws Exception {
        Site destination = new Site("Haifa", "North");
        ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Product product = new Product(1001, "Chair", 10);

        document.addProduct(product, 5);
        document.reduceAmountFromProduct(product, 2);

        assertEquals(30, document.getTotalWeight());
    }
}
