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

    public StockItem(int quantity, String locationStatus, StockItemStatus status, LocalDate expiryDate) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Objects.requireNonNull(locationStatus, "Location cannot be null for StockItem");
        this.stockItemId = UUID.randomUUID().toString();
        this.quantity = quantity;
        this.location = locationStatus;
        this.status = status;
        this.expiryDate = expiryDate;


        // Getters and Setters
    }

    public void setStatus(StockItemStatus status) {
        this.status = status;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStockItemId(){
        return stockItemId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public StockItemStatus getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

   public void setLocation(String location) {
        this.location = location;
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
