package inventory.data.DTO;

import inventory.domain.Product;
import inventory.domain.StockItemStatus;

import java.time.LocalDate;

public class StockItemDTO {
    public String stockItemId;
    public Product product;
    public int quantity;
    public String location;
    public LocalDate expiryDate;
    StockItemStatus status;

    public StockItemDTO(String stockItemId, Product product, int quantity,
                        String location, LocalDate expiryDate, StockItemStatus status) {
        this.stockItemId = stockItemId;
        this.product = product;
        this.quantity = quantity;
        this.location = location;
        this.expiryDate = expiryDate;
        this.status = status;
    }
}
