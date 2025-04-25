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
        assertThrows(Exception.class, ()-> new Delivery("NotVAlid", ""),
                "didnt throw when delivery way was not valid");
        assertThrows(Exception.class, () -> new Delivery("Temporary Delivery", "NotVAlid"),
                "didnt throw when arrival day was not valid");
        assertThrows(Exception.class, () -> new Delivery("Self Pick Up", "NotVAlid"),
                "didnt throw when arrival day was not valid");
        assertThrows(Exception.class, () -> new Delivery("Constant Delivery", "NotVAlid"),
                "didnt throw when arrival day was not valid");
        assertDoesNotThrow(() -> new Delivery("Temporary Delivery", ""),
                "Threw error when delivery way was valid");
        assertDoesNotThrow(() -> new Delivery("Self Pick Up", ""),
                "Threw error when delivery way was valid");
        assertDoesNotThrow(() -> new Delivery("Constant Delivery", "Sunday"),
                "Threw error when delivery way was valid");
    }

    @BeforeEach
    void setUp() {
        delivery = new Delivery("Temporary Delivery", "");
    }

    @Test
    void getDeliveryWay() {
        assertEquals("Temporary Delivery", delivery.getDeliveryWay());
    }

}