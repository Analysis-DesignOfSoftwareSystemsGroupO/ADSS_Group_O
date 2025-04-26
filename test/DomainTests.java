package test;

import static org.junit.jupiter.api.Assertions.*;

import Transport_Module_Exceptions.ATransportModuleException;
import org.junit.jupiter.api.Test;

import transport_module.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class DomainTests {

    @Test
    public void testCreateTruck() {
        DrivingLicence licence = new DrivingLicence("Medium track - max 12 tons", "C1");
        Truck truck = new Truck(licence, 12000, "1234567");

        assertEquals(licence, truck.getDrivingLicence());
        assertEquals(12000, truck.getMaxWeight());
        assertEquals("1234567", truck.getPlateNumber());
    }

    @Test
    public void testTruckAvailabilityByDefault() {
        DrivingLicence licence = new DrivingLicence("Medium track - max 12 tons", "C1");
        Truck truck = new Truck(licence, 12000, "1234567");

        assertTrue(truck.getAvailablity(LocalDate.now()));
    }

    @Test
    public void testAddProductToDocument() {
        Site destination = new Site("Tel Aviv", "Center");
        ProductListDocument document = new ProductListDocument(destination, "26/04/2025");
        Product product = new Product(1001, "Chairs", 10);

        document.addProduct(product, 5);

        assertEquals(5, document.getProducts().get(product).intValue());
    }

    @Test
    public void testRemoveProductFromDocument() {
        Site destination = new Site("Tel Aviv", "Center");
        ProductListDocument document = new ProductListDocument(destination, "26/04/2025");
        Product product = new Product(1001, "Chairs", 10);

        document.addProduct(product, 10);
        document.reduceAmountFromProduct(product, 4);

        assertEquals(6, document.getProducts().get(product).intValue());
    }

    @Test
    public void testSendTransport() throws ATransportModuleException {
        Truck truck = new Truck(new DrivingLicence("C1", "Medium track"), 12000, "1234567");
        Site site = new Site("Haifa", "North");
        Transport transport = new Transport("26/04/2025", "12:00", truck, site);

        transport.sendTransport();

        assertTrue(transport.isSent());
    }

    @Test
    public void testDriverWithWrongLicense() throws ATransportModuleException {
        DrivingLicence licenceC1 = new DrivingLicence("C1", "Medium track");
        Truck truck = new Truck(licenceC1, 12000, "1234567");

        DrivingLicence licenceB = new DrivingLicence("B", "Private car");
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(licenceB);
        Driver driver = new Driver("John", "123456789", licences);

        Site site = new Site("Haifa", "North");
        Transport transport = new Transport("26/04/2025", "12:00", truck, site);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transport.addDriver(driver);
        });

        String expectedMessage = "Driver license not suitable";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testLoadDocumentToTransport() throws ATransportModuleException {
        Truck truck = new Truck(new DrivingLicence("C1", "Medium track"), 12000, "1234567");
        Site site = new Site("Haifa", "North");
        Transport transport = new Transport("26/04/2025", "12:00", truck, site);

        ProductListDocument document = new ProductListDocument(new Site("Tel Aviv", "Center"), "26/04/2025");

        transport.loadByDocument(document);

        assertTrue(transport.getDocuments().contains(document));
    }

    @Test
    public void testProductNegativeAmount() {
        Site destination = new Site("Tel Aviv", "Center");
        ProductListDocument document = new ProductListDocument(destination, "26/04/2025");
        Product product = new Product(1001, "Chairs", 10);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            document.addProduct(product, -5);
        });

        assertEquals("Amount must be positive", exception.getMessage());
    }

    @Test
    public void testSiteToString() {
        Site site = new Site("Haifa", "North");

        String expected = "Site{name='Haifa', area='North'}";
        assertEquals(expected, site.toString());
    }
}
