package inventory.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class StockItem {
    private String id;
    private Product product;
    private int quantity;
    private String location;
    private LocalDate expiryDate;
    private StockItemStatus status;

    public StockItem(String id, Product product, int quantity, String location, StockItemStatus status) {
        Objects.requireNonNull(product, "Product cannot be null for StockItem");
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Objects.requireNonNull(location, "Location cannot be null for StockItem");
        this.id = (id == null) ? UUID.randomUUID().toString() : id; // TODO: Decide if the id should be auto-generated or provided
        this.product = product;
        this.quantity = quantity;
        this.location = location;
        this.status = status;
        this.expiryDate = null; // Default to null if not provided

        // Getters and Setters
    }
}
