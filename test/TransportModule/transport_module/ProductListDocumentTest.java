package TransportModule.transport_module;

import TransportModule.Transport_Module_Exceptions.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductListDocumentTest {

    private ProductListDocument document;
    private Site site;
    private Product product;
    private String date;
    private LocalTime time;
    private String timeeStr;
    private LocalDate dated;

    @BeforeEach
    void setUp() throws ATransportModuleException {
        site = new Site("Tel Aviv", "Hamasger 3");
        date = "04/06/2025";
        dated = LocalDate.now().plusDays(1);
        time = LocalTime.of(10, 30);
        product = new Product( "10","Box",10);
        timeeStr = "10:30";
        document = new ProductListDocument(1, site, date, "10:30");
    }

    @AfterEach
    void tearDown() {
        document = null;
        site = null;
        product = null;
    }

    @Test
    void getDate() {
        assertEquals(LocalDate.of(2025, 6, 4), document.getDate());
    }

    @Test
    void getId() {
        assertEquals(1, document.getId());
    }

    @Test
    void getDestination() {
        assertEquals("Tel Aviv", document.getDestination().getName());
    }

    @Test
    void getApproximatedArriavaleTime() {
        assertEquals(time, document.getApproximatedArriavaleTime());
    }

    @Test
    void addProductAndGetTotalWeight() throws ATransportModuleException {
        document.addProduct(product, 3);
        assertEquals(30, document.getTotalWeight());
        Map<Product, Integer> products = document.getProducts();
        assertTrue(products.containsKey(product));
        assertEquals(3, products.get(product));
    }

    @Test
    void reduceAmountFromProduct() throws ATransportModuleException {
        document.addProduct(product, 5);
        document.reduceAmountFromProduct(product, 2);
        assertEquals(3, document.getProducts().get(product));
    }

    @Test
    void reduceAmountFromProductToZero() throws ATransportModuleException {
        document.addProduct(product, 2);
        document.reduceAmountFromProduct(product, 2);
        assertFalse(document.getProducts().containsKey(product));
    }

    @Test
    void attachTransportToDocumentAndGetTransport() throws ATransportModuleException {
        //    public Transport(int id, String d, String time, Site s) throws ATransportModuleException {
        Transport transport = new Transport(10, date, timeeStr, site);
        document.attachTransportToDocument(transport.getId());
        assertEquals(transport.getId(), document.getTransportId());
    }

    @Test
    void changeDestination() {
        Site newSite = new Site("Haifa", "Hahashmonaim 5");
        document.changeDestination(newSite);
        assertEquals("Haifa", document.getDestination().getName());
    }

    @Test
    void changeDateWithoutTransport() throws ATransportModuleException {
        LocalDate newDate = dated.plusDays(1);
        document.changeDate(newDate);
        assertEquals(newDate, document.getDate());
    }

    @Test
    void testEqualsAndHashCode() throws ATransportModuleException {
        ProductListDocument doc2 = new ProductListDocument(1, site, date, timeeStr);
        assertEquals(document, doc2);
        assertEquals(document.hashCode(), doc2.hashCode());
    }

    @Test
    void testToString() throws ATransportModuleException {
        document.addProduct(product, 2);
        String str = document.toString();
        assertTrue(str.contains("Document num 1"));
        assertTrue(str.contains("Box"));
        assertTrue(str.contains("20")); // 2*10
    }

    @Test
    void testSetArriavleTime() {
        LocalTime newTime = LocalTime.of(14, 0);
        document.setArriavleTime(newTime);
        assertEquals(newTime, document.getApproximatedArriavaleTime());
    }

    @Test
    void testRealiseFromTransport() throws ATransportModuleException {
        //     public Transport(int id, String d, String time, Site s) throws ATransportModuleException {
        Transport transport = new Transport(20, date, timeeStr,site);
        document.attachTransportToDocument(transport.getId());
        document.realiseFromTransport();
        assertEquals(-1, document.getTransportId());

    }

    @Test
    void testInvalidDateFormat() {
        assertThrows(InvalidDateFormatException.class, () -> {
            new ProductListDocument(2, site, "31-12-2025", "10:30");
        });
    }

    @Test
    void testInvalidTimeFormat() {
        assertThrows(InvalidInputException.class, () -> {
            new ProductListDocument(3, site, date, "25:00");
        });
    }



}
