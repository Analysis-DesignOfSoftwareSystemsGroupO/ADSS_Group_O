package SupplierMoudle;

public class Discount {
    private Agreement agreement;
    private SuppliedItem suppliedItem;
    private int quantity;
    private int discount;

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
