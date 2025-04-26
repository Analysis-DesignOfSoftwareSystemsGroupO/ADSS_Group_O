import Transport_Module_Exceptions.*;
import transport_module.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

public class DomainTests {
//********************************************************************************************************************** testFullTransportFlow

    /**
     * Tests a full transport flow: creating driver, truck, document, and transport,
     * attaching driver, loading document, and successfully sending the transport.
     */
    @Test
    public void testFullTransportFlow() throws Exception {
        // Create a driver
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(new DrivingLicence("Medium Truck", "C1"));
        Driver driver = new Driver("John Doe", "123456789", licences);

        // Create a truck
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "9876543");

        // Create a product list document
        Site destinationSite = new Site("Tel Aviv", "Center");
        String dateStr = LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ProductListDocument document = new ProductListDocument(destinationSite, dateStr);
        Product product = new Product(2001, "Box", 100);
        document.addProduct(product, 10); // total 1000 kg

        // Create a transport
        Site sourceSite = new Site("Haifa", "North");
        Transport transport = new Transport(dateStr, "08:00", truck, sourceSite);

        // Attach driver
        transport.addDriver(driver);
        assertFalse(transport.isSent());

        // Load document
        transport.loadByDocument(document);
        assertTrue(transport.isSiteIsDestination("Tel Aviv"));

        // Send transport
        transport.sendTransport();
        assertTrue(transport.isSent());
    }
//********************************************************************************************************************** testOverWeightShouldThrowException
    /**
     * Tests that sending a transport with an overweight document throws OverWeightException.
     */
    @Test
    public void testOverWeightShouldThrowException() throws Exception {
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(new DrivingLicence("Heavy Truck", "C"));
        Driver driver = new Driver("Jane Doe", "987654321", licences);
        Truck truck = new Truck(new DrivingLicence("Heavy Truck", "C"), 1000, "1231231");

        Site destination = new Site("Eilat", "South");
        String dateStr = LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        ProductListDocument document = new ProductListDocument(destination, dateStr);
        Product product = new Product(3001, "HeavyMachine", 500);
        document.addProduct(product, 3); // total 1500kg

        Site source = new Site("Jerusalem", "Center");
        Transport transport = new Transport(dateStr, "12:00", truck, source);

        transport.addDriver(driver);

        // Catch ATransportModuleException and assert it is specifically OverWeightException
        ATransportModuleException ex = assertThrows(ATransportModuleException.class, () -> {
            transport.loadByDocument(document);
        });

        assertTrue(ex instanceof OverWeightException, "Expected OverWeightException but got: " + ex.getClass().getSimpleName());
    }

//********************************************************************************************************************** testBasicTransportCreation
    /**
     * Tests that a basic transport creation is successful and is initially not sent.
     */
    @Test
    public void testBasicTransportCreation() throws Exception {
        // Create a truck
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "1111111");

        // Create a basic transport
        Site source = new Site("Haifa", "North");
        String dateStr = LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Transport transport = new Transport(dateStr, "10:00", truck, source);

        assertNotNull(transport);
        assertFalse(transport.isSent());
    }
//********************************************************************************************************************** testLoadDocumentAndSendTransport

    /**
     * Tests loading a valid document and successfully sending the transport.
     */
    @Test
    public void testLoadDocumentAndSendTransport() throws Exception {
        // Create truck and transport
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "2222222");
        Site source = new Site("Haifa", "North");
        Transport transport = new Transport(
                LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                "08:00",
                truck,
                source
        );

        // Create driver and add to transport
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(new DrivingLicence("Medium Truck", "C1"));
        Driver driver = new Driver("Alice", "111111111", licences);
        transport.addDriver(driver);

        // Create document and load
        Site destination = new Site("Tel Aviv", "Center");
        ProductListDocument document = new ProductListDocument(destination,
                LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        document.addProduct(new Product(1001, "Box", 100), 5);

        // Load document into transport
        transport.loadByDocument(document);

        // Send transport
        transport.sendTransport();

        // Check if transport is sent
        assertTrue(transport.isSent());
    }

//********************************************************************************************************************** testOverweightTransportShouldNotSend

    /**
     * Tests that loading an overweight document throws OverWeightException.
     */
    @Test
    public void testOverweightTransportShouldNotSend() throws Exception {
        // Create truck and transport
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 1000, "3333333");
        Site source = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "09:00", truck, source);

        // Create heavy document
        Site destination = new Site("Eilat", "South");
        ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        document.addProduct(new Product(1002, "HeavyMachine", 600), 2);

        // Assert that overweight exception is thrown when trying to load document
        assertThrows(OverWeightException.class, () -> {
            transport.loadByDocument(document);
        });
    }

//********************************************************************************************************************** testAttachDocumentWithMismatchedDateThrows

    /**
     * Tests that attaching a document with a mismatched date throws TransportMismatchException.
     */
    @Test
    public void testAttachDocumentWithMismatchedDateThrows() {
        // Try to attach a document with different date - should throw exception
        assertThrows(Exception.class, () -> {
            Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "4444444");
            Site source = new Site("Haifa", "North");
            Transport transport = new Transport(LocalDate.now().plusDays(2).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "07:00", truck, source);

            Site destination = new Site("Tel Aviv", "Center");
            ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            transport.loadByDocument(document);
        });
    }

//********************************************************************************************************************** testChangeDocumentDestination

    /**
     * Tests changing the destination of a document.
     */
    @Test
    public void testChangeDocumentDestination() throws Exception {
        // Change destination of a document
        Site originalDestination = new Site("Haifa", "North");
        ProductListDocument document = new ProductListDocument(originalDestination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Site newDestination = new Site("Jerusalem", "Center");
        document.changeDestination(newDestination);

        assertEquals("Jerusalem", document.getDestination().getName());
    }

//********************************************************************************************************************** testChangeTruckSuccessfully

    /**
     * Tests changing the truck of a transport successfully.
     */
    @Test
    public void testChangeTruckSuccessfully() throws Exception {
        // Change the truck for an existing transport
        Truck truck1 = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "5555555");
        Truck truck2 = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "6666666");
        Site source = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "11:00", truck1, source);

        transport.changeTruck(truck2);
        assertFalse(transport.toString().contains("5555555"));
    }

//********************************************************************************************************************** testOutOfZoneTransportFlag

    /**
     * Tests that a transport with an out-of-zone destination is flagged properly.
     */
    @Test
    public void testOutOfZoneTransportFlag() throws Exception {
        // Create a transport that has an out-of-area destination
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "7777777");
        Site source = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "12:00", truck, source);

        Site destination = new Site("Be'er Sheva", "South");
        ProductListDocument document = new ProductListDocument(destination, LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        document.addProduct(new Product(1003, "Chair", 50), 10);

        transport.loadByDocument(document);
        assertTrue(transport.toString().contains("out of Area Zone"));
    }

//********************************************************************************************************************** testAddDriverWithWrongLicenceFails

    /**
     * Tests that adding a driver with a wrong licence fails.
     */
    @Test
    public void testAddDriverWithWrongLicenceFails() {
        // Try to assign a driver with wrong license - should fail
        assertThrows(Exception.class, () -> {
            Truck truck = new Truck(new DrivingLicence("Heavy Truck", "C"), 30000, "8888888");
            Site source = new Site("Tel Aviv", "Center");
            Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "13:00", truck, source);

            ArrayList<DrivingLicence> licences = new ArrayList<>();
            licences.add(new DrivingLicence("Small Car", "B")); // wrong licence
            Driver driver = new Driver("BadDriver", "123456789", licences);

            transport.addDriver(driver);
        });
    }

//********************************************************************************************************************** testChangeTransportDate

    /**
     * Tests changing the date of a transport successfully.
     */
    @Test
    public void testChangeTransportDate() throws Exception {
        // Change transport date
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "9999999");
        Site source = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(2).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "14:00", truck, source);

        String newDate = LocalDate.now().plusDays(3).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        transport.changeDate(newDate);

        assertEquals(LocalDate.parse(newDate, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), transport.getDate());
    }

//********************************************************************************************************************** testChangeTransportHour

    /**
     * Tests changing the departure hour of a transport successfully.
     */
    @Test
    public void testChangeTransportHour() throws Exception {
        // Change transport departure time
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "1010101");
        Site source = new Site("Haifa", "North");
        Transport transport = new Transport(LocalDate.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), "08:00", truck, source);

        transport.changeHour("15:30");
        assertEquals(15, transport.getDeparture_time().getHour());
        assertEquals(30, transport.getDeparture_time().getMinute());
    }
}