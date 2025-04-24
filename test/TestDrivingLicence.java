import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import transport_module.*;
public class TestDrivingLicence {
    @Test
    public void test_getCode(){
        DrivingLicence C1 = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        assertEquals("C1", C1.getCode());

    }

    @Test
    public void test_getDescription(){
        DrivingLicence C = new DrivingLicence("Heavy track - maximum 32 tons", "C");
        assertEquals("Heavy track - maximum 32 tons",C.getDescription());
    }

    @Test
    public void test_not_equals_code_is_different(){
        DrivingLicence first = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        DrivingLicence second = new DrivingLicence("Medium track - maximum 12 tons", "C");
        assertNotEquals(second,first);
    }

    @Test
    public void test_not_equals_description_is_different(){
        DrivingLicence first = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        DrivingLicence second = new DrivingLicence("Heavy track - maximum 32 tons", "C1");
        assertNotEquals(second,first);
    }

    @Test
    public void test_equals_different_intstances(){
        DrivingLicence first = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        DrivingLicence second = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        assertEquals(first,second);

    }

    @Test
    public void test_equals_same_intstance(){
        DrivingLicence first = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        assertEquals(first,first);

    }

    @Test
    public void testHashCodeConsistency() {
        DrivingLicence first = new DrivingLicence("Medium track - maximum 12 tons", "C1");
        int hash1 = first.hashCode();
        int hash2 = first.hashCode();
        assertEquals(hash1, hash2, "hashCode should be consistent across multiple calls");
    }
}
