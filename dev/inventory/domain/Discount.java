package inventory.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.RoundingMode;
import java.util.Objects;

public class Discount {
    private String id;
    private String description;
    private DiscountTargetType targetType;
    private String targetId; // Product ID or Category ID
    private BigDecimal discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;


    public Discount(String id, String description, DiscountTargetType targetType, String targetId, BigDecimal discountPercentage) {
        Objects.requireNonNull(id, "Discount ID cannot be null");
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(targetType, "Target type cannot be null");
        Objects.requireNonNull(targetId, "Target ID cannot be null");
        Objects.requireNonNull(discountPercentage, "Discount percentage cannot be null");

        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 || discountPercentage.compareTo(new BigDecimal(100)) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        this.id = id;
        this.description = description;
        this.targetType = targetType;
        this.targetId = targetId;
        this.discountPercentage = discountPercentage;
        this.isActive = true; // Default to active
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(DiscountTargetType targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


}
