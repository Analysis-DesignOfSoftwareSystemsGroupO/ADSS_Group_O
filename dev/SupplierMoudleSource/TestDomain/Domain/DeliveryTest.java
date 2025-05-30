package Domain;

import SupplierMoudleSource.Domain.Delivery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryTest {
    private Delivery delivery;
    @Test
    void testConstructor() {
        assertThrows(Exception.class, ()-> new Delivery("NotVAlid"),
                "didnt throw when delivery way was not valid");
        assertThrows(Exception.class, () -> new Delivery("Temporary Delivery"),
                "didnt throw when arrival day was not valid");
        assertThrows(Exception.class, () -> new Delivery("Self Pick Up"),
                "didnt throw when arrival day was not valid");
        assertThrows(Exception.class, () -> new Delivery("Constant Delivery"),
                "didnt throw when arrival day was not valid");

    }

    @BeforeEach
    void setUp() {
        delivery = new Delivery("Temporary Delivery");
    }

    @Test
    void getDeliveryWay() {
        assertEquals("Temporary Delivery", delivery.getDeliveryWay());
    }

}