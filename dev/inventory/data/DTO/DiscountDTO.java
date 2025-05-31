package inventory.data.DTO;

import inventory.domain.DiscountTargetType;
import inventory.domain.DiscountType;

import java.time.LocalDate;

public class DiscountDTO {
    public String id;
    public String description;
    public DiscountTargetType targetType;
    public String targetId;
    public double discountPercentage;
    public LocalDate startDate;
    public LocalDate endDate;
    public DiscountType discountType;

    public DiscountDTO(String id, String description, DiscountTargetType targetType, String targetId,
                       double discountPercentage, LocalDate startDate, LocalDate endDate, DiscountType discountType) {
        this.id = id;
        this.description = description;
        this.targetType = targetType;
        this.targetId = targetId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
    }
}
