package Domain;

public class Discount {
    public SuppliedItem suppliedItem;
    public int quantity;
    public int discount;

    public Discount(SuppliedItem suppliedItem, int quantity, int discount) {
        this.suppliedItem = suppliedItem;
        this.quantity = quantity;
        this.discount = discount;
    }
    public int getDiscount() {
        return discount;
    }
}
