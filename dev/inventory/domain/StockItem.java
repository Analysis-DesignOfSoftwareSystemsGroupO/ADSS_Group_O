package inventory.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class StockItem {
    private String productId;
    private int quantity;
    private String location;
    private LocalDate expiryDate;
    private StockItemStatus status;

    public StockItem(String productId, int quantity, String location, StockItemStatus status) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Objects.requireNonNull(location, "Location cannot be null for StockItem");
        this.productId = (productId == null) ? UUID.randomUUID().toString() : productId;
        this.quantity = quantity;
        this.location = location;
        this.status = status;
        this.expiryDate = null; // Default to null if not provided
        

        // Getters and Setters
    }

    public String getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "StockItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", location='" + location + '\'' +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                '}';
    }
}
