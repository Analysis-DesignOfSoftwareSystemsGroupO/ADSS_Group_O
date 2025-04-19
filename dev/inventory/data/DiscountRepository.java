package inventory.data;

import inventory.domain.Discount;

import java.util.List;

public interface DiscountRepository {
    void saveDiscount(Discount discount);

    void updateDiscount(Discount discount);

    void deleteDiscount(String id);

    Discount getDiscountById(String id);

    List<Discount> getAllDiscounts();

    void printAllDiscounts();
}
