import transport_module.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TransportTest {

    @Test
    public void testTransportCreation() throws Exception {
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "1234567");
        Site site = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "10:30", truck, site);

        assertEquals(site.getName(), transport.toString().contains("Haifa") ? "Haifa" : "");
        assertFalse(transport.isSent());
    }

    @Test
    public void testLoadDocument() throws Exception {
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "1234567");
        Site site = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "10:30", truck, site);

        Site destination = new Site("Tel Aviv", "Center");
        ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        Product product = new Product(1001, "Chair", 10);
        document.addProduct(product, 5);

        transport.loadByDocument(document);
        assertTrue(transport.isSiteIsDestination("Tel Aviv"));
    }
}
