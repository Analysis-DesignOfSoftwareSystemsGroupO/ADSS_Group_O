package TransportModule.transport_module;

import TransportModule.Transport_Module_Exceptions.ATransportModuleException;
import TransportModule.Transport_Module_Exceptions.InvalidInputException;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {

    private Truck truck;
    private final DrivingLicence licence = new DrivingLicence("c");
    private final String plateNumber = "123-456-78";

    @BeforeEach
    void setUp() throws ATransportModuleException {
        truck = new Truck(licence, 1000, plateNumber);
    }

    @AfterEach
    void tearDown() {
        truck = null;
    }

    @Test
    void getWeight() {
        assertEquals(0, truck.getWeight());
        truck.addWeight(100);
        assertEquals(100, truck.getWeight());
    }

    @Test
    void getMaxWeight() {
        assertEquals(1000, truck.getMaxWeight());
    }

    @Test
    void getPlateNumber() {
        assertEquals(plateNumber, truck.getPlateNumber());
    }

    @Test
    void getAvailablity() {
        LocalDate date = LocalDate.now().plusDays(1);
        assertTrue(truck.getAvailablity(date));
        truck.setDate(date);
        assertFalse(truck.getAvailablity(date));
    }

    @Test
    void getDrivingLicence() {
        assertEquals(licence, truck.getDrivingLicence());
    }

    @Test
    void isOverWeight() {
        assertFalse(truck.isOverWeight());
        assertFalse(truck.addWeight(1001));
    }
// // code will be apply after merging Employee from HR
//    @Test
//    void confirmDriver() throws ATransportModuleException {
//        Driver validDriver = new Driver("D1", Collections.singletonList(new DrivingLicence("C")));
//        Driver invalidDriver = new Driver("D2", Collections.singletonList(DrivingLicence.B));
//
//        assertTrue(truck.confirmDriver(validDriver));
//        assertFalse(truck.confirmDriver(invalidDriver));
//    }

    @Test
    void setDateAndReleaseTruck() {
        LocalDate date = LocalDate.of(2025, 6, 10);
        truck.setDate(date);
        assertFalse(truck.getAvailablity(date));
        truck.releaseTruck(date);
        assertTrue(truck.getAvailablity(date));
    }

    @Test
    void setPlateNumber() {
        truck.setPlateNumber("999-999-99");
        assertEquals("999-999-99", truck.getPlateNumber());
    }

    @Test
    void addWeight() {
        assertTrue(truck.addWeight(500));
        assertEquals(500, truck.getWeight());

        assertFalse(truck.addWeight(600));
        assertEquals(500, truck.getWeight());
    }

    @Test
    void clear() {
        truck.addWeight(500);
        assertEquals(500, truck.getWeight());
        truck.clear();
        assertEquals(0, truck.getWeight());
    }

    @Test
    void testToString() {
        String str = truck.toString();
        assertTrue(str.contains(plateNumber));
        assertTrue(str.contains("Maximum weight"));
    }

    @Test
    void testEquals() throws ATransportModuleException {
        Truck sameTruck = new Truck(licence, 2000, plateNumber);
        Truck diffTruck = new Truck(licence, 2000, "987-654-32");
        assertEquals(truck, sameTruck);
        assertNotEquals(truck, diffTruck);
    }

    @Test
    void testHashCode() throws ATransportModuleException {
        Truck sameTruck = new Truck(licence, 500, plateNumber);
        assertEquals(truck.hashCode(), sameTruck.hashCode());
    }

    @Test
    void testInvalidTruckCreation() {
        assertThrows(InvalidInputException.class, () -> new Truck(null, 1000, "A"));
        assertThrows(InvalidInputException.class, () -> new Truck(licence, 0, "A"));
        assertThrows(InvalidInputException.class, () -> new Truck(licence, 1000, ""));
    }
}
