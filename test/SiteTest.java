import transport_module.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SiteTest {

    @Test
    public void testSiteCreation() {
        Site site = new Site("Haifa", "North");
        assertEquals("Haifa", site.getName());
        assertEquals("North", site.getArea());
    }

    @Test
    public void testSiteEquals() {
        Site s1 = new Site("Haifa", "North");
        Site s2 = new Site("Haifa", "North");
        assertEquals(s1, s2);
    }

    @Test
    public void testSiteNotEquals() {
        Site s1 = new Site("Haifa", "North");
        Site s2 = new Site("Tel Aviv", "Center");
        assertNotEquals(s1, s2);
    }
}
