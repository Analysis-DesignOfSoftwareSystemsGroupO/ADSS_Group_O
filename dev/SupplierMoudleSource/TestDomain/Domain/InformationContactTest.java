package Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.InformationContact;
import static org.junit.jupiter.api.Assertions.*;

class InformationContactTest {
    InformationContact informationContact;

    @BeforeEach
    void setUp() {
         informationContact = new InformationContact("Maxim", "058568", "Cheerleader");
    }

    @Test
    void getContactName() {
        assertEquals("Maxim", informationContact.getContactName());
    }

    @Test
    void setContactName() {
        informationContact.setContactName("Max");
        assertEquals("Max", informationContact.getContactName());
        assertThrows(NullPointerException.class, () -> informationContact.setContactName(null));
    }

    @Test
    void getContactPhone() {
        assertEquals("058568", informationContact.getContactPhone());
    }

    @Test
    void setContactPhone() {
        informationContact.setContactPhone("058567");
        assertEquals("058567", informationContact.getContactPhone());
        assertThrows(NullPointerException.class, () -> informationContact.setContactPhone(null));
    }

    @Test
    void getTitle() {
        assertEquals("Cheerleader", informationContact.getTitle());
    }

    @Test
    void setTitle() {
        informationContact.setTitle("Teacher");
        assertEquals("Teacher", informationContact.getTitle());
        assertThrows(NullPointerException.class, () -> informationContact.setTitle(null));
    }
}