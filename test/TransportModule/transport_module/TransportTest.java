package TransportModule.transport_module;

import TransportModule.Transport_Module_Exceptions.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransportTest {

    private Transport transport;
    private Site sourceSite;
    private ProductListDocument document;
    // private Driver driver;
    private Truck truck;
    private String dateStr;
    private String timeStr;
    private LocalDate date;
    private LocalTime time;
    private Product product;

    @BeforeEach
    void setUp() throws ATransportModuleException {
        dateStr = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        timeStr = "09:15";
        date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        time = LocalTime.of(9, 15);
        sourceSite = new Site("Beer Sheva", "Ben Gurion 1");
        transport = new Transport(1, dateStr, timeStr, sourceSite);
        truck = new Truck(new DrivingLicence("C"), 1000, "123-456-78");
        //driver = new Driver("Moshe", List.of(new DrivingLicence("C")));
        product = new Product("123", "Box", 10);
        Site dest = new Site("Tel Aviv", "HaMasger 5");
        document = new ProductListDocument(1, dest, dateStr, "10:30");
        document.addProduct(product, 3);
    }

    @AfterEach
    void tearDown() {
        transport = null;
    }

    @Test
    void getId() {
        assertEquals(1, transport.getId());
    }

    @Test
    void getSource() {
        assertEquals("Beer Sheva", transport.getSource().getName());
    }

    @Test
    void getMaxWeight() {
        assertEquals(0, transport.getMaxWeight());
        transport.setMaxWeight(500);
        assertEquals(500, transport.getMaxWeight());
    }

    @Test
    void getDriver() {
        assertNull(transport.getDriver());
    }

    @Test
    void isSent() {
        assertFalse(transport.isSent());
    }

    @Test
    void getDeparture_time() {
        assertEquals(time, transport.getDeparture_time());
    }

    @Test
    void getDocument() throws ATransportModuleException {
        transport.setMaxWeight(500);
        transport.loadByDocument(document);
        assertEquals(document, transport.getDocument(document.getDestination()));
    }

    @Test
    void getDate() {
        assertEquals(date, transport.getDate());
    }

    @Test
    void getStatus() {
        assertEquals(Transport.Status.waitForShipment, transport.getStatus());
    }

    @Test
    void isSiteIsDestination() throws ATransportModuleException {
        transport.setMaxWeight(100);
        transport.loadByDocument(document);
        assertTrue(transport.isSiteIsDestination(document.getDestination()));
    }

    @Test
    void getTruck() throws ATransportModuleException {
        transport.assignTruck(truck);
        assertEquals(truck, transport.getTruck());
    }

    @Test
    void getAllPLD() throws ATransportModuleException {
        transport.setMaxWeight(1000);
        transport.loadByDocument(document);
        assertEquals(1, transport.getAllPLD().size());
        assertTrue(transport.getAllPLD().contains(document));
    }

//    @Test
//    void addDriver() throws ATransportModuleException {
//        transport.assignTruck(truck);
//        transport.addDriver(driver);
//        assertEquals(driver, transport.getDriver());
//    }

    @Test
    void assignTruck() throws ATransportModuleException {
        transport.assignTruck(truck);
        assertEquals(truck, transport.getTruck());
        assertEquals(truck.getMaxWeight(), transport.getMaxWeight());
    }

//    @Test
//    void sendTransport() throws ATransportModuleException {
//        transport.assignTruck(truck);
//        transport.addDriver(driver);
//        transport.setMaxWeight(1000);
//        transport.loadByDocument(document);
//        transport.sendTransport();
//        assertTrue(transport.isSent());
//    }

    @Test
    void loadByDocument() throws ATransportModuleException {
        transport.setMaxWeight(100);
        transport.loadByDocument(document);
        assertEquals(document, transport.getDocument(document.getDestination()));
    }

    @Test
    void reduceAmountFromProduct() throws ATransportModuleException {
        transport.setMaxWeight(100);
        transport.loadByDocument(document);
        transport.reduceAmountFromProduct(document.getDestination(), product, 1);
        assertEquals(2, document.getProducts().get(product));
    }

    @Test
    void removeDocumentFromTransport() throws ATransportModuleException {
        transport.setMaxWeight(100);
        transport.loadByDocument(document);
        transport.removeDocumentFromTransport(document);
        assertFalse(transport.isSiteIsDestination(document.getDestination()));
    }

    @Test
    void changeDate() throws ATransportModuleException {
        transport.setMaxWeight(100);
        transport.loadByDocument(document);
        String newDateStr = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        transport.changeDate(newDateStr);
        assertEquals(LocalDate.parse(newDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")), transport.getDate());
    }

    @Test
    void changeHour() {
        transport.changeHour("15:45");
        assertEquals(LocalTime.of(15, 45), transport.getDeparture_time());
    }

    @Test
    void changeSourceSite() {
        Site newSource = new Site("Haifa", "Herzl 10");
        transport.changeSourceSite(newSource);
        assertEquals("Haifa", transport.getSource().getName());
    }
//
//    @Test
//    void testToString() throws ATransportModuleException {
//        transport.assignTruck(truck);
//        transport.addDriver(driver);
//        String output = transport.toString();
//        assertTrue(output.contains("Transport num: 1"));
//        assertTrue(output.contains("Beer Sheva"));
//        assertTrue(output.contains("There is no driver") || output.contains("Moshe"));
//    }

    @Test
    void testEquals() throws ATransportModuleException {
        Transport t2 = new Transport(1, dateStr, timeStr, sourceSite);
        assertEquals(transport, t2);
    }

    @Test
    void testHashCode() throws ATransportModuleException {
        Transport t2 = new Transport(1, dateStr, timeStr, sourceSite);
        assertEquals(transport.hashCode(), t2.hashCode());
    }
}
