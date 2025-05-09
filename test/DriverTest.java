import transport_module.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

public class DriverTest {

    @Test
    public void testDriverAvailability() {
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(new DrivingLicence("Medium Truck", "C1"));
        Driver driver = new Driver("John", "123456789", licences);

        LocalDate date = LocalDate.now();
        assertTrue(driver.isavailable(date)); // driver is available
    }

    @Test
    public void testDriverAssignRelease() {
        ArrayList<DrivingLicence> licences = new ArrayList<>();
        licences.add(new DrivingLicence("Medium Truck", "C1"));
        Driver driver = new Driver("John", "123456789", licences);

        LocalDate date = LocalDate.now();
        driver.assignToMission(date);
        assertFalse(driver.isavailable(date)); // driver now busy

        driver.release(date);
        assertTrue(driver.isavailable(date)); // driver released
    }
}
