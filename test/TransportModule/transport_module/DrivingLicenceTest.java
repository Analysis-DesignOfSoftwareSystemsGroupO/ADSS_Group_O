package TransportModule.transport_module;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrivingLicenceTest {

    @Test
    void constructorAndGetCode() {
        DrivingLicence dl = new DrivingLicence("C1");
        assertEquals("C1", dl.getCode());
    }

    @Test
    void copyConstructor() {
        DrivingLicence original = new DrivingLicence("B");
        DrivingLicence copy = new DrivingLicence(original);

        assertNotSame(original, copy);
        assertEquals(original.getCode(), copy.getCode());
        assertEquals(original, copy);
    }

    @Test
    void testToString() {
        DrivingLicence dl = new DrivingLicence("C");
        assertEquals("C", dl.toString());
    }

    @Test
    void testEquals() {
        DrivingLicence dl1 = new DrivingLicence("C1");
        DrivingLicence dl2 = new DrivingLicence("C1");
        DrivingLicence dl3 = new DrivingLicence("C");

        assertEquals(dl1, dl2);
        assertNotEquals(dl1, dl3);
        assertNotEquals(null, dl1);
        assertNotEquals("NotALicence",dl1 );
    }

    @Test
    void testHashCode() {
        DrivingLicence dl1 = new DrivingLicence("C1");
        DrivingLicence dl2 = new DrivingLicence("C1");

        assertEquals(dl1.hashCode(), dl2.hashCode());
    }
}
