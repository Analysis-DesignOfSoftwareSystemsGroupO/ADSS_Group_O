import inventory.domain.Discount;
import inventory.domain.DiscountTargetType;
import inventory.domain.DiscountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {
    @Test
    void testDiscountConstructor() {
        Discount discount = new Discount("Black Friday", DiscountTargetType.PRODUCT, "123",
                20.0, null, null, DiscountType.STORE);
        assertEquals("Black Friday", discount.getDescription());
        assertEquals("123", discount.getTargetId());
        assertEquals(20.0, discount.getDiscountPercentage());
    }

    @Test
    void testDiscountSetPercentage() {
        Discount discount = new Discount("Black Friday", DiscountTargetType.PRODUCT, "123",
                20.0, null, null, DiscountType.STORE);
        assertThrows(IllegalArgumentException.class, () -> {
            discount.setDiscountPercentage(150.0);
        });
    }

    @Test
    void testDiscountTypeToString() {
        Discount discount = new Discount("Black Friday", DiscountTargetType.PRODUCT, "123",
                20.0, null, null, DiscountType.STORE);
        assertEquals("STORE", discount.getDiscountType().toString());
    }

    @Test
    void testDiscountIsActive() {
        Discount discount = new Discount("Black Friday", DiscountTargetType.PRODUCT, "123",
                20.0, null, null, DiscountType.STORE);
        assertFalse(discount.isActive()); // Should be false since start and end dates are null
    }
}