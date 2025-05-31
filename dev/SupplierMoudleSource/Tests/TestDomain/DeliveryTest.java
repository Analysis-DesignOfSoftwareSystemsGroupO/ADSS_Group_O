package SupplierMoudleSource.Tests.TestDomain;

import SupplierMoudleSource.DTO.DeliveryDTO;
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

    }

    @BeforeEach
    void setUp() {
        delivery = new Delivery("Temporary Delivery");
    }

    @Test
    void getDeliveryWay() {
        assertEquals("Temporary Delivery", delivery.getDeliveryWay());
    }

    @Test void testSetDeliveryWayDTO() {
        DeliveryDTO deliveryDTO = delivery.getDeliveryDTO();
        assertEquals("Temporary Delivery", deliveryDTO.getDeliveryWay());
    }

}