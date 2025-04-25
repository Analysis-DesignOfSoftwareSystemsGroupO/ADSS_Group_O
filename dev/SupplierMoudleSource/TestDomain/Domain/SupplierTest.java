package Domain;

import SupplierMoudleSource.Domain.InformationContact;
import SupplierMoudleSource.Domain.Product;
import SupplierMoudleSource.Domain.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SupplierTest {
    private Supplier supplier;
    @BeforeEach
    void setUp() {
        supplier = new Supplier("1", "Dudi","Cash" , "5555", "10",
                "401", "Dudi", "0547559841",
                "Manager", "Temporary Delivery", "");
    }

    @Test
    void getID() {
        assertEquals(supplier.getID(), "1");
    }

    @Test
    void getSupplierName() {
        assertEquals(supplier.getSupplierName(), "Dudi");
    }


    @Test
    void getBank() {
        assertEquals(supplier.getBank().getOwnerID(), "1");
    }

    @Test
    void addInformationContact() {
        InformationContact infoContact = new InformationContact("Maxim", "5555", "manager");
        supplier.addInformationContact(infoContact);
        assertTrue(supplier.getInformationContacts().contains(infoContact), "contact was not added" );
        assertThrows(Exception.class, () -> supplier.addInformationContact(null), "contact was null");
        assertThrows(Exception.class, () -> supplier.addInformationContact(infoContact), "contact was added twice");
    }

    @Test
    void getInformationContacts() {
        assertInstanceOf(List.class, supplier.getInformationContacts());
    }

    @Test
    void addProduct() {
        Product product = new Product("1", "Bamba", "osem");
        assertThrows(Exception.class, () -> supplier.addProduct(product, -15), "cannot insert negative price");
        assertThrows(Exception.class, () -> supplier.addProduct(null, 15), "cannot insert null product");
        supplier.addProduct(product, 15);
        assertEquals(supplier.getProduct("1").getProduct(), product);
    }
    

}