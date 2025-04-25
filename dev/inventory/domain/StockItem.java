package inventory.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class StockItem {
    private String stockItemId;
    private Product product;
    private int quantity;
    private String location;
    private LocalDate expiryDate;
    private StockItemStatus status;

    public StockItem(int quantity, String location, StockItemStatus status) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Objects.requireNonNull(location, "Location cannot be null for StockItem");
        this.stockItemId = UUID.randomUUID().toString();
        this.quantity = quantity;
        this.location = location;
        this.status = status;
        this.expiryDate = null; // Default to null if not provided


        // Getters and Setters
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public StockItemStatus getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "StockItem{" +
                ", quantity=" + quantity +
                ", location='" + location + '\'' +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                '}';
    }
}
