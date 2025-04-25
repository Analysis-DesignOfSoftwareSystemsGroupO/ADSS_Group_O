package Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import SupplierMoudleSource.Domain.*;
import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {
    private Discount discount;
    private Product product;
    private SuppliedItem suppliedItem;



    @BeforeEach
    void setUp() {
         product = new Product("1", "Tuna", "RamiLevi");
         suppliedItem = new SuppliedItem(50, product);
         discount = new Discount(suppliedItem, 10, 5);
    }
    @Test
    void testConstructor() {
        assertThrows(Exception.class, () -> new Discount(null, 10, 5),
                "didnt throw when SuppliedItem is null");
        assertThrows(Exception.class, () -> new Discount(suppliedItem, 1, 51),
                "didnt throw when discount was greater than the price");
    }

    @Test
    void getDiscount() {
        assertEquals(5, discount.getDiscount());
    }

    @Test
    void getSuppliedItem() {
        assertEquals(suppliedItem, discount.getSuppliedItem());
    }

    @Test
    void getProductId() {
        assertEquals("1", discount.getProductId());
    }
}