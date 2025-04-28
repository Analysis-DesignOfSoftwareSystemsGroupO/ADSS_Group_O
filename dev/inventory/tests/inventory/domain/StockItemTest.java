import inventory.domain.StockItem;
import inventory.domain.StockItemStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockItemTest {
    @Test
    void testStockItemConstructor() {
        StockItem stockItem = new StockItem(123, "Somewhere", StockItemStatus.OK, LocalDate.now().plusDays(10));
        assertEquals(123, stockItem.getQuantity());
        assertEquals("Somewhere", stockItem.getLocation());
        assertEquals(StockItemStatus.OK, stockItem.getStatus());
    }

    @Test
    void testStockItemSetQuantity() {
        StockItem stockItem = new StockItem(123, "Somewhere", StockItemStatus.OK, LocalDate.now().plusDays(10));
        assertThrows(IllegalArgumentException.class, () -> {
            stockItem.setQuantity(-1);
        });
    }

    @Test
    void testStockItemSetLocation() {
        StockItem stockItem = new StockItem(123, "Somewhere", StockItemStatus.OK, LocalDate.now().plusDays(10));
        stockItem.setLocation("Anywhere");
        assertEquals("Anywhere", stockItem.getLocation());
    }
}