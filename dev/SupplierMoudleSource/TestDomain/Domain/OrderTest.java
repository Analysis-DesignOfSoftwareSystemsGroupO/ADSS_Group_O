package Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Order order;
    private Agreement agreement;
    private Branch branch;
    private Supplier supplier;
    private SuppliedItem suppliedItem1;
    private SuppliedItem suppliedItem2;
    private Discount discount1;
    private Discount discount2;

    @BeforeEach
    void setUp() {
        branch = new Branch("1", "Petach tikva", "Zikhron Yaakov");
        supplier = new Supplier("1", "Dudi","Cash" , "5555", "10",
                "401", "Dudi", "0547559841",
                "Manager", "Temporary Delivery", "");
        agreement = new Agreement(branch, supplier);
        order = new Order(agreement,  branch);
        Product product = new Product("1", "Bamba", "Lulu");
        Product product2 = new Product("2", "Bamba", "Osem");
        suppliedItem2 = new SuppliedItem(25, product2);
        suppliedItem1 = new SuppliedItem(50, product);
        discount1 = new Discount(suppliedItem1, 5, 100);
        discount2 = new Discount(suppliedItem2, 5, 100);
        supplier.addProduct(product, 50);
        agreement.addItem(suppliedItem1);
    }
    @Test
    void testConstructor() {
        Branch branch1 = new Branch("2", "Petach tikva", "Zikhron Yaakov");
        assertThrows(Exception.class, ()-> new Order(agreement, branch1), "branch id have to match the " +
                "branch in the agreement");
        assertThrows(Exception.class, ()-> new Order(null, branch), "didnt throw when agreement is null");
        assertThrows(Exception.class, () -> new Order(agreement, null), "didnt throw when branch is null");
    }


    @Test
    void addItemToOrder() throws Exception {
        order.addItemToOrder(suppliedItem1.getSuppliedItemID(), 15);
        assertTrue(order.getSuppliedItems().contains(suppliedItem1));
        assertThrows(Exception.class, () -> order.addItemToOrder(suppliedItem2.getSuppliedItemID(), 15),
                "added item that was not in the agreement");
        assertThrows(Exception.class, () -> order.addItemToOrder(null, 15),
                "added null item");
        order.closeOrder();
        assertThrows(Exception.class, () -> order.addItemToOrder(suppliedItem1.getSuppliedItemID(), 15),
                "added item after the order was closed");
    }

    @Test
    void closeOrder() throws Exception {
        assertFalse(order.isOrderClosed(), "order was closed when should not have been");
        assertThrows(Exception.class, () -> order.closeOrder(), "order was closed when it was empty");
        order.addItemToOrder(suppliedItem1.getSuppliedItemID(), 15);
        assertDoesNotThrow(() -> order.closeOrder(), "order was closed when it was empty");
        assertTrue(order.isOrderClosed(), "order was not closed when it should be closed");

    }

    @Test
    void getTotalPrice() throws Exception {
        assertEquals(order.getTotalPrice(), 0, "total price was not equal to 0");
        order.addItemToOrder(suppliedItem1.getSuppliedItemID(), 15);
        assertEquals(order.getTotalPrice(), 750, "total price was not equal to 750");
        agreement.addDiscount(discount1);
        assertEquals(order.getTotalPrice(), 650, "total price was not equal to 650 after adding a discount");
    }


    @Test
    void getBranch() {
        assertEquals(order.getBranch(), branch, "didnt get the branch");
    }
}