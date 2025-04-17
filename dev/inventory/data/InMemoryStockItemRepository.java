package inventory.data;

import inventory.domain.StockItem;

import java.util.ArrayList;
import java.util.List;

public class InMemoryStockItemRepository implements StockItemRepository {
    private final List<StockItem> stockItems = new ArrayList<>();

    @Override
    public void saveStockItem(StockItem stockItem) {
        stockItems.add(stockItem);
    }

    @Override
    public void updateStockItem(StockItem stockItem) {
    }

    @Override
    public void deleteStockItem(String id) {
        stockItems.removeIf(stockItem -> stockItem.getProductId().equals(id));
    }

    @Override
    public StockItem getStockItemById(String id) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getProductId().equals(id)) {
                return stockItem;
            }
        }
        return null;
    }

    @Override
    public List<StockItem> getAllStockItems() {
        return new ArrayList<>(stockItems);
    }

    @Override
    public void printAllStockItems() {
        for (StockItem stockItem : stockItems) {
            System.out.println(stockItem);
        }
    }
}
