package Domain;

public class Discount {
    private Agreement agreement;
    public SuppliedItem suppliedItem;
    public int quantity;
    public int discount;

    public Discount(Agreement agreement, SuppliedItem suppliedItem, int quantity, int discount) {
        this.agreement = agreement;
        this.suppliedItem = suppliedItem;
        this.quantity = quantity;
        this.discount = discount;
    }
    public int getDiscount() {
        return discount;
    }
}
