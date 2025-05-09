import transport_module.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

public class TruckTest {

    @Test
    public void testTruckCreation() throws Exception {
        DrivingLicence licence = new DrivingLicence("Medium Truck", "C1");
        Truck truck = new Truck(licence, 12000, "1234567");
        assertEquals(12000, truck.getMaxWeight());
        assertEquals("1234567", truck.getPlateNumber());
        assertEquals(licence, truck.getDrivingLicence());
    }

    @Test
    public void testTruckAvailability() throws Exception {
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 12000, "1234567");
        LocalDate today = LocalDate.now();
        assertTrue(truck.getAvailablity(today));

        truck.setDate(today);
        assertFalse(truck.getAvailablity(today));
    }

    @Test
    public void testTruckConfirmDriver() throws Exception {
        DrivingLicence licence = new DrivingLicence("Medium Truck", "C1");
        Truck truck = new Truck(licence, 12000, "1234567");

        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(new DrivingLicence("Medium Truck", "C1"));
        Driver driver = new Driver("John", "123456789", licences);

        assertTrue(truck.confirmDriver(driver));
    }

    @Test
    public void testTruckOverWeight() throws Exception {
        Truck truck = new Truck(new DrivingLicence("Medium Truck", "C1"), 10000, "1234567");
        assertTrue(truck.addWeight(5000));
        assertFalse(truck.addWeight(6000));
    }
}
