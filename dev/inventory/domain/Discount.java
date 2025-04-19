package inventory.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Discount {
    private String id;
    private String description;
    private DiscountTargetType targetType;
    private String targetId; // Product ID or Category ID
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public Discount(String description, DiscountTargetType targetType,
                    String targetId, double discountPercentage, LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(targetType, "Target type cannot be null");
        Objects.requireNonNull(targetId, "Target ID cannot be null");

        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }

        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.targetType = targetType;
        this.targetId = targetId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        if(discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static String typeToString(DiscountTargetType type) {
        switch (type) {
            case PRODUCT:
                return "Product";
            case CATEGORY:
                return "Category";
            default:
                throw new IllegalArgumentException("Unknown discount target type: " + type);
        }
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", targetType=" + targetType +
                ", targetId='" + targetId + '\'' +
                ", discountPercentage=" + discountPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public boolean isActive() {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
