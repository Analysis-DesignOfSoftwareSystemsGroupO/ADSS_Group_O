package inventory.data;

import inventory.domain.Discount;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDiscountRepository implements DiscountRepository {
    private final List<Discount> discounts = new ArrayList<>();

    @Override
    public void saveDiscount(Discount Discount) {
        discounts.add(Discount);
    }

    @Override
    public void updateDiscount(Discount Discount) {
        for (int i = 0; i < discounts.size(); i++) {
            if (discounts.get(i).getId().equals(Discount.getId())) {
                discounts.set(i, Discount);
                return;
            }
        }
    }

    @Override
    public void deleteDiscount(String id) {
        discounts.removeIf(Discount -> Discount.getId().equals(id));
    }

    @Override
    public Discount getDiscountById(String id) {
        for (Discount Discount : discounts) {
            if (Discount.getId().equals(id)) {
                return Discount;
            }
        }
        return null;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discounts;
    }

    @Override
    public void printAllDiscounts() {
        for (Discount discount : getAllDiscounts()) {
            System.out.println(discount);
        }
    }
}