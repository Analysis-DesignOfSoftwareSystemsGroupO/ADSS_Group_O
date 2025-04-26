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
        for (StockItem stockItem: stockItems) {
            if (stockItem.getId().equals(id)) {
                stockItems.remove(stockItem);
                return;
            }
        }
    }

    @Override
    public StockItem getStockItemById(String id) {
        for (StockItem stockItem : stockItems) {
            if (stockItem.getId().equals(id)) {
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
