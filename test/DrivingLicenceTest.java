
import transport_module.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class DrivingLicenceTest {

    @Test
    public void testLicenceCreation() {
        DrivingLicence dl = new DrivingLicence( "C1");
        assertEquals("C1", dl.getCode());
    }

    @Test
    public void testLicenceEquals() {
        DrivingLicence dl1 = new DrivingLicence( "C1");
        DrivingLicence dl2 = new DrivingLicence( "C1");
        assertEquals(dl1, dl2);
    }

    @Test
    public void testLicenceNotEquals() {
        DrivingLicence dl1 = new DrivingLicence( "C1");
        DrivingLicence dl2 = new DrivingLicence( "B");
        assertNotEquals(dl1, dl2);
    }
}
