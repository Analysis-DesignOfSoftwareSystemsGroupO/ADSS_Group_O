package inventory.data;

import inventory.domain.StockItem;
import java.util.List;

public interface StockItemRepository {
    void saveStockItem(StockItem stockItem);
    void updateStockItem(StockItem stockItem);
    void deleteStockItem(String id);
    StockItem getStockItemById(String id);
    List<StockItem> getAllStockItems();
    void printAllStockItems();
}
